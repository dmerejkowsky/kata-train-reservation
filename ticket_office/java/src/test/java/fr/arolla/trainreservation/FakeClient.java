package fr.arolla.trainreservation;

import java.util.List;

public class FakeClient implements ServiceClient {
  @Override
  public String getBookingReference() {
    return "abc123";
  }

  @Override
  public String getTrainData(String trainId) {
    return "";
  }

  @Override
  public void makeReservation(String trainId, String bookingReference, List<String> seats) {
    // TODO
  }
}
