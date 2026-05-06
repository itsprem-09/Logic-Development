# EcoBite — Part 3 of 3: Interview Questions & Answers

---

## CATEGORY 1: Project Overview

**Q1: Explain the EcoBite project in 2 minutes.**

EcoBite is a hyper-local food rescue platform that connects food donors (restaurants, cafes) with receivers (individuals, charities) to reduce food waste. It is built as a .NET 8 Web API backend with a React frontend.

The backend handles four main concerns:
1. Authentication — JWT-based login with role-based access (Donor vs Receiver).
2. Spatial Discovery — Receivers query nearby food using GPS coordinates and radius search powered by NetTopologySuite.
3. Real-Time Updates — SignalR WebSockets push stock changes instantly to all users when a claim is made.
4. Automated Cleanup — A Hangfire recurring job runs every minute to expire unclaimed reservations and restore stock.

The system follows a closed loop: Donor posts food → Receiver finds it nearby → Receiver claims it (gets a QR code) → Donor scans QR code → Pickup verified → Review submitted.

---

**Q2: What is the complete system workflow?**

1. Donor registers and posts a donation with GPS coordinates and expiry time.
2. Receiver calls GET /api/Donation/nearby?lat=X&lng=Y&radiusKm=5 to find active food within 5km.
3. Receiver calls POST /api/Claims to reserve items. Backend: runs a DB transaction, decrements stock, generates a GUID (ClaimCode), sets a 30-minute deadline, broadcasts SignalR event.
4. All connected users see the stock drop in real-time via WebSocket.
5. Receiver shows QR code (containing ClaimCode) to Donor.
6. Donor calls POST /api/Claims/verify with the GUID. Backend validates ownership, marks claim as PickedUp, creates audit record, sends targeted SignalR notification to the Receiver.
7. Receiver submits a review (only after PickedUp status confirmed).
8. Every minute, Hangfire finds Reserved claims past their deadline and restores stock.

---

## CATEGORY 2: Database & EF Core

**Q3: What is normalization and how is it applied in EcoBite?**

Normalization organizes a database to reduce redundancy and improve integrity.

- 1NF: All columns hold atomic values. No arrays or repeating groups anywhere in EcoBite.
- 2NF: All non-key columns depend on the full primary key. All tables use single-column INT primary keys so this is automatically satisfied.
- 3NF: No transitive dependencies. Example: PickupVerifications is a separate table. If we had stored VerifiedByUserId, ScannedAt, IsSuccessful directly in Claims, those fields would depend on "who verified" not on the claim itself — that is a transitive dependency. Separating them into PickupVerifications achieves 3NF.

---

**Q4: What is RowVersion / [Timestamp] and why is it used?**

RowVersion is a SQL Server TIMESTAMP column (8 bytes) that SQL Server automatically updates every time that row is modified. It is used for optimistic concurrency control.

How it works in EcoBite:
1. EF Core loads a Donation and captures its RowVersion value.
2. While processing the claim, another user may claim the same item.
3. When EF Core calls SaveChanges(), it generates: WHERE Id = @id AND RowVersion = @captured_value.
4. If another user changed the row, RowVersion changed, so 0 rows match.
5. EF Core throws DbUpdateConcurrencyException.
6. The controller catches it, rolls back the transaction, and returns 409 Conflict.

This prevents double-booking — two people cannot claim the last item even if their requests arrive simultaneously.

---

**Q5: What is the difference between OriginalQuantity and RemainingQuantity?**

OriginalQuantity = how many items the donor originally listed. It never changes after creation. It is used for reporting, showing "X out of Y remaining."

RemainingQuantity = current available stock. It decrements when claims are created and increments when claims expire. It reaches 0 when all items are claimed, triggering Status = Completed.

---

**Q6: Why are some FKs nullable (int?) in Claims, Messages, and Reviews?**

SQL Server forbids multiple cascade delete paths to the same table (multi-path cascade). For example:
- Path 1: Users → Claims (via ReceiverId)
- Path 2: Users → Donations → Claims (via DonorId → DonationId)

Two paths from Users to Claims would cause a migration error. Making ReceiverId nullable tells EF Core to use NO ACTION (not CASCADE) for that FK, breaking the cycle. The same logic applies to Messages (SenderId, ReceiverId) and Reviews (ReviewerId, TargetUserId).

---

**Q7: What is DbContext and what is its lifetime?**

DbContext is EF Core's Unit of Work implementation. It:
- Tracks all loaded entity objects in memory (Change Tracker).
- Translates LINQ queries to SQL.
- Batches and sends changes on SaveChanges().
- Manages database connections.

