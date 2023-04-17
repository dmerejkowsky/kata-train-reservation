package fr.arolla.trainreservation.train_data;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "no such train")
public class NoSuchTrain extends RuntimeException {
  private final String trainId;

  public NoSuchTrain(String trainId) {
    this.trainId = trainId;
  }

  @Override
  public String toString() {
    return String.format("No train found with id %s", trainId);
  }
}
