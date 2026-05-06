# EcoBite — Complete Technical Explanation (Part 1 of 3)
## Database Design, Models & Program.cs

---

## Project Folder Structure

```
EcoBite/ (Solution Root)
├── EcoBite.sln              ← Visual Studio solution file
├── README.md
├── ecobite-frontend/        ← React frontend (separate project)
└── EcoBite/                 ← Main .NET 8 Web API project
    ├── Program.cs            ← App entry point, all service registration
    ├── appsettings.json      ← DB connection string, JWT config
    ├── EcoBite.csproj        ← NuGet package references
    ├── Controllers/          ← HTTP request handlers (5 controllers)
    ├── Data/                 ← DbContext (EF Core bridge to SQL Server)
    ├── Hubs/                 ← SignalR hub (real-time WebSocket)
    ├── Migrations/           ← EF Core auto-generated SQL scripts
    ├── Models/
    │   ├── Entities/         ← 7 database table classes
    │   └── *.Dto.cs          ← Request/Response shapes (DTOs)
    └── Services/             ← Hangfire background job
```

---

## SECTION 1: Database Design & Normalization

### What is Normalization?

Normalization = organizing a DB to reduce redundancy and improve integrity.

EcoBite follows 3rd Normal Form (3NF):
- **1NF**: Every column holds one atomic value. No repeating groups.
- **2NF**: All non-key columns depend on the whole primary key.
- **3NF**: No non-key column depends on another non-key column.

---

### Table 1: Users

File: Models/Entities/User.cs

```csharp
public enum UserRole { Donor, Receiver, Admin }

public class User
{
    [Key]
    public int Id { get; set; }

    [Required, MaxLength(50)]
    public required string UserName { get; set; }

    [Required, MaxLength(100)]
    public required string Email { get; set; }

    [Required, MaxLength(255)]
    public required string PasswordHash { get; set; }

    [MaxLength(20)]
    public string? PhoneNumber { get; set; }

    [Required, Column(TypeName = "nvarchar(20)")]
    public required UserRole Role { get; set; }

    public Point? DefaultLocation { get; set; }

    public DateTime CreatedAt { get; set; }

    // Navigation Properties (not DB columns)
    public ICollection<Donation> Donations { get; set; } = new List<Donation>();
    public ICollection<Claim> Claims { get; set; } = new List<Claim>();
}
```

Field-by-field explanation:

| Field | SQL Type | Purpose |
|-------|----------|---------|
| Id | INT IDENTITY PK | Auto-incremented unique row ID |
| UserName | NVARCHAR(50) NOT NULL | Display name |
| Email | NVARCHAR(100) NOT NULL | Login credential, must be unique |
| PasswordHash | NVARCHAR(255) NOT NULL | BCrypt hash of the password |
| PhoneNumber | NVARCHAR(20) NULL | Optional contact number |
| Role | NVARCHAR(20) NOT NULL | "Donor", "Receiver", or "Admin" |
| DefaultLocation | GEOGRAPHY NULL | Spatial point — user's saved GPS location |
| CreatedAt | DATETIME2 | Account creation timestamp |

Why `[Column(TypeName = "nvarchar(20)")]` on Role?
By default EF Core stores enums as integers (0=Donor, 1=Receiver). This annotation overrides that and stores the string name "Donor" or "Receiver" directly in the DB — much more readable.

What are Navigation Properties?
`ICollection<Donation> Donations` is NOT a real column. It is EF Core's representation of a one-to-many relationship. EF will automatically load related data when you write `.Include(u => u.Donations)`. It generates a SQL JOIN behind the scenes.

---

### Table 2: Donations

File: Models/Entities/Donation.cs

