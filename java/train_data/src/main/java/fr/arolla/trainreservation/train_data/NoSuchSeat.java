package fr.arolla.trainreservation.train_data;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NoSuchSeat extends RuntimeException {
  private final String seatId;

  public NoSuchSeat(String seatId) {
    this.seatId = seatId;
  }

  @Override
  public String toString() {
    return String.format("No seat found with id %s", seatId);
  }
}
