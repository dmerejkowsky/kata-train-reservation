package fr.arolla.trainreservation.ticket_office;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.arolla.trainreservation.ticket_office.infra.BookingRequest;
import fr.arolla.trainreservation.ticket_office.infra.BookingResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EndToEndTests {
  public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
    MediaType.APPLICATION_JSON.getType(),
    MediaType.APPLICATION_JSON.getSubtype(),
    StandardCharsets.UTF_8
  );

  @Autowired
  private MockMvc mockMvc;

  @Test
  void booking_one_seat_from_empty_train() throws Exception {
    BookingRequest bookingRequest = new BookingRequest("express_2000", 1);

    var actualResponse = makeBookingRequest(bookingRequest);

    BookingResponse expectedResponse = new BookingResponse("express_2000", "75bcd15", Set.of("1A"));
    assertThat(actualResponse).isEqualTo(expectedResponse);
  }

  private BookingResponse makeBookingRequest(BookingRequest bookingRequest) throws Exception {
    String url = "http://127.0.0.1:8083/reserve";

    ObjectMapper mapper = new ObjectMapper();
    String input = mapper.writeValueAsString(bookingRequest);

    var result = mockMvc.perform(post(url).contentType(APPLICATION_JSON_UTF8)
        .content(input))
      .andExpect(status().isOk())
      .andReturn()
      .getResponse();

    return mapper.readValue(result.getContentAsString(), BookingResponse.class);
  }

}