```csharp
public enum DonationStatus { Active, Completed, Expired }

public class Donation
{
    [Key]
    public int Id { get; set; }

    public int DonorId { get; set; }           // FK → Users.Id
    public User Donor { get; set; }

    [Required, MaxLength(100)]
    public required string Title { get; set; }

    [MaxLength(500)]
    public string? Description { get; set; }

    public int OriginalQuantity { get; set; }
    public int RemainingQuantity { get; set; }

    [Required]
    public required Point PickupLocation { get; set; } // SQL GEOGRAPHY

    [MaxLength(200)]
    public string? AddressText { get; set; }

    public DateTime AvailableFrom { get; set; }
    public DateTime ExpiryDateTime { get; set; }

    [Required, Column(TypeName = "nvarchar(20)")]
    public required DonationStatus Status { get; set; }

    [Column(TypeName = "decimal(18,2)")]
    public decimal Price { get; set; }

    [Timestamp]
    public byte[] RowVersion { get; set; }     // Concurrency token

    public ICollection<Claim> Claims { get; set; } = new List<Claim>();
}
```

Critical Field — PickupLocation (Point):
- Uses NetTopologySuite. Stored as SQL Server GEOGRAPHY type.
- SRID 4326 = the GPS coordinate system (same as Google Maps).
- Enables spatial queries: "find donations within 5km of me."
- Created as: `new Point(longitude, latitude) { SRID = 4326 }`

Critical Field — RowVersion [Timestamp]:
- SQL Server auto-updates this 8-byte binary every time the row changes.
- When EF Core saves, it adds: `WHERE RowVersion = @original_value`
- If row was changed by someone else in between, no rows match → EF throws DbUpdateConcurrencyException.
- This prevents double-booking: two users cannot claim the last item simultaneously.

---

### Table 3: Claims

File: Models/Entities/Claim.cs

```csharp
public enum ClaimStatus { Reserved, PickedUp, Cancelled, Expired }

public class Claim
{
    [Key]
    public int Id { get; set; }

    public int DonationId { get; set; }
    public required Donation Donation { get; set; }

    public int? ReceiverId { get; set; }        // Nullable to avoid cascade loops
    public User? Receiver { get; set; }

    public int QuantityClaimed { get; set; }

    public Guid ClaimCode { get; set; }          // The QR code value

    [Required, Column(TypeName = "nvarchar(20)")]
    public required ClaimStatus Status { get; set; }

    public DateTime ClaimedAt { get; set; }
    public DateTime? PickedUpAt { get; set; }    // Null until verified
    public DateTime PickupDeadline { get; set; } // ClaimedAt + 30 minutes

    public ICollection<PickupVerification> Verifications { get; set; }
}
```

ClaimCode (Guid) — The QR Code:
- Generated by `Guid.NewGuid()` — a 128-bit globally unique identifier.
- Stored as SQL UNIQUEIDENTIFIER.
- Near-zero collision probability.
- This value is embedded in the QR code displayed to the Receiver.
- The Donor scans it and sends it to POST /api/Claims/verify.

Why is ReceiverId nullable (int?)?
EF Core would normally set up a CASCADE DELETE from Users to Claims. But there is already a cascade path from Users → Donations → Claims. SQL Server does not allow two cascade paths to the same table (multi-path cascade). Making ReceiverId nullable tells EF to use NO ACTION instead of CASCADE, breaking the cycle.

---

### Table 4: PickupVerifications

File: Models/Entities/PickupVerification.cs

```csharp
public class PickupVerification
{
    [Key]
    public int Id { get; set; }

    public int? ClaimId { get; set; }
    public Claim? Claim { get; set; }

    public int? VerifiedByUserId { get; set; }
    public User? VerifiedByUser { get; set; }

    public DateTime ScannedAt { get; set; }
    public bool IsSuccessful { get; set; }

    [MaxLength(255)]
    public string? FailureReason { get; set; }
}
```

Purpose: Pure audit trail. Every QR scan creates a record whether it succeeds or fails. This detects fraud (someone scanning an already-used code). Separating this into its own table follows 3NF — verification data belongs to the verification event, not to the claim itself.

---

### Table 5: Reviews

File: Models/Entities/Review.cs

