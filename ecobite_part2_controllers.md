# EcoBite — Part 2 of 3: Controllers, Services, Hubs & DTOs

---

## SECTION 5: DTOs (Data Transfer Objects)

DTOs are simple classes that define the SHAPE of incoming request bodies. They decouple the API contract from the internal domain model.

### Why DTOs?
- Security: Never expose PasswordHash, RowVersion, or internal IDs from entity classes directly.
- Validation: Data annotations on DTOs validate user input before it touches the database.
- Flexibility: The API shape can change without changing the database model.

### UserRegisterDto
```csharp
public class UserRegisterDto
{
    [Required] public string Username { get; set; } = string.Empty;
    [Required, EmailAddress] public string Email { get; set; } = string.Empty;
    [Required, MinLength(6)] public string Password { get; set; } = string.Empty;
    [Required] public string Role { get; set; } = "Receiver";
}
```
- `[EmailAddress]` — validates the format is a valid email (contains @, domain, etc.)
- `[MinLength(6)]` — password must be at least 6 characters
- Role is a string because the client sends "Donor" or "Receiver" as text; the controller parses it to the enum.

### CreateDonationDto
```csharp
public class CreateDonationDto
{
    [Required] public string Title { get; set; } = string.Empty;
    public string? Description { get; set; }
    [Required] public int Quantity { get; set; }
    [Required] public double Latitude { get; set; }
    [Required] public double Longitude { get; set; }
    public string? AddressText { get; set; }
    public decimal? Price { get; set; } = 0;
    [Required] public DateTime ExpiryDateTime { get; set; }
}
```
The client sends Latitude + Longitude as separate doubles. The controller combines them into a NetTopologySuite Point object before saving to DB.

### CreateClaimDto
```csharp
public class CreateClaimDto
{
    [Required] public int DonationId { get; set; }
    [Required]
    [Range(1, 20, ErrorMessage = "You can only claim between 1 and 20 items.")]
    public int Quantity { get; set; }
}
```
`[Range(1, 20)]` — prevents abuse (claiming 0 or 10,000 items). Validation runs automatically because of [ApiController].

### VerifyPickupDto
```csharp
public class VerifyPickupDto
{
    [Required] public Guid ClaimCode { get; set; }
}
```
Just the GUID from the QR code. Simple but powerful.

---

## SECTION 6: Controllers

Controllers are classes that handle HTTP requests. In ASP.NET Core Web API:
- `[Route("api/[controller]")]` — sets the base URL (e.g., `api/Auth`, `api/Donation`)
- `[ApiController]` — enables automatic model validation, binding, and error responses
- Each public method with `[HttpGet]`/`[HttpPost]` etc. becomes an API endpoint

---

### AuthController (api/Auth)

File: Controllers/AuthController.cs

```csharp
[Route("api/[controller]")]
[ApiController]
public class AuthController : ControllerBase
{
    private readonly ApplicationDbContext _context;
    private readonly IConfiguration _configuration;

    public AuthController(ApplicationDbContext context, IConfiguration configuration)
    {
        _context = context;
        _configuration = configuration;
    }
```

Constructor Injection: ASP.NET Core's DI container automatically provides `ApplicationDbContext` and `IConfiguration`. The controller does not create them — it receives them. This is Dependency Injection.

#### POST /api/Auth/register

```csharp
[HttpPost("register")]
public IActionResult Register(UserRegisterDto userRegisterDto)
{
    // 1. Check if email already exists
    if (_context.Users.Any(u => u.Email == userRegisterDto.Email))
        return BadRequest("User with this email already exists.");

    // 2. Parse the role string to enum
    if (!Enum.TryParse<UserRole>(userRegisterDto.Role, true, out var role))
        return BadRequest("Invalid Role. Use 'Donor' or 'Receiver'.");

    // 3. Hash the password
    var user = new User
    {
        UserName = userRegisterDto.Username,
        Email = userRegisterDto.Email,
        PasswordHash = BCrypt.Net.BCrypt.HashPassword(userRegisterDto.Password),
        Role = role,
        CreatedAt = DateTime.UtcNow,
    };

    _context.Users.Add(user);
    _context.SaveChanges();
    return Ok("User registered successfully.");
}
```

