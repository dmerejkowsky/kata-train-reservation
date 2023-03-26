package fr.arolla.trainreservation.controllers;

import java.util.List;

public record BookingResponse(String bookingReference, List<String> seats) {
}
