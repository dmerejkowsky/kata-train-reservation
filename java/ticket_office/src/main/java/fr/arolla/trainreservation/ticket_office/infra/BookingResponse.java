package fr.arolla.trainreservation.ticket_office.infra;

import java.util.Set;

public record BookingResponse(String train_id, String booking_reference, Set<String> seats) {
}