Step-by-step:
1. `_context.Users.Any(...)` → `SELECT TOP 1 1 FROM Users WHERE Email = @email` — checks uniqueness.
2. `Enum.TryParse<UserRole>("Donor", true, out var role)` — the `true` parameter means case-insensitive. Returns false if "xyz" is passed.
3. `BCrypt.Net.BCrypt.HashPassword(password)` — converts "abc123" into "$2a$11$..." (60-char hash). Never stores plain text.
4. `SaveChanges()` → executes `INSERT INTO Users (...)`.

#### POST /api/Auth/login

```csharp
[HttpPost("login")]
public IActionResult Login(UserLoginDto userLoginDto)
{
    var user = _context.Users.FirstOrDefault(u => u.Email == userLoginDto.Email);

    if (user == null || !BCrypt.Net.BCrypt.Verify(userLoginDto.Password, user.PasswordHash))
        return Unauthorized("Invalid email or password.");

    var token = GenerateJwtToken(user);
    return Ok(new { token, userId = user.Id, role = user.Role.ToString() });
}
```

`BCrypt.Verify` — takes the plain text password and the stored hash, re-hashes the plain text with the same salt, and compares. Returns true/false. The same error message is returned whether email is wrong OR password is wrong — this prevents enumeration attacks.

#### GenerateJwtToken (private method)

```csharp
private string GenerateJwtToken(User user)
{
    var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_configuration["Jwt:Key"]!));
    var creds = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);

    var claims = new[]
    {
        new Claim(JwtRegisteredClaimNames.Sub, user.Id.ToString()),  // User ID
        new Claim(JwtRegisteredClaimNames.Email, user.Email),
        new Claim(ClaimTypes.Role, user.Role.ToString())             // "Donor" or "Receiver"
    };

    var token = new JwtSecurityToken(
        issuer: _configuration["Jwt:Issuer"],
        audience: _configuration["Jwt:Audience"],
        claims: claims,
        expires: DateTime.Now.AddDays(7),
        signingCredentials: creds
    );

    return new JwtSecurityTokenHandler().WriteToken(token);
}
```

Key points:
- `JwtRegisteredClaimNames.Sub` = "sub" = the standard JWT subject claim. This is where the userId lives.
- `ClaimTypes.Role` = "http://schemas.microsoft.com/ws/2008/06/identity/claims/role" — the long Microsoft URI. ASP.NET Core reads this to enforce `[Authorize(Roles = "Donor")]`.
- Token expires in 7 days — after that the client must login again.
- `HmacSha256` — HMAC with SHA-256 produces a 256-bit signature.

---

### DonationController (api/Donation)

File: Controllers/DonationController.cs

#### POST / — Create Donation (Donor only)

```csharp
[HttpPost]
[Authorize(Roles = "Donor")]
public IActionResult CreateDonation(CreateDonationDto dto)
{
    var userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)?.Value ?? "0");
    var location = new Point(dto.Longitude, dto.Latitude) { SRID = 4326 };

    var donation = new Donation
    {
        Title = dto.Title,
        OriginalQuantity = dto.Quantity,
        RemainingQuantity = dto.Quantity,  // Starts equal to OriginalQuantity
        PickupLocation = location,
        ExpiryDateTime = dto.ExpiryDateTime,
        Status = DonationStatus.Active,
        AvailableFrom = DateTime.UtcNow,
        DonorId = userId,
    };

    _context.Donations.Add(donation);
    _context.SaveChanges();
    return Ok(new { message = "Donation posted successfully", donationId = donation.Id });
}
```

`User.FindFirst(ClaimTypes.NameIdentifier)` — reads the "sub" claim from the JWT. This is how the controller knows WHICH user is making the request without hitting the database for user lookup.

`new Point(dto.Longitude, dto.Latitude) { SRID = 4326 }` — NOTE: NetTopologySuite takes longitude FIRST, then latitude (X=lng, Y=lat). SRID 4326 = WGS84 GPS standard.

`[Authorize(Roles = "Donor")]` — ASP.NET Core reads the `ClaimTypes.Role` from the JWT. If the role is "Receiver", it returns 403 Forbidden automatically.

#### GET /nearby — Spatial Search

