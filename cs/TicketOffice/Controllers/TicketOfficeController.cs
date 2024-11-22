using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System.Net.Http.Headers;

namespace TicketOffice.Controllers;
public record BookingRequest(string train_id, int count) { }
public record BookingResponse(string booking_reference, List<string> seats) { }

[ApiController]
[Route("reserve")]
public class TicketOfficeController : ControllerBase
{

    [HttpPost(Name = "reserve")]
    public async Task<BookingResponse> Reserve(BookingRequest request)
    {

        var trainId = request.train_id;
        var count = request.count;

        // Step 1: Get a new booking reference from the 'booking_reference' service
        var client = new HttpClient();
        var response = await client.GetAsync("http://127.0.0.1:8082/booking_reference");
        response.EnsureSuccessStatusCode();
        var bookingReference = await response.Content.ReadAsStringAsync();

        // Step 2 : Get the train data from the 'train_data' service
        response = await client.GetAsync($"http://127.0.0.1:8081/data_for_train/{request.train_id}");
        response.EnsureSuccessStatusCode();
        var json = await response.Content.ReadAsStringAsync();
        var data = JObject.Parse(json);
        var jsonSeats = data["seats"].Values();
        var availaibleSeats = new List<string>();
        foreach (var jsonSeat in jsonSeats)
        {
            var seatNumber = jsonSeat["seat_number"].Value<string>();
            var coachId = jsonSeat["coach"].Value<string>();
            var seatId = seatNumber + coachId;
            availaibleSeats.Add(seatId);
        }
        availaibleSeats.Sort();
        var seatsToBook = availaibleSeats.Take(count).ToList();

        // Step 3: make the reservation on the 'train data' servie
        var reservation = new Dictionary<string, object>
        {
            { "train_id", trainId },
            { "seats", seatsToBook },
            { "booking_reference", bookingReference }
        };
        var reservationContent = new StringContent(JsonConvert.SerializeObject(reservation));
        reservationContent.Headers.ContentType = new MediaTypeHeaderValue("application/json");
        response = await client.PostAsync("http://127.0.0.1:8081/reserve", reservationContent);
        response.EnsureSuccessStatusCode();

        // Step 4: return the booking response
        var result = new BookingResponse(bookingReference, seatsToBook);
        return await Task.FromResult(result);
    }
}

