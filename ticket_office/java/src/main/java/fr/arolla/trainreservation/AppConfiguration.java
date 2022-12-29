package fr.arolla.trainreservation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {
  @Bean
  public ServiceClient serviceClient() {
    return new RestClient();
  }
}
