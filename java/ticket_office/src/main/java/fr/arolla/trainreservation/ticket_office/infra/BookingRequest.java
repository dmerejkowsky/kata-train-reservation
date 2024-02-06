package fr.arolla.trainreservation.ticket_office.infra;

public record BookingRequest(String train_id, int count) {
}
