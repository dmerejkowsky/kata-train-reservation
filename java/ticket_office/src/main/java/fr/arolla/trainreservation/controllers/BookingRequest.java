package fr.arolla.trainreservation.controllers;

public record BookingRequest(String train_id, int count) {
}