```csharp
public class Review
{
    [Key] public int Id { get; set; }

    public int ClaimId { get; set; }
    public required Claim Claim { get; set; }

    public int? ReviewerId { get; set; }        // The Receiver writing the review
    [ForeignKey("ReviewerId")]
    public User? Reviewer { get; set; }

    public int? TargetUserId { get; set; }      // The Donor being reviewed
    [ForeignKey("TargetUserId")]
    public User? TargetUser { get; set; }

    [Range(1, 5)]
    public int Rating { get; set; }

    [MaxLength(500)]
    public string? Comment { get; set; }

    public DateTime CreatedAt { get; set; }
}
```

Two FKs to the same Users table requires both to be nullable — same multi-path cascade reason as Claims.

`[ForeignKey("ReviewerId")]` — explicit annotation needed because EF cannot auto-map two FK properties pointing to Users without help.

---

### Table 6: Messages

```csharp
public class Message
{
    [Key] public int Id { get; set; }
    public int? SenderId { get; set; }
    [ForeignKey("SenderId")]
    public User? Sender { get; set; }

    public int? ReceiverId { get; set; }
    [ForeignKey("ReceiverId")]
    public User? Receiver { get; set; }

    [Required]
    public required string Content { get; set; }
    public DateTime SentAt { get; set; }
    public bool IsRead { get; set; }
}
```

Same cascade-loop avoidance pattern. IsRead allows "unread messages" badge feature on the frontend.

---

### Table 7: Notifications

```csharp
public class Notification
{
    [Key] public int Id { get; set; }
    public int UserId { get; set; }
    public User User { get; set; }

    [Required, MaxLength(100)]
    public required string Title { get; set; }

    [Required, MaxLength(255)]
    public required string Message { get; set; }

    public bool IsRead { get; set; }

    [MaxLength(50)]
    public string? Type { get; set; }      // e.g. "ClaimExpired", "PickupVerified"

    public DateTime CreatedAt { get; set; }
}
```

---

### Database Relationships (ERD)

```
Users 1 ──────────────< Donations       (One Donor → Many Donations)
Users 1 ──────────────< Claims          (One Receiver → Many Claims)
Donations 1 ──────────< Claims          (One Donation → Many Claims)
Claims 1 ─────────────< PickupVerifications
Claims 1 ─────────────< Reviews (1 per claim)
Users 1 ──────────────< Messages (as Sender)
Users 1 ──────────────< Messages (as Receiver)
Users 1 ──────────────< Notifications
```

---

## SECTION 2: ApplicationDbContext

File: Data/ApplicationDbContext.cs

```csharp
public class ApplicationDbContext : DbContext
{
    public ApplicationDbContext(DbContextOptions options) : base(options) { }

    public DbSet<User> Users { get; set; }
    public DbSet<Donation> Donations { get; set; }
    public DbSet<Claim> Claims { get; set; }
    public DbSet<PickupVerification> PickupVerifications { get; set; }
    public DbSet<Review> Reviews { get; set; }
    public DbSet<Notification> Notifications { get; set; }
    public DbSet<Message> Messages { get; set; }
}
```

What is DbContext?
It is EF Core's implementation of the Unit of Work + Repository patterns combined:
1. Represents one session/connection with the database.
2. Tracks all in-memory changes to entity objects (Change Tracker).
3. Translates LINQ queries → SQL via the SQL Server provider.
4. Batches and sends all changes when SaveChanges() is called.

What is DbSet<T>?
Each DbSet<T> = one database table:
- `_context.Users.ToList()` → SELECT * FROM Users
- `_context.Users.Add(user)` → queues INSERT INTO Users
- `_context.Users.Find(1)` → SELECT * FROM Users WHERE Id = 1
- `_context.Users.Where(u => u.Role == "Donor")` → SELECT * FROM Users WHERE Role = 'Donor'

---

## SECTION 3: Program.cs Line-by-Line

```csharp
var builder = WebApplication.CreateBuilder(args);
```
Creates the application host builder. Sets up Configuration (reads appsettings.json), Logging, and the Dependency Injection (DI) container.

---

### 3.1 Controllers + JSON Options

```csharp
builder.Services.AddControllers().AddJsonOptions(options =>
{
    options.JsonSerializerOptions.ReferenceHandler = ReferenceHandler.IgnoreCycles;
    options.JsonSerializerOptions.Converters.Add(
        new NetTopologySuite.IO.Converters.GeoJsonConverterFactory());
    options.JsonSerializerOptions.Converters.Add(new JsonStringEnumConverter());
});
```

