package fr.arolla.trainreservation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class FakeClientTests {

  @Test
  public void get_different_booking_references_at_each_call() {
    var train = new Train();
    var fakeRestClient = new FakeClient(train);
    var ref1 = fakeRestClient.getBookingReference();
    var ref2 = fakeRestClient.getBookingReference();
    assertNotEquals(ref1, ref2);
  }

}
