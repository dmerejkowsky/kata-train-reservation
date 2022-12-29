package fr.arolla.trainreservation;

public class AlreadyBookedException extends RuntimeException {
  public AlreadyBookedException(String id, String oldReference, String newReference) {
    super(String.format("Cannot book seat '%s' with reference '%s' - it is already booked with '%s'", id, newReference, oldReference));
  }
}