```csharp
[HttpGet("nearby")]
public IActionResult GetNearbyDonations(double lat, double lng, double radiusKm = 5)
{
    var userLocation = new Point(lng, lat) { SRID = 4326 };
    double radiusMeters = radiusKm * 1000;

    var nearbyDonations = _context.Donations
        .Where(d => d.Status == DonationStatus.Active &&
                    d.RemainingQuantity > 0 &&
                    d.ExpiryDateTime > DateTime.UtcNow &&
                    d.PickupLocation.IsWithinDistance(userLocation, radiusMeters))
        .OrderBy(d => d.PickupLocation.Distance(userLocation))
        .Select(d => new
        {
            d.Id, d.Title, d.RemainingQuantity,
            Latitude = d.PickupLocation.Y,
            Longitude = d.PickupLocation.X,
            DistanceMeters = d.PickupLocation.Distance(userLocation)
        })
        .ToList();

    return Ok(nearbyDonations);
}
```

`IsWithinDistance(userLocation, radiusMeters)` — EF Core translates this to SQL Server's `geography::STDistance()` function. The distance calculation happens IN THE DATABASE using SQL Server's spatial index — very fast even with millions of rows.

`Distance(userLocation)` in OrderBy — sorts results from closest to farthest. Also translated to `STDistance()` in SQL.

`d.PickupLocation.Y` = Latitude (because Y-axis = North/South), `.X` = Longitude. Extracted manually because the JSON serializer would output the full GeoJSON object otherwise.

#### GET /my-donations — Donor's own listings

Filters by `DonorId == userId` from the JWT, orders by newest first. Projects to an anonymous object to avoid circular reference issues with navigation properties.

---

### ClaimsController (api/Claims)

File: Controllers/ClaimsController.cs — THE MOST COMPLEX CONTROLLER

```csharp
public class ClaimsController : ControllerBase
{
    private readonly ApplicationDbContext _context;
    private readonly IHubContext<DonationHub> _hubContext;  // SignalR

    public ClaimsController(ApplicationDbContext context, IHubContext<DonationHub> hubContext)
    {
        _context = context;
        _hubContext = hubContext;
    }
```

`IHubContext<DonationHub>` — a server-side interface to the SignalR hub. Injected via DI. Allows calling `Clients.All.SendAsync(...)` from outside the hub class (e.g., from a controller or service).

#### POST / — Create Claim (Receiver only)

```csharp
[HttpPost]
[Authorize(Roles = "Receiver")]
public IActionResult CreateClaim(CreateClaimDto dto)
{
    var userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)?.Value ?? "0");

    // 1. Begin database transaction
    using var transaction = _context.Database.BeginTransaction();

    try
    {
        // 2. Load donation (EF tracks RowVersion automatically)
        var donation = _context.Donations.Find(dto.DonationId);

        if (donation == null) return NotFound("Donation not found.");
        if (donation.Status != DonationStatus.Active) return BadRequest("No longer active.");
        if (donation.ExpiryDateTime < DateTime.UtcNow) return BadRequest("Expired.");
        if (donation.RemainingQuantity < dto.Quantity)
            return BadRequest($"Only {donation.RemainingQuantity} left.");

        // 3. Decrement stock
        donation.RemainingQuantity -= dto.Quantity;
        if (donation.RemainingQuantity == 0)
            donation.Status = DonationStatus.Completed;

        // 4. Create claim record
        var claim = new Claim
        {
            DonationId = donation.Id,
            Donation = donation,
            ReceiverId = userId,
            Receiver = _context.Users.Find(userId)!,
            QuantityClaimed = dto.Quantity,
            Status = ClaimStatus.Reserved,
            ClaimedAt = DateTime.UtcNow,
            PickupDeadline = DateTime.UtcNow.AddMinutes(30),
            ClaimCode = Guid.NewGuid()   // QR code value
        };

        _context.Claims.Add(claim);

        // 5. SaveChanges — concurrency check happens HERE
        _context.SaveChanges();

        // 6. Commit transaction
        transaction.Commit();

        // 7. Broadcast real-time stock update via SignalR
        _hubContext.Clients.All.SendAsync("ReceiveStockUpdate", new
        {
            DonationId = donation.Id,
            NewQuantity = donation.RemainingQuantity,
            Status = donation.Status.ToString()
        }).Wait();

        return Ok(new
        {
            message = "Claim successful! Pickup within 30 minutes.",
            claimCode = claim.ClaimCode,
            pickupDeadline = claim.PickupDeadline
        });
    }
    catch (DbUpdateConcurrencyException)
    {
        // This fires if two users claimed at the exact same millisecond
        transaction.Rollback();
        return Conflict(new { message = "Someone else just claimed this item. Please try again." });
    }
    catch (Exception ex)
    {
        transaction.Rollback();
        return StatusCode(500, $"Internal Server Error: {ex.Message}");
    }
}
```

