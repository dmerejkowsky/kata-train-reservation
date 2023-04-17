package fr.arolla.trainreservation.booking_reference;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
  private int counter = 123456789;

  @RequestMapping("booking_reference")

  public String bookingReference() {
    this.counter++;
    return Integer.toHexString(this.counter);
  }
}