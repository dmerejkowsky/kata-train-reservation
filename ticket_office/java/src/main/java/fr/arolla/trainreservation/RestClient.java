package fr.arolla.trainreservation;

import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestClient implements ServiceClient {
  private final RestTemplate restTemplate;

  public RestClient() {
    restTemplate = new RestTemplate();
  }

  @Override
  public String getBookingReference() {
    return restTemplate.getForObject("http://127.0.0.1:8082/booking_reference", String.class);
  }

  @Override
  public String getTrainData(String trainId) {
    return restTemplate.getForObject("http://127.0.0.1:8081/data_for_train/" + trainId, String.class);
  }

  @Override
  public void makeReservation(String trainId, String bookingReference, List<String> seats) {
    Map<String, Object> payload = new HashMap<>();
    payload.put("train_id", trainId);
    payload.put("seats", seats);
    payload.put("booking_reference", bookingReference);
    restTemplate.postForObject("http://127.0.0.1:8081/reserve", payload, String.class);
  }
}
