package fr.arolla.trainreservation.train_data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;

@RestController
public class TrainController {
  private JsonNode trains;

  TrainController() {
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

  @PostMapping(path = "/reset/{trainId}")
  public String reset(@PathVariable("trainId") String trainId) {
    resetTrains();
    return "";
  }

  @PostMapping("reserve")
  public String reserve(@RequestBody ReservationRequest request) {
    var trainId = request.train_id();
    var seats = request.seats();
    var bookingReference = request.booking_reference();
    JsonNode train = trains.get(trainId);
    if (train == null) {
      throw new NoSuchTrain(trainId);
    }
    var jsonSeats = train.get("seats");
    for (var seatId : seats) {
      ObjectNode jsonSeat = (ObjectNode) jsonSeats.get(seatId);
      if (jsonSeat == null) {
        throw new NoSuchSeat(seatId);
      }
      var oldBookingReference = jsonSeat.get("booking_reference").asText();
      if (!oldBookingReference.isEmpty() && !oldBookingReference.equals(bookingReference)) {
        throw new AlreadyBooked(seatId, bookingReference, oldBookingReference);
      }
      jsonSeat.put("booking_reference", bookingReference);
    }
    return train.toPrettyString();
  }

  @GetMapping("data_for_train/{trainId}")
  public String dataForTrain(@PathVariable("trainId") String trainId) {
    JsonNode train = trains.get(trainId);
    if (train == null) {
      throw new NoSuchTrain(trainId);
    }
    return train.toPrettyString();
  }

  @ExceptionHandler({NoSuchTrain.class, NoSuchSeat.class})
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public String handleNotFound(RuntimeException e) {
    return e.toString();
  }

  @ExceptionHandler({AlreadyBooked.class})
  @ResponseStatus(value = HttpStatus.CONFLICT)
  public String handleConflict(RuntimeException e) {
    return e.toString();
  }
}