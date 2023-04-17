package fr.arolla.trainreservation.ticket_office.controllers;

import java.util.List;

public record BookingResponse(String trainId, String bookingReference, List<String> seats) {
}