How the transaction works:
- `BeginTransaction()` — starts an explicit SQL transaction. All changes are held in a pending state.
- `SaveChanges()` — sends SQL to the DB but inside the transaction (not yet committed).
- `Commit()` — makes all changes permanent.
- `Rollback()` — undoes everything if something fails.

How concurrency protection works:
1. `_context.Donations.Find(dto.DonationId)` — EF loads the row AND captures the current RowVersion value in the change tracker.
2. When `SaveChanges()` runs, EF generates: `UPDATE Donations SET RemainingQuantity=@new WHERE Id=@id AND RowVersion=@captured_value`
3. If another request already changed the row (RowVersion changed), 0 rows are affected → EF throws `DbUpdateConcurrencyException`.
4. Caught in the catch block → Rollback → return 409 Conflict.

SignalR broadcast after commit:
- `_hubContext.Clients.All.SendAsync("ReceiveStockUpdate", data)` — pushes data to ALL connected WebSocket clients.
- `.Wait()` — synchronously waits for the async broadcast to complete (inside a non-async method).
- React clients listening for "ReceiveStockUpdate" instantly update the displayed quantity.

#### POST /verify — Verify QR Code (Donor only)

```csharp
[HttpPost("verify")]
[Authorize(Roles = "Donor")]
public IActionResult VerifyPickup(VerifyPickupDto dto)
{
    var scannerUserId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)?.Value ?? "0");

    // Find claim by GUID
    var claim = _context.Claims
        .Include(c => c.Donation)
        .FirstOrDefault(c => c.ClaimCode == dto.ClaimCode);

    if (claim == null) return NotFound("Invalid QR Code.");

    // Security: verify the scanner owns this donation
    if (claim.Donation.DonorId != scannerUserId)
        return Unauthorized("You cannot verify a claim for a donation that is not yours.");

    // State checks
    if (claim.Status == ClaimStatus.PickedUp)
        return BadRequest("This item has already been picked up.");
    if (claim.Status == ClaimStatus.Expired || claim.Status == ClaimStatus.Cancelled)
        return BadRequest("This claim has expired or was cancelled.");

    // Mark as picked up
    claim.Status = ClaimStatus.PickedUp;
    claim.PickedUpAt = DateTime.UtcNow;

    // Create audit record
    var verification = new PickupVerification
    {
        ClaimId = claim.Id,
        VerifiedByUserId = scannerUserId,
        ScannedAt = DateTime.UtcNow,
        IsSuccessful = true
    };

    _context.PickupVerifications.Add(verification);
    _context.SaveChanges();

    // Notify specific receiver via SignalR
    _hubContext.Clients.User(claim.ReceiverId.ToString())
        .SendAsync("ClaimStatusUpdated", claim.Id, "PickedUp")
        .Wait();

    return Ok(new { message = "Pickup Verified!", item = claim.Donation.Title });
}
```

`_hubContext.Clients.User(userId)` — sends to ONE specific connected user (the Receiver), not all. SignalR identifies users via the NameIdentifier claim from their JWT token. This gives the Receiver an instant phone notification that their pickup is confirmed.

---

### MessagesController (api/Messages)

File: Controllers/MessagesController.cs

`[Authorize]` at class level — ALL endpoints in this controller require authentication.

#### POST /send

