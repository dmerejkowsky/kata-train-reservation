package fr.arolla.trainreservation.ticket_office.domain;

public class Train {

  private final String trainId;

  public Train(String trainId) {
    this.trainId = trainId;
  }

  public String getTrainId() {
    return trainId;
  }


}