AddControllers() — Scans the assembly for [ApiController] classes, registers the MVC pipeline for handling HTTP requests.

ReferenceHandler.IgnoreCycles — EF navigation properties create circular object graphs:
User.Donations → Donation.Donor → User.Donations → ... (infinite)
This setting stops the serializer when it encounters a second visit to the same object, writing null instead of looping forever.

GeoJsonConverterFactory — System.Text.Json cannot serialize NetTopologySuite's Point type by default. This factory registers a converter that turns `Point(lng, lat)` into standard GeoJSON format: `{"type":"Point","coordinates":[lng,lat]}` so the React Leaflet map can read it.

JsonStringEnumConverter — Without this, DonationStatus.Active serializes as 0, ClaimStatus.Reserved as 0. With this, they serialize as "Active" and "Reserved". The frontend was doing `if (status === 'Active')` so string conversion was essential.

---

### 3.2 Swagger

```csharp
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();
```

Registers Swagger/OpenAPI. In Development, navigating to /swagger shows an interactive HTML page listing all endpoints with request/response schemas. Great for manual testing without a frontend.

---

### 3.3 Database (EF Core + Spatial)

```csharp
builder.Services.AddDbContext<ApplicationDbContext>(options =>
    options.UseSqlServer(
        builder.Configuration.GetConnectionString("DefaultConnection"),
        x => x.UseNetTopologySuite()
    ));
```

AddDbContext<ApplicationDbContext> — Registers ApplicationDbContext as a Scoped service.
Scoped = one instance is created per HTTP request, then disposed. This is EF Core's recommended lifetime because the DbContext is not thread-safe.

GetConnectionString("DefaultConnection") — Reads from appsettings.json:
```
Server=LAPTOP-224LOGIF\SQLEXPRESS;Database=EcoBiteDBTest;
Trusted_Connection=True;TrustServerCertificate=true;
```
- Trusted_Connection=True → Windows Authentication (no SQL username/password)
- TrustServerCertificate=true → Accepts dev SSL certificate

x.UseNetTopologySuite() — Enables spatial support. Without this, EF Core cannot translate Point objects to SQL Server GEOGRAPHY columns. This extension also enables IsWithinDistance() and Distance() to become proper SQL Server spatial function calls.

---

### 3.4 SignalR

```csharp
builder.Services.AddSignalR();
```

Registers the SignalR real-time messaging service. SignalR uses WebSockets as the primary transport. If WebSockets are unavailable it falls back to Server-Sent Events, then Long Polling. Clients maintain a persistent connection to the server. When the server calls Clients.All.SendAsync("EventName", data), all connected clients receive the message instantly.

---

### 3.5 Hangfire — DEEP DIVE

```csharp
builder.Services.AddHangfire(configuration => configuration
    .UseSimpleAssemblyNameTypeSerializer()
    .UseRecommendedSerializerSettings()
    .UseSqlServerStorage(builder.Configuration.GetConnectionString("DefaultConnection")));

builder.Services.AddHangfireServer();
```

What is Hangfire?
A background job processing library. It persists job definitions in the database and has a background server thread that picks them up and executes them — even if the web server restarts (jobs are not lost, they live in the DB).

EcoBite uses it for one recurring job: `ReleaseUnclaimedDonations()` runs every minute.

AddHangfire(configuration => ...) — Configures Hangfire globally using a fluent builder. Each method modifies the same configuration object.

--- UseSimpleAssemblyNameTypeSerializer() ---

When Hangfire stores a job in the HangFire.Job SQL table, it serializes the job definition to JSON. This includes the full .NET type name of the class and method to call.

Without this setting (default):
```json
{
  "Type": "EcoBite.Services.DonationCleanupService, EcoBite, Version=1.0.0.0, Culture=neutral, PublicKeyToken=null",
  "Method": "ReleaseUnclaimedDonations"
}
```

