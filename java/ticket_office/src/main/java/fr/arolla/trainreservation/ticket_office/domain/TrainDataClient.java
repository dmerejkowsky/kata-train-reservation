package fr.arolla.trainreservation.ticket_office.domain;

import java.util.List;

public interface TrainDataClient {
  Train getTrain();

  void bookSeats(List<String> seatIds);
}
