
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.TestHost;
using Newtonsoft.Json;
using System.Net.Http.Headers;
using TicketOffice.Controllers;

namespace TicketOffice.Tests;

public class EndToEndTests
{
    // Note: we need two clients
    // * One normal HTTP client to call the /reset route on the 'train data'  service
    // * One to call our web application, which requires going through a WebBuilder
    private HttpClient _httpClient;
    private HttpClient _webClient;

    [SetUp]
    public void SetUp()
    {
        _httpClient = new HttpClient();

        var builder = new WebHostBuilder();
        builder.UseStartup<Startup>();
        var server = new TestServer(builder);
        _webClient = server.CreateClient();
    }

    [Test]
    /// Given a train with no reservation at all
    /// When we book 4 seats,
    /// Get get 4 seats back
    public async Task reserve_four_seats_from_empty_train()
    {
        const string trainId = "express_2000";
        var response = await _httpClient.PostAsync($"http://127.0.0.1:8081/reset/{trainId}", new StringContent(""));
        response.EnsureSuccessStatusCode();

        var bookingRequest = new BookingRequest(trainId, 4);
        var bookingContent = new StringContent(JsonConvert.SerializeObject(bookingRequest));
        bookingContent.Headers.ContentType = new MediaTypeHeaderValue("application/json");
        response = await _webClient.PostAsync("/reserve", bookingContent);
        response.EnsureSuccessStatusCode();
        var bookingResponse = JsonConvert.DeserializeObject<BookingResponse>(await response.Content.ReadAsStringAsync());

        var bookedIds = bookingResponse.seats;
        Assert.That(bookedIds, Is.EqualTo(new List<string>(new[] { "1A", "1B", "2A", "2B" })));
    }

    /// Given a train with 4 booked seats
    /// When we booked 4 additional seats
    /// Then we get 4 new seats
    [Test]
    public async Task reserve_four_additional_seats()
    {
        // Make sure 4 seats are booked:
        const string trainId = "express_2000";
        var response = await _httpClient.PostAsync($"http://127.0.0.1:8081/reset/{trainId}", new StringContent(""));
        response.EnsureSuccessStatusCode();

        var bookingRequest = new BookingRequest(trainId, 4);
        var bookingContent = new StringContent(JsonConvert.SerializeObject(bookingRequest));
        bookingContent.Headers.ContentType = new MediaTypeHeaderValue("application/json");
        response = await _webClient.PostAsync("/reserve", bookingContent);
        response.EnsureSuccessStatusCode();

        // Book 4 additional seats
        bookingRequest = new BookingRequest(trainId, 4);
        bookingContent = new StringContent(JsonConvert.SerializeObject(bookingRequest));
        bookingContent.Headers.ContentType = new MediaTypeHeaderValue("application/json");
        response = await _webClient.PostAsync("/reserve", bookingContent);
        response.EnsureSuccessStatusCode();

        // TODO: this fails with a 419 conflict
    }
}