With UseSimpleAssemblyNameTypeSerializer():
```json
{
  "Type": "EcoBite.Services.DonationCleanupService, EcoBite",
  "Method": "ReleaseUnclaimedDonations"
}
```

The version, culture, and public key token are stripped.

WHY THIS MATTERS:
If you publish a new version of the app (Version=2.0.0.0), Hangfire tries to deserialize old jobs still sitting in the queue. The full name no longer matches → TypeLoadException → job fails.
With the simple serializer, version changes are transparent. The job always resolves correctly.
This is a best practice especially in CI/CD pipelines and production deployments.

--- UseRecommendedSerializerSettings() ---

Applies safe JSON settings for Hangfire's internal Newtonsoft.Json serializer:
- TypeNameHandling.Auto: include type info only when needed for polymorphism
- NullValueHandling.Ignore: skip null properties to keep JSON compact
- DefaultValueHandling.IgnoreAndPopulate: skip defaults on serialize, populate on deserialize
- PreserveReferencesHandling.None: no circular reference tracking (avoids bloat)

This makes the serialized job arguments stored in the DB compact and safe from serialization crashes.

--- UseSqlServerStorage(connectionString) ---

Directs Hangfire to use SQL Server as its job persistence backend. On first run, Hangfire auto-creates these tables in your database:
- HangFire.Job — each background job (its type, method, args, state)
- HangFire.State — history of state transitions per job (Enqueued → Processing → Succeeded)
- HangFire.Server — list of active Hangfire server instances
- HangFire.Set, HangFire.Hash, HangFire.List — internal queue management

--- AddHangfireServer() ---

Starts the in-process background worker. This is a separate thread inside your app process that:
1. Polls HangFire.Job every few seconds.
2. Picks up due jobs.
3. Executes them using the DI container.
4. Updates their state (Succeeded/Failed).

Without this line, Hangfire would store jobs but never run them.

---

### 3.6 JWT Authentication

```csharp
var jwtKey = builder.Configuration["Jwt:Key"]
    ?? throw new InvalidOperationException("JWT Key is missing");
var key = Encoding.ASCII.GetBytes(jwtKey);

builder.Services.AddAuthentication(options =>
{
    options.DefaultAuthenticateScheme = JwtBearerDefaults.AuthenticationScheme;
    options.DefaultChallengeScheme = JwtBearerDefaults.AuthenticationScheme;
})
.AddJwtBearer(options =>
{
    options.RequireHttpsMetadata = false;
    options.SaveToken = true;
    options.TokenValidationParameters = new TokenValidationParameters
    {
        ValidateIssuerSigningKey = true,
        IssuerSigningKey = new SymmetricSecurityKey(key),
        ValidateIssuer = true,
        ValidateAudience = true,
        ValidIssuer = builder.Configuration["Jwt:Issuer"],
        ValidAudience = builder.Configuration["Jwt:Audience"]
    };
});
```

What is a JWT?
A JSON Web Token has 3 Base64-encoded parts separated by dots:
Header.Payload.Signature

- Header: `{"alg":"HS256","typ":"JWT"}`
- Payload: `{"sub":"42","email":"user@x.com","role":"Donor","exp":...}`
- Signature: HMAC_SHA256(base64(header) + "." + base64(payload), secret_key)

Nobody can forge the token without the secret key because the signature would not match.

DefaultAuthenticateScheme — tells ASP.NET: when [Authorize] is encountered, use JWT Bearer to identify the user.

ValidateIssuerSigningKey = true — the server recomputes the signature and compares it. Rejects tampered tokens.

SymmetricSecurityKey(key) — same key signs the token on login and verifies it on every request.

ValidateIssuer + ValidAudience — ensures the token was created by "EcoBiteApp" for "EcoBiteUsers". Prevents tokens from other systems being used here.

RequireHttpsMetadata = false — allows HTTP during local development.

SaveToken = true — saves the raw JWT string in HttpContext for later retrieval if needed.

---

### 3.7 CORS

