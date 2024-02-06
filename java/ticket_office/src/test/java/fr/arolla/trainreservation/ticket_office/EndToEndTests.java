package fr.arolla.trainreservation.ticket_office;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

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
  void reserve_works() throws Exception {
    String url = "http://127.0.0.1:8083/reserve";
    var result = mockMvc.perform(post(url).contentType(APPLICATION_JSON_UTF8)
        .content(""))
      .andExpect(status().isOk())
      .andReturn()
      .getResponse();

    var actual = result.getContentAsString();
    assertThat(actual).isEqualTo("OK");
  }
  
}
