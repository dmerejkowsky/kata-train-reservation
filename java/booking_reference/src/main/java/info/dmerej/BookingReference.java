package info.dmerej;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/booking_reference")
public class BookingReference {
  private int counter = 123456789;

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String bookingReference() {
    this.counter++;
    return Integer.toHexString(this.counter);
  }
}