In EcoBite it is registered as Scoped: `AddDbContext<ApplicationDbContext>()`. One instance is created per HTTP request and disposed at the end of the request. This is the recommended lifetime because DbContext is not thread-safe.

In Hangfire (background thread), there is no HTTP request, so we use IServiceScopeFactory to manually create a new scope and get a fresh DbContext for each job run.

---

**Q8: What is the difference between .Find() and .FirstOrDefault() in EF Core?**

.Find(id) — Checks the Change Tracker first (in-memory cache). If the entity was already loaded in this request, returns it without a DB query. Falls back to DB only if not found in memory. Best for PK lookups.

.FirstOrDefault(predicate) — Always queries the database. Translates to SELECT TOP 1. Used when filtering by non-PK columns or when you need .Include() for eager loading.

In ClaimsController: .Find() is used to load the Donation (fast PK lookup). .FirstOrDefault() with .Include() is used in VerifyPickup because we need to load the related Donation in the same query.

---

## CATEGORY 3: JWT Authentication

**Q9: Explain how JWT works in EcoBite end-to-end.**

1. User POSTs to /api/Auth/login with email + password.
2. Controller finds the user, calls BCrypt.Verify() to check the password hash.
3. If valid, GenerateJwtToken() creates a JWT with claims: sub (userId), email, role.
4. JWT is signed with HMAC-SHA256 using the secret key from appsettings.json.
5. Token returned to client. Client stores it (localStorage or memory).
6. Client sends token in every request header: Authorization: Bearer <token>.
7. UseAuthentication() middleware reads the header, validates the signature and expiry.
8. HttpContext.User is populated with the claims from the token.
9. [Authorize(Roles = "Donor")] checks the role claim. Returns 403 if wrong role.

---

**Q10: What claims are embedded in the JWT and why?**

Three claims are embedded:
- JwtRegisteredClaimNames.Sub = user.Id.ToString() — the subject, uniquely identifies the user. Read in controllers as User.FindFirst(ClaimTypes.NameIdentifier).
- JwtRegisteredClaimNames.Email = user.Email — useful for display/logging.
- ClaimTypes.Role = user.Role.ToString() — enables [Authorize(Roles = "Donor")].

ClaimTypes.Role uses the full Microsoft URI: http://schemas.microsoft.com/ws/2008/06/identity/claims/role. ASP.NET Core's authorization system reads this specific URI to enforce role-based access.

---

**Q11: What does ValidateIssuer and ValidateAudience do?**

They prevent token misuse across systems.
- ValidIssuer = "EcoBiteApp" — the token must have been created by "EcoBiteApp". Prevents someone using a JWT from another application.
- ValidAudience = "EcoBiteUsers" — the token must be intended for "EcoBiteUsers". Prevents an access token from one service being used in another.

Both are configured in appsettings.json under the Jwt section.

---

## CATEGORY 4: SignalR

**Q12: What is SignalR and why is it used instead of REST polling?**

SignalR is a real-time communication library that maintains a persistent connection between the server and clients using WebSockets (with fallback to SSE/Long Polling).

Without SignalR, the React app would need to poll GET /api/Donation/nearby every few seconds to see updated quantities — wasteful and not instant.

With SignalR, when any user claims an item, the server immediately pushes a ReceiveStockUpdate event to all connected clients. React clients react within milliseconds. No polling needed.

---

**Q13: What is the difference between Clients.All and Clients.User()?**

Clients.All.SendAsync("event", data) — broadcasts to EVERY connected WebSocket client. Used for stock updates because all users viewing the app should see inventory changes.

Clients.User(userId.ToString()).SendAsync("event", data) — sends to ONE specific user identified by their NameIdentifier claim. Used in VerifyPickup to notify only the specific Receiver that their pickup is confirmed.

SignalR matches the userId string to the sub claim in the connected user's JWT to route the message correctly.

---

**Q14: What is the SignalR CORS issue and how was it solved?**

Problem: SignalR requires AllowCredentials() to pass JWT tokens over WebSocket. Browsers forbid combining AllowCredentials with the wildcard Access-Control-Allow-Origin: *. Using .AllowAnyOrigin() sets the wildcard, causing the browser to reject the connection.

