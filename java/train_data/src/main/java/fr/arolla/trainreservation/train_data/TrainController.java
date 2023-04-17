package fr.arolla.trainreservation.train_data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<String> reserve(@RequestBody ReservationRequest request) {
    var trainId = request.train_id();
    var seats = request.seats();
    var bookingReference = request.booking_reference();
    JsonNode train = trains.get(trainId);
    if (train == null) {
      String message = String.format("No train found for id: %s", trainId);
      return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }
    var jsonSeats = train.get("seats");
    for (var seatId : seats) {
      ObjectNode jsonSeat = (ObjectNode) jsonSeats.get(seatId);
      if (jsonSeat == null) {
        String message = String.format("No seat found for id: %s", seatId);
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
      }
      var oldBookingReference = jsonSeat.get("booking_reference").asText();
      if (!oldBookingReference.isEmpty() && !oldBookingReference.equals(bookingReference)) {
        String message = String.format("Cannot book seat %s with %s - already booked with %s", seatId, bookingReference, oldBookingReference);
        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
      }
      jsonSeat.put("booking_reference", bookingReference);
    }
    return new ResponseEntity<>(train.toPrettyString(), HttpStatus.OK);
  }

  @GetMapping("data_for_train/{trainId}")
  public ResponseEntity dataForTrain(@PathVariable("trainId") String trainId) {
    JsonNode train = trains.get(trainId);
    if (train == null) {
      String message = String.format("No train found for id: %s", trainId);
      return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(train.toPrettyString(), HttpStatus.OK);
  }
}