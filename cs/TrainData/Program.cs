using System.Text.Json.Nodes;

var builder = WebApplication.CreateBuilder(args);
builder.WebHost.UseUrls("http://127.0.0.1:8081/");
var app = builder.Build();

var json = File.ReadAllText("trains.json");
var trains = JsonNode.Parse(json);
if (trains == null)
{
    System.Console.Error.WriteLine("Could not parse trains.json");
    Environment.Exit(1);
}

app.MapGet("/data_for_train/{trainId}", (string trainId) =>
{
    var train = trains[trainId];
    if (train == null)
    {
        return Results.NotFound();
    }
    return Results.Ok(train);
});

app.MapPost("/reset/{trainId}", (string trainId) =>
 {
     var train = trains[trainId];
     if (train == null)
     {
         return Results.NotFound();
     }
     var seats = train["seats"];
     if (seats == null)
     {
         return Results.Problem();
     }
     foreach (KeyValuePair<string, JsonNode?> kp in seats.AsObject())
     {
         kp.Value!["booking_reference"] = "";
     }

     return Results.Ok();
 });

app.MapPost("/reserve", (ReservationRequest request) =>
{
    var trainId = request.train_id;
    var newBookingReference = request.booking_reference;
    var train = trains[trainId];
    if (train == null)
    {
        return Results.NotFound();
    }
    var seats = train["seats"]!;
    foreach (var seatId in request.seats)
    {
        var seat = seats[seatId];
        if (seat == null)
        {
            return Results.BadRequest($"No such seat id: {seatId}");
        }
        var oldBookingReference = seat["booking_reference"]!.GetValue<string>();
        if (oldBookingReference != "" && oldBookingReference != newBookingReference)
        {
            return Results.Conflict(
                $"Cannot book seat '{seatId}' with '{newBookingReference}' " +
                $" - already booked with '{oldBookingReference}'"
           );
        }

        seat["booking_reference"] = newBookingReference;
    }

    return Results.Ok(train);
});

app.Run();


#pragma warning disable IDE1006 // Naming Styles
record ReservationRequest(string train_id, List<string> seats, string booking_reference);
#pragma warning restore IDE1006 // Naming Styles