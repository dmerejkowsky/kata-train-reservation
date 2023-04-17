package fr.arolla.trainreservation.train_data;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code = HttpStatus.CONFLICT)

public class BookingConflict extends RuntimeException {
  private final String seatId;
  private final String currentReference;
  private final String newReference;

  public BookingConflict(String seatId, String currentReference, String newReference) {
    this.seatId = seatId;
    this.currentReference = currentReference;
    this.newReference = newReference;
    System.out.println(this);
  }


  @Override
  public String toString() {
    return String.format("Cannot book seat %s with %s - already booked with %s", seatId, newReference, currentReference);
  }
}

