package info.dmerej;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;

@Path("/")
public class TrainResource {
  private JsonNode trains;

  TrainResource() {
    resetTrains();
  }

  private void resetTrains() {
    InputStream inputStream = Thread.currentThread()
      .getContextClassLoader()
      .getResourceAsStream("trains.json");
    var objectMapper = new ObjectMapper();
    try {
      trains = objectMapper.readValue(inputStream, JsonNode.class);
    } catch (IOException e) {
      throw new RuntimeException("Could not parse trains.json resource");
    }
  }

  @Path("reset/{trainId}")
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public String reset(@PathParam("trainId") String trainId) {
    resetTrains();
    return "";
  }

  @Path("reserve")
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public Response reserve(ReservationRequest request) {
    var trainId = request.train_id();
    var seats = request.seats();
    var bookingReference = request.booking_reference();
    JsonNode train = trains.get(trainId);
    if (train == null) {
      return Response.status(404).entity("No such train: " + trainId).build();
    }
    var jsonSeats = train.get("seats");
    for (var seatId : seats) {

      ObjectNode jsonSeat = (ObjectNode) jsonSeats.get(seatId);
      if (jsonSeat == null) {
        return Response.status(404).entity("No such seat: " + seatId).build();
      }
      var oldBookingReference = jsonSeat.get("booking_reference").asText();
      if (!oldBookingReference.isEmpty() && oldBookingReference != bookingReference) {
        String message = String.format(
          "Seat %s is already booked with %s - cannot book with %s",
          seatId,
          oldBookingReference,
          bookingReference
        );
        return Response.status(409).entity(message).build();
      }
      jsonSeat.put("booking_reference", bookingReference);
    }
    return Response.status(200).entity(train).build();
  }

  @Path("data_for_train/{trainId}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response get(@PathParam("trainId") String trainId) {
    JsonNode train = trains.get(trainId);
    if (train == null) {
      return Response.status(404).entity("No such train: " + trainId).build();
    }
    return Response.status(200).entity(train).build();
  }
}