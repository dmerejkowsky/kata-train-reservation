package fr.arolla.trainreservation.ticket_office.domain;

import java.util.List;

public class FakeTrainDataClient implements TrainDataClient {


  private Train train;

  @Override
  public Train getTrain() {
    return train;
  }

  public void setTrain(Train train) {
    this.train = train;
  }

  @Override
  public void bookSeats(List<String> seatIds) {

  }
}
