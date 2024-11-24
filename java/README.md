# Instructions for Java

## Running the three webservices:

* Make sure you have JDK 17 installed
* Make sure port 8081, 8082, and 8083 are free
* Open two consoles and run the two external web services with


```
cd booking_reference
./mvnw compile exec:java
```

and

```
cd train_data
./mvnw compile exec:java
```

To run the ticket_office service, you can use:

```
cd ticket_office
./mvnw compile exec:java
```

or use the appropriate "run configuration" from IntelliJ

## Running the tests

```
cd ticket_office
./mvnw compile exec:java
```

or use the appropriate "run configuration" from IntelliJ

Make sure the end-to-end test pass.

## Start writing integration tests

Your first goal is to make sure the tests can run without the other two web services running.

Doing so requires some changes in the production code.  In particular,
you should remove the `restTemplate` attribute of the
`BookingController` class, and remove the calls to
`restTemplate.getForObject` and `restTemplate.postForObject`

But you need to do that without breaking the production code and the end-to-end tests.

## Implementing an interface

In order to keep the end-to-end tests writing while being able to call
the `BookingController()` methods direrctly in a test,  extract a
RestClient interface with two implementations: HttpClient (using the restTemplate)
and a FakeHttpClient:
  
```java
interface RestClient {
  String getBookingReference();
  String getTrainData();
  void makeReservation(HashMap<String, Object> payload);
}
```

```java
// Implementation of the interface for the production code:
class HttpClient implements RestClient {
  private final ResTemplate restTemplate;

  String getBookingReference() {
    // ...
  }
}
```

```java
// Implementation of the interface for the test code:
class FakeHttpClient implements RestClient {

  String getBookingReference() {
    return "abc123";
  }

  String getTrainData() {
    return "";
  }

  // ...
}
```

Refactor the BookingController class so that it looks like this:

```java
BookingController() {
  private final restClient RestClient;
  // Note: no more restTemplate allowed here!

  BookingController(RestClient restClient) {
    this.restClient = restClient;
  }

  BookingController() {
    this(new HttpClient());
  }

  // ...
}
```

Finally, write an *integration test* that looks like this and make sure it compiles:

```java
class IntegrationTests {
  @Test
  void book_two_seats_from_empty_train() {
    var fakeHttpClient = new FakeHttpClient();

    var bookingController = new BookingController(fakeHttpClient);

    var bookingResponse = bookingController.book(bookingRequest);
  }
}
```

The end-to-end tests should still pass, but the integration test should fail because of a JSON parsing error.

## Improve RestClient API

In order to make the integration test pass,  introduce a `Train` and a
`Reservation` class so that the RestClient interface now looks like
this:

```diff
interface RestClient {
  String getBookingReference();
- String getTrainData();
+  Train getTrain();
 
- void makeReservation(HashMap<String, Object> payload);
+ void makeReservation(Reservation reservation);
}
```

Refactor the production and the test code

Now change the integration test to look like this and make sure it passes:

```java
class IntegrationTests {
  @Test
  void book_two_seats_from_empty_train() {
    var train = Helpers.newEmptyTrain("express_2000",
      "1A" , "2A", "3A" , "4A"
    );
    fakeHttpClient.setTrain(train)

    var bookingResponse = bookingController.book(bookingRequest);

    assertEquals(List.of("1A", "2A"), bookingResponse.seatIds());
  }
}
```

## Adding the second integration test

Again, make this test compile and pass:

```java
class IntegrationTests {
  @Test
  void book_two_additional_seats() {
    var train = Helpers.newEmptyTrain("express_2000",
      "1A" , "2A", "3A" , "4A"
    );
    train.bookSeats(List.of("1A", "2A"));
    fakeHttpClient.setTrain(train)

    var bookingResponse = bookingController.book(bookingRequest);

    assertEquals(List.of("3A", "4A"), bookingResponse.seatIds());
  }
}
```

## Adding a bug in the Controller

Remove the `filter()`  call inside BookingController

The end-to-end test should fail for the right reason - because of a 409 HTTP Exception - but
the integration tests will fail for the *wrong* reason.

## Make the integration test fail for right reason

To make the integration fail for the right reason, add a Unit Test for the seat class:

```java
class SeatTestst {
  @Test
  void cannot_book_a_seat_twice() {
    var seat = Seat.available("1A");
    seat.book("abc123");

    assertThrows(AlreadyBookedException, seat.book("def456");
  }
}
```

Then make sure the integration tests call `Seat.book()`.

This time, the IntegrationTests should fail with an `AlreadyBookedException`.

When this is the case, re-add the call to `filter()` and make sure all the tests pass

## Extracting the core logic

Make this test compile and pass:

```java
class SeatFinderTests {
  @Test
  void finding_seats() {
    var train = Helpers.newEmptyTrain("express_2000",
      "1A" , "2A", "3A" , "4A"
    );
    train.bookSeats("1A" , "2A");

    var seatFinder = new SeatFinder(train);

    var ids = seatFinder.findSeats(2);

    assertEquals(List.of("3A", "4A"), ids);
  }
}
```

Refactor BookingController so that it uses the SeatFinder class.

## Conclusion

What do you think about the final architecture?
