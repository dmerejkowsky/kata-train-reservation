package fr.arolla.trainreservation.ticket_office.controllers;

public record BookingRequest(String train_id, int count) {
}