```csharp
builder.Services.AddCors(options =>
{
    options.AddPolicy("AllowReactApp", policy =>
    {
        policy.SetIsOriginAllowed(origin => true)
              .AllowAnyHeader()
              .AllowAnyMethod()
              .AllowCredentials();
    });
});
```

CORS = Cross-Origin Resource Sharing. A browser security rule: script from http://localhost:5173 cannot call https://localhost:7162 (different origin = different port) unless the server explicitly allows it.

The Conflict:
SignalR requires AllowCredentials() so it can pass the JWT token via WebSocket.
But browsers forbid Access-Control-Allow-Origin: * when credentials are allowed.
AllowAnyOrigin() sets the * header → browser blocks it.

The Solution — SetIsOriginAllowed(origin => true):
Instead of returning *, the server reads the incoming request's Origin header (e.g., http://localhost:5173) and echoes it back exactly in the response: Access-Control-Allow-Origin: http://localhost:5173.
The browser sees a specific origin (not *) → allows credentials. Problem solved.

---

### 3.8 Middleware Pipeline (ORDER IS CRITICAL)

```csharp
var app = builder.Build();

if (app.Environment.IsDevelopment()) { app.UseSwagger(); app.UseSwaggerUI(); }

app.UseHttpsRedirection();    // Redirect HTTP → HTTPS
app.UseAuthentication();      // Step 1: WHO are you? (reads + validates JWT)
app.UseAuthorization();       // Step 2: Are you ALLOWED? (checks [Authorize] roles)
app.UseHangfireDashboard();   // Exposes /hangfire monitoring UI
app.MapHub<DonationHub>("/donationHub");  // WebSocket endpoint

RecurringJob.AddOrUpdate<DonationCleanupService>(
    "release-unclaimed-food",
    service => service.ReleaseUnclaimedDonations(),
    Cron.Minutely);

app.UseCors("AllowReactApp"); // Must be before MapControllers
app.MapControllers();
app.Run();
```

Order rules:
1. Authentication BEFORE Authorization — Auth must first parse the JWT and populate HttpContext.User before Authorization can check roles.
2. UseCors BEFORE MapControllers — CORS headers must be added to the response before the controller writes its response body.
3. UseHangfireDashboard AFTER Build — pipeline middleware runs on app (built instance), not builder.

RecurringJob.AddOrUpdate:
- "release-unclaimed-food" — unique Hangfire job ID
- service => service.ReleaseUnclaimedDonations() — the method expression stored as JSON in DB
- Cron.Minutely — standard cron: runs every 60 seconds

MapHub<DonationHub>("/donationHub"):
React connects to wss://localhost:7162/donationHub. Hangfire itself manages the WebSocket handshake upgrade from HTTP.

---

## SECTION 4: NuGet Packages

| Package | Purpose |
|---------|---------|
| BCrypt.Net-Next | Slow password hashing (brute-force resistant) |
| Hangfire + Hangfire.SqlServer | Background jobs + SQL persistence |
| Microsoft.AspNetCore.Authentication.JwtBearer | JWT auth middleware |
| Microsoft.EntityFrameworkCore.SqlServer | EF Core SQL Server driver |
| Microsoft.EntityFrameworkCore.SqlServer.NetTopologySuite | Spatial data (GEOGRAPHY) via EF |
| Microsoft.EntityFrameworkCore.Design + Tools | dotnet ef migrations CLI |
| Microsoft.IdentityModel.Tokens | JWT token model classes |
| NetTopologySuite.IO.GeoJSON4STJ | Point → GeoJSON for System.Text.Json |
| Swashbuckle.AspNetCore | Swagger UI |
| System.IdentityModel.Tokens.Jwt | JWT creation and parsing |

BCrypt detail: BCrypt.HashPassword("abc123") produces "$2a$11$..." (60 chars). The $11$ means "cost factor 11" = 2^11 = 2048 hash rounds. Each round doubles the time. This is intentionally slow to defeat brute-force. BCrypt.Verify("abc123", hash) returns true/false without needing to store the original password.

---
Continue reading Part 2 for Controllers, Services, Hubs, and DTOs.
Continue reading Part 3 for Interview Questions and Answers.
