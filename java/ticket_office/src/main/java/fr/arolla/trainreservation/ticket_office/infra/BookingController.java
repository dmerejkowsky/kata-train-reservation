package fr.arolla.trainreservation.ticket_office.infra;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;


@RestController
public class BookingController {
  @RequestMapping("/reserve")
  BookingResponse reserve(@RequestBody BookingRequest bookingRequest) {
    BookingResponse bookingResponse = new BookingResponse("express_2000", "75bcd15", Set.of("1A"));

    return bookingResponse;
  }
}
