package fr.arolla.trainreservation.train_data;

public class NoSuchTrain extends RuntimeException {
  public NoSuchTrain(String trainId) {
    super(String.format("No train found for id %s", trainId));
  }
}
