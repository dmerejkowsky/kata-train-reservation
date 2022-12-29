package fr.arolla.trainreservation;

import java.util.List;

public interface ServiceClient {
  String getBookingReference();
  
  Train getTrain(String trainId);


  void makeReservation(String trainId, String bookingReference, List<String> seats);
}