```csharp
[HttpPost("send")]
public IActionResult SendMessage(SendMessageDto dto)
{
    var senderId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)?.Value ?? "0");

    if (!_context.Users.Any(u => u.Id == dto.ReceiverId)) return NotFound("Receiver not found.");
    if (senderId == dto.ReceiverId) return BadRequest("You cannot send messages to yourself.");

    var message = new Message
    {
        SenderId = senderId,
        ReceiverId = dto.ReceiverId,
        Content = dto.Content,
        SentAt = DateTime.UtcNow,
        IsRead = false
    };

    _context.Messages.Add(message);
    _context.SaveChanges();

    // Real-time push to specific receiver
    _hubContext.Clients.User(dto.ReceiverId.ToString())
        .SendAsync("ReceiveMessage", new { SenderId = senderId, Content = dto.Content, SentAt = message.SentAt })
        .Wait();

    return Ok(new { message = "Message sent successfully!" });
}
```

The message is saved to DB first (permanent), then pushed via SignalR (real-time). If the receiver is offline, the DB record ensures they see it when they reconnect.

#### GET /history/{otherUserId}

```csharp
var messages = _context.Messages
    .Where(m => (m.SenderId == myId && m.ReceiverId == otherUserId) ||
                (m.SenderId == otherUserId && m.ReceiverId == myId))
    .OrderBy(m => m.SentAt)
    .Select(m => new MessageResponseDto { ... })
    .ToList();
```

The WHERE clause retrieves messages in BOTH directions between the two users. The OR condition is what makes this a two-way conversation history lookup.

#### GET /contacts

```csharp
var contactIds = _context.Messages
    .Where(m => m.SenderId == myId || m.ReceiverId == myId)
    .Select(m => m.SenderId == myId ? m.ReceiverId : m.SenderId)
    .Distinct()
    .ToList();
```

Uses a conditional select (inline ternary) to extract the OTHER person's ID from each message. `.Distinct()` removes duplicates. Result = list of unique user IDs the current user has chatted with.

---

### ReviewsController (api/Reviews)

File: Controllers/ReviewsController.cs

#### POST / — Submit Review (Receiver only)

```csharp
[HttpPost]
[Authorize(Roles = "Receiver")]
public IActionResult AddReview(AddReviewDto dto)
{
    var reviewerId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)?.Value ?? "0");

    var claim = _context.Claims
        .Include(c => c.Donation)
        .FirstOrDefault(c => c.Id == dto.ClaimId && c.ReceiverId == reviewerId);

    if (claim == null) return NotFound("Claim not found.");
    if (claim.Status != ClaimStatus.PickedUp)
        return BadRequest("You can only review after pickup is verified.");

    // Prevent duplicate reviews
    if (_context.Reviews.Any(r => r.ClaimId == dto.ClaimId && r.ReviewerId == reviewerId))
        return BadRequest("You have already reviewed this transaction.");

    var review = new Review
    {
        ClaimId = claim.Id,
        ReviewerId = reviewerId,
        TargetUserId = claim.Donation.DonorId,  // Donor is the target
        Rating = dto.Rating,
        Comment = dto.Comment,
        CreatedAt = DateTime.UtcNow
    };

    _context.Reviews.Add(review);
    _context.SaveChanges();
    return Ok(new { message = "Review submitted successfully!" });
}
```

Business rules enforced:
1. Only Receivers can review (role check).
2. Claim must belong to THIS receiver (data ownership check).
3. Pickup must be verified before reviewing (state machine check).
4. Only one review per claim (duplicate prevention).
5. TargetUserId is automatically set to the Donor — user cannot review a random person.

#### GET /user/{targetUserId}

```csharp
var average = reviews.Average(r => r.Rating);
return Ok(new {
    averageRating = Math.Round(average, 1),
    totalReviews = reviews.Count,
    reviews
});
```

`Math.Round(average, 1)` — rounds to 1 decimal place. 4.666... → 4.7. Used for displaying star ratings on the donor's profile.

---

## SECTION 7: Hubs/DonationHub.cs

```csharp
public class DonationHub : Hub
{
    public void JoinLocationGroup(string cityName)
    {
        Groups.AddToGroupAsync(Context.ConnectionId, cityName).Wait();
    }
}
```

What is SignalR Hub?
A Hub is the central communication class. It inherits from `Hub`, giving access to:
- `Context.ConnectionId` — unique ID for this WebSocket connection
- `Clients` — send messages to clients
- `Groups` — manage groups of connections

`JoinLocationGroup(cityName)` — a client-callable method. When a React client calls `connection.invoke("JoinLocationGroup", "Mumbai")`, this method runs on the server, adding that client's connection to the "Mumbai" group. Future broadcasts to `Clients.Group("Mumbai")` only reach Mumbai users.

