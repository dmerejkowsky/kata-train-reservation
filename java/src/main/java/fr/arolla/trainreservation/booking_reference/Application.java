package fr.arolla.trainreservation.booking_reference;

import static spark.Spark.get;

public class Application {
  public static void main(String[] args) {
    // init();
    // port(8082);
    get("/booking_reference", (req, res) -> "Hello, world");
  }


}
