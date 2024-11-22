# Step by step instructions (java)

## Extract a RestClient

Extract a RestClient with two implementors: HttpClient (using the restTemplate) and a FakeHttpClient

```java
interface RestClient {
  String getBookingReference();
  String getTrainData();
  void makeReservation(HashMap<String, Object> payload);
}
```

Use this to write an *integration test* that looks like this:

```java
@Test
void book_two_seats_from_empty_train() {
  var fakeHttpClient = new FakeHttpClient();

  var bookingController = new BookingController(fakeHttpClient);

  var bookingResponse = bookingController.book(bookingRequest);
}
```

Use a dummy FakeHttpClient at first - test should fail because of a json parsing error

The BookingController should now look like this:

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
## Improve RestClient API


Introduce a `Train` and a `Reservation` class so that the RestClient interface now looks like this:

```diff
interface RestClient {
  String getBookingReference();
- String getTrainData();
+  Train getTrain();
 
- void makeReservation(HashMap<String, Object> payload);
+ void makeReservation(Reservation reservation);
}
```

Now make sure the integration test passes:

```java
@Test
void book_two_seats_from_empty_train() {
  var train = Helpers.newEmptyTrain("express_2000",
    "1A" , "2A", "3A" , "4A"
  );
  fakeHttpClient.setTrain(train)

  var bookingResponse = bookingController.book(bookingRequest);

  assertEquals(List.of("1A", "2A"), bookingResponse.seatIds());
}
```

## Adding the second integration test

Again, make this test compile and pass:

```java
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
```

## Adding a bug in the Controller

Remove the `filter()`  call inside BookingController

The end-to-end test should fail for the right reason - because of a 409 HTTP Exception - but
the integration test should fail for the *wrong* reason.

## Make the test fail for right reason

To make it fail for the right reason, make this test compile and pass:

```java
@Test
void cannot_book_a_seat_twice() {
  var seat = Seat.available("1A");
  seat.book("abc123");

  assertThrows(AlreadyBookedException, seat.book("def456");
}
```

Then make sure the integration test calls `Seat.book()` and re-add the call to `filter()`

## Extracting the core logic

Finally, make this test compile and pass:

```java
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
```

Refactor BookingController so that it uses the SeatFinder class

