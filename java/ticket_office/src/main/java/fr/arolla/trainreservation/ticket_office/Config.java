package fr.arolla.trainreservation.ticket_office;

import fr.arolla.trainreservation.ticket_office.repository.JsonRepository;
import fr.arolla.trainreservation.ticket_office.repository.SeatsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
  @Bean
  SeatsRepository getRepo(){
    return new JsonRepository();
  }
}
