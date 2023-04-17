package fr.arolla.trainreservation.train_data;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Already booked")

public class BookingConflict extends RuntimeException {
  private final String seatId;
  private final String currentReference;
  private final String newReference;

  public BookingConflict(String seatId, String currentReference, String newReference) {
    this.seatId = seatId;
    this.currentReference = currentReference;
    this.newReference = newReference;
  }


  @Override
  public String toString() {
    return String.format("Cannot book seat %d with %s - already booked with %s", seatId);
  }
}