`IHubContext<DonationHub>` vs `Hub`:
- `DonationHub` (inherits Hub) — used when clients call server methods
- `IHubContext<DonationHub>` — injected into controllers/services so they can push messages to clients from outside the hub

---

## SECTION 8: Services/DonationCleanupService.cs

```csharp
public class DonationCleanupService
{
    private readonly IServiceScopeFactory _scopeFactory;
    private readonly IHubContext<DonationHub> _hubContext;

    public DonationCleanupService(IServiceScopeFactory scopeFactory,
                                   IHubContext<DonationHub> hubContext)
    {
        _scopeFactory = scopeFactory;
        _hubContext = hubContext;
    }

    public void ReleaseUnclaimedDonations()
    {
        using (var scope = _scopeFactory.CreateScope())
        {
            var context = scope.ServiceProvider.GetRequiredService<ApplicationDbContext>();

            var expiredClaims = context.Claims
                .Include(c => c.Donation)
                .Where(c => c.Status == ClaimStatus.Reserved &&
                            c.PickupDeadline < DateTime.UtcNow)
                .ToList();

            if (!expiredClaims.Any()) return;

            foreach (var claim in expiredClaims)
            {
                claim.Status = ClaimStatus.Expired;
                var donation = claim.Donation;

                if (donation.ExpiryDateTime > DateTime.UtcNow)
                {
                    donation.RemainingQuantity += claim.QuantityClaimed;

                    if (donation.Status == DonationStatus.Completed)
                        donation.Status = DonationStatus.Active;

                    // Broadcast stock restoration
                    _hubContext.Clients.All.SendAsync("ReceiveStockUpdate", new
                    {
                        DonationId = donation.Id,
                        NewQuantity = donation.RemainingQuantity,
                        Status = donation.Status.ToString()
                    }).Wait();
                }
                else
                {
                    donation.Status = DonationStatus.Expired;
                }
            }

            context.SaveChanges();
        }
    }
}
```

Why IServiceScopeFactory?
Hangfire background jobs run on a separate thread. `ApplicationDbContext` is registered as Scoped (one per HTTP request). There is no HTTP request in a background job, so you CANNOT inject DbContext directly — it would use a disposed or incorrect scope.

Solution: Inject `IServiceScopeFactory` (Singleton) and manually create a new scope for each job run. `_scopeFactory.CreateScope()` creates a fresh DI scope, then `scope.ServiceProvider.GetRequiredService<ApplicationDbContext>()` creates a fresh DbContext for this job execution. The `using` block ensures disposal when done.

Step-by-step logic:
1. Query all Claims with Status=Reserved AND PickupDeadline < Now (deadline passed, not picked up).
2. `.Include(c => c.Donation)` — loads the related Donation in one query (SQL JOIN).
3. For each expired claim: set Status = Expired.
4. If the food itself is still valid (Donation.ExpiryDateTime > Now): restore stock by adding QuantityClaimed back to RemainingQuantity.
5. If donation was "Completed" (stock hit 0), change it back to "Active".
6. Broadcast SignalR event so all users see the stock come back.
7. If food itself is expired: mark donation as Expired, no stock restore.
8. `context.SaveChanges()` — one batch save for all changes.

This runs every minute via: `RecurringJob.AddOrUpdate<DonationCleanupService>("release-unclaimed-food", s => s.ReleaseUnclaimedDonations(), Cron.Minutely)`

---

## SECTION 9: Migrations Folder

File: Migrations/20251125174813_initial database confg.cs

Contains two methods:
- `Up()` — the forward migration. Creates all tables, columns, constraints, indexes, foreign keys. This is what `dotnet ef database update` executes.
- `Down()` — the reverse migration. Drops all tables. Executed on `dotnet ef database update <previous-migration>`.

`ApplicationDbContextModelSnapshot.cs` — a C# snapshot of the current DB schema. EF Core compares this to your entity classes when you run `Add-Migration` to determine what changed.

Migration workflow:
1. Add/change a property in an entity class.
2. Run: `Add-Migration "description"` → generates new Up()/Down() methods.
3. Run: `Update-Database` → applies Up() to the actual SQL Server database.

---

*See Part 3 for Interview Questions and Answers.*