Solution: .SetIsOriginAllowed(origin => true). Instead of returning *, the server echoes the exact incoming Origin header value (e.g., http://localhost:5173) back to the client. The browser sees a specific non-wildcard origin and allows credentials. This satisfies both the browser security policy and SignalR's requirements.

---

**Q15: What is IHubContext and why is it needed?**

Normally you send SignalR messages from inside a Hub class (which inherits Hub). But in EcoBite, messages need to be sent from controllers and background services — outside the Hub class.

IHubContext<DonationHub> is a server-side proxy to the hub. It exposes Clients.All, Clients.User(), etc. It is registered as a Singleton by AddSignalR() and can be injected anywhere via DI.

---

## CATEGORY 5: Hangfire

**Q16: What is Hangfire and what does it do in EcoBite?**

Hangfire is a background job processing library. It stores job definitions in SQL Server and has a background server thread that executes them.

In EcoBite, one recurring job is scheduled: DonationCleanupService.ReleaseUnclaimedDonations() runs every minute using Cron.Minutely.

This job finds all Claims with Status=Reserved AND PickupDeadline < Now, marks them as Expired, restores the stock to the donation, and broadcasts a SignalR event. This automated cleanup prevents inventory from being permanently locked by no-show receivers.

---

**Q17: Explain UseSimpleAssemblyNameTypeSerializer in detail.**

When Hangfire stores a job, it serializes the method reference as a JSON string including the .NET type name.

Default (without it): EcoBite.Services.DonationCleanupService, EcoBite, Version=1.0.0.0, Culture=neutral, PublicKeyToken=null.

With UseSimpleAssemblyNameTypeSerializer(): EcoBite.Services.DonationCleanupService, EcoBite.

The version, culture, and public key token are stripped. This matters in CI/CD: when version 2.0.0.0 is deployed, old jobs in the queue have "Version=1.0.0.0" in their type name. The deserializer cannot find a matching type → TypeLoadException → job fails permanently.

The simple serializer makes type resolution version-agnostic. A production best practice.

---

**Q18: Why does DonationCleanupService use IServiceScopeFactory instead of directly injecting DbContext?**

DbContext is registered as Scoped — one instance per HTTP request, disposed at request end.

Hangfire jobs run on a background thread with NO HTTP request context. If you inject DbContext directly into a Hangfire service (registered as Singleton or Transient), you would either get an exception (cannot consume scoped from singleton) or use a stale/disposed DbContext.

Solution: Inject IServiceScopeFactory (which is Singleton). Inside the job method, call _scopeFactory.CreateScope() to manually create a new DI scope, then resolve a fresh DbContext from that scope. The using block disposes both the scope and the DbContext when the method exits.

---

## CATEGORY 6: Spatial Data

**Q19: What is NetTopologySuite and how is it used?**

NetTopologySuite (NTS) is a .NET library for working with geographic data types and operations. It adds types like Point, LineString, Polygon etc.

In EcoBite:
- Point stores latitude/longitude as a single GEOGRAPHY column in SQL Server.
- SRID 4326 = the WGS84 coordinate system (same as GPS and Google Maps).
- IsWithinDistance(point, meters) translates to SQL Server's STDistance() spatial function.
- Distance(point) calculates meters between two points using the Earth's curvature.

This allows the database itself to do the radius calculation using spatial indexes, making the nearby query extremely fast.

---

**Q20: Why is Point created as new Point(longitude, latitude) not (latitude, longitude)?**

NetTopologySuite follows the mathematical X, Y convention:
- X = horizontal = East/West = Longitude
- Y = vertical = North/South = Latitude

This is the opposite of how humans say coordinates (lat, lng). Getting this wrong places points in the wrong ocean. The code correctly extracts Latitude = d.PickupLocation.Y and Longitude = d.PickupLocation.X in the Select projections.

---

**Q21: What caused the GeoJSON serialization crash and how was it fixed?**

Problem: System.Text.Json did not know how to serialize a NetTopologySuite Point object. It threw an assembly reference error.

Fix: Installed NetTopologySuite.IO.GeoJSON4STJ package and registered the GeoJsonConverterFactory in AddControllers().AddJsonOptions():
options.JsonSerializerOptions.Converters.Add(new GeoJsonConverterFactory())

This teaches the JSON serializer to convert Point to standard GeoJSON format: {"type":"Point","coordinates":[lng,lat]}. The React Leaflet map library natively understands GeoJSON.

---

## CATEGORY 7: Architecture & Design

**Q22: What is Dependency Injection and how does EcoBite use it?**

Dependency Injection (DI) is a design pattern where a class receives its dependencies from an external source instead of creating them itself.

In EcoBite:
- All services (DbContext, SignalR, Hangfire, JWT) are registered in Program.cs with builder.Services.Add*().
- Controllers declare their dependencies in the constructor. ASP.NET Core's DI container automatically provides the correct instances.
- Benefits: testability (swap real DB with mock), loose coupling, lifecycle management (Scoped, Singleton, Transient).

Example: ClaimsController constructor takes ApplicationDbContext and IHubContext<DonationHub>. Neither is created by the controller — both are injected by the DI container.

---

**Q23: What is the DTO pattern and why not expose entity classes directly?**

DTOs (Data Transfer Objects) are simple classes that define the API contract independently from the database model.

Reasons not to expose entities directly:
1. Security: User entity has PasswordHash. Exposing it via API is a security vulnerability.
2. Over-posting: Client could send DonorId in the request body and override the authenticated user's ID.
3. Shape mismatch: API needs Latitude/Longitude as separate doubles but DB stores them as a single Point.
4. Circular references: Entity navigation properties create infinite JSON loops.

DTOs solve all four problems by defining exactly what the API accepts and returns.

---

**Q24: What is the difference between AddControllers, AddMvc, and AddControllersWithViews?**

- AddControllers() — registers only Web API controllers (no Razor Views, no Pages). Used in EcoBite because it is a pure API project.
- AddControllersWithViews() — adds controllers PLUS Razor View support (for MVC web apps).
- AddMvc() — adds everything: controllers, views, Razor Pages.

EcoBite uses AddControllers() because there is no server-side HTML rendering — React handles all UI.

---

**Q25: Why is UseAuthentication called before UseAuthorization in the pipeline?**

UseAuthentication reads the Authorization: Bearer <token> header, validates the JWT, and populates HttpContext.User with the claims (userId, email, role).

UseAuthorization then reads HttpContext.User to check if the user has the required role for [Authorize(Roles = "Donor")].

If the order is reversed, UseAuthorization runs first with an empty HttpContext.User → every [Authorize] returns 401 even for valid tokens.

---

## CATEGORY 8: Real Challenges Solved

**Q26: What was the Enum serialization bug and how was it fixed?**

Bug: Backend serialized DonationStatus.Active as the integer 0 (default JSON enum behavior). React frontend was checking if (status === 'Active') — comparing against a string. The comparison always failed, breaking status-based UI logic.

Fix: Added options.JsonSerializerOptions.Converters.Add(new JsonStringEnumConverter()) in AddControllers().AddJsonOptions(). This makes the serializer convert enums to their string names globally. All enums in all controllers are now serialized as strings.

---

**Q27: What is a Database Transaction and why is it used in CreateClaim?**

A database transaction groups multiple SQL operations into an atomic unit — either ALL succeed or ALL are rolled back.

In CreateClaim:
- Operation 1: Decrement RemainingQuantity on Donation.
- Operation 2: Insert a new Claim row.

If Op 1 succeeds but Op 2 fails (e.g., network error), the inventory is permanently wrong — stock was decremented but no claim was created. The transaction ensures both operations succeed together or neither does.

BeginTransaction() → SaveChanges() → Commit(). If exception → Rollback().

---

**Q28: What is the ClaimCode (GUID) and how does the QR verification flow work?**

When a Receiver creates a claim, Guid.NewGuid() generates a 128-bit random unique identifier. This is stored in the Claims table and returned to the React frontend.

The React app generates a QR code image containing the GUID string and displays it to the Receiver.

The Donor uses the app's scanner (react-qr-reader) to scan the QR code, extracting the GUID string.

The app calls POST /api/Claims/verify with the GUID. The backend finds the matching claim, verifies the Donor owns the donation (security check), marks it PickedUp, creates an audit record in PickupVerifications, and sends a targeted SignalR notification to the Receiver.

---

**Q29: What security checks does VerifyPickup perform?**

Four checks in order:
1. Does the ClaimCode exist? If not → 404. Prevents invalid QR codes.
2. Does this donation belong to the scanning Donor? claim.Donation.DonorId != scannerUserId → 401. Prevents a Donor from scanning another Donor's QR codes.
3. Is the claim already PickedUp? → 400. Prevents double-scanning (replay attack).
4. Is the claim Expired or Cancelled? → 400. Prevents scanning voided claims.

---

**Q30: If you could improve EcoBite, what would you add?**

Good answers:
1. Push Notifications — integrate Firebase Cloud Messaging so receivers get a phone notification when their pickup is verified, even if the app is in the background (not just SignalR which requires an open connection).
2. Refresh Tokens — current JWT expires in 7 days with no revocation. Add a refresh token system with a stored token table.
3. Admin Dashboard — the Admin role exists in the enum but has no controller. Add an admin controller for moderation (removing fraudulent posts).
4. Rate Limiting — add ASP.NET Core rate limiting middleware to prevent spam claims.
5. Soft Delete — instead of hard-deleting donations, add an IsDeleted flag so history is preserved.
6. Unit Tests — add xUnit tests for ClaimsController with an in-memory EF Core database to test concurrency logic.
7. Redis Cache — cache nearby donation results for 30 seconds to reduce DB load during peak times.
