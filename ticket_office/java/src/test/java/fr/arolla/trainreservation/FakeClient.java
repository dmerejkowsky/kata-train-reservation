package fr.arolla.trainreservation;

import java.util.List;

public class FakeClient implements ServiceClient {
  private int count = 0;
  private final Train train;

  public FakeClient(Train train) {
    this.train = train;
  }

  @Override
  public String getBookingReference() {
    count++;
    return Integer.toString(count);
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
