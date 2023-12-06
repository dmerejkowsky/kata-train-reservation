package fr.arolla.trainreservation.ticket_office.repository;

import fr.arolla.trainreservation.ticket_office.Seat;

import java.util.ArrayList;
import java.util.List;

public interface SeatsRepository {
  String getRef();
  ArrayList<Seat> getSeats(String trainId);
  List<String> reserveSeat(ArrayList<Seat> seats, int seatCount, String trainId, String bookingReference);
}
