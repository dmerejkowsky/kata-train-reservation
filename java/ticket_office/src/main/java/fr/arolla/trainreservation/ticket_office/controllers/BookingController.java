package fr.arolla.trainreservation.ticket_office.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class BookingController {
  @RequestMapping("/reserve")
  String reserve() {
    return "OK";
  }
}
