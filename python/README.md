# Readme for Python

## Installing dependencies

Create a virtualenv, install the dependencies from `requirements.txt` in it, and
activate it.

## Running the three webservices:

* Make sure port 8081, 8082, and 8083 are free
* Open three consoles and run

```
$ cd train_data
$ python app.py
```

```
$ cd booking_reference
$ python app.py
```

```
$ cd ticket_office
$ python app.py
```

## Running the end-to-end tests

Activate the virtualenv and run:

```
$ cd ticket_office
$ python -m pytest
```

The end-to-end tests should pass.

## Start writing integration tests

Your first goal is to make sure the tests can run without the other two web services running.

Doing so requires some changes in the production code.  In particular,
you should remove the usage of the requests `Session()` object inside the
`reserve()` function.

But you need to do that without breaking the production code and the end-to-end tests.

And add a new integration test:

## Introducing a BookingController

The code as is is difficult to refactor and test - so let's add some abstractions using
classes.

First, introduce a `BookingController` class so that the code looks like this:

```python
class BookingController:

    def __init__(self):
        pass

    def reserve(self, *, seat_count, train_id):
        # move code from create_app() here
        session = Session()
        booking_reference = session.get("http://localhost:8082/booking_reference").text
        # ...
        reservation = {
            "train_id": train_id,
            "booking_reference": booking_reference,
            "seats": seat_ids,
        }
        # ...
        response = session.post(
            "http://localhost:8081/reserve",
            json=reservation_payload,
        )
        assert response.status_code == 200, response.text

        return reservation

def create_app():
    app = Flask("ticket_office")

    booking_controller = BookingController()

    @app.post("/reserve")
    def reserve():
        body = request.json
        seat_count = body["count"]
        train_id = body["train_id"]

        reservation = booking_controller.reserve(train_id=train_id, seat_count=seat_count)
        return json.dumps(reservation)
```

This should make it possible to test the `BookingController` class without the need for anything
related to Flask (except the call to `/reset`)

```python
# In integration_tests.py

def test_reserve_seats_from_empty_train():
    train_id = "express_2000"
    session = requests.Session()
    response = session.post(f"http://127.0.0.1:8081/reset/{train_id}")
    response.raise_for_status()

    controller = BookingController()
    response = controller.reserve(seat_count=2, train_id=train_id)
    assert response["train_id"] == train_id
    assert response["seats"] == ["1A", "2A"]
```

## Getting rid of HTTP calls in the controller

To get rid of the HTTP calls in the controller, introduce an Client object:

```python
from requests import Session
class Client:
    def __init__(self):
        self._session = Session()

    def get_booking_reference(self):
        return self._session.get("http://localhost:8082/booking_reference").text

    def get_train_data(self, train_id):
        return self._session.get(f"http://localhost:8081/data_for_train/{train_id}").json

    def make_reservation(self, *, train_id, booking_reference, seats):
        body = {
            "train_id": train_id,
            "booking_reference": booking_reference,
            "seats": seats,
        }
        response = self._session.post(
            "http://localhost:8081/reserve",
            json=body,
        )
        assert response.status_code == 200, response.text
```

Then refactor `BookingController` to use the Client:


```diff
class BookingController:
-    def __init__(self):
+    def __init__(self, *, client=client):
+       self.client = client
+
    def reserve(self, *, seat_count, train_id):
-      session = Session()
-      booking_reference = session.get("http://localhost:8082/booking_reference").text
+      booking_reference = self.client.get_booking_reference()
 
-       train_data = session.get(
-           f"http://localhost:8081/data_for_train/" + train_id
-       ).json()
+       train_data = self.client.get_train_data(train_id)

-       reservation_payload = {
-           "train_id": reservation["train_id"],
-           "seats": reservation["seats"],
-           "booking_reference": reservation["booking_reference"],
-       }
-       response = session.post(
-           "http://localhost:8081/reserve",
-           json=reservation_payload,
-       )
-       assert response.status_code == 200, response.text
+       self.client.make_reservation(train_id=train_id, seats=seat_ids, booking_reference=booking_reference)
```

And fix `create_app()`:

```diff
def create_app():
-   booking_controller = BookingController()
+   client = Client()
+   booking_controller = BookingController(client=client)
```

Make sure tests continue passing.


## Introducing a FakeClient

Rewrite the integration test:

```python
# In integration_tests.py

def test_reserve_seats_from_empty_train():
    train_id = "express_2000"
    fake_client = FakeClient()
    controller = BookingController(client=fake_client)

    response = controller.reserve(seat_count=2, train_id=train_id)

    assert response["train_id"] == train_id
    assert response["seats"] == ["1A", "2A"]
```

Make this test pass - you'll have to implement a `FakeClient` class

## Introducing a bug in the BookingController

Introduce a bug in the controller:


```diff
-      available_seats = [s for s in seats if not s["booking_reference"]][0:seat_count]
+      available_seats = [s for s in seats][0:seat_count]
```

The end-to-end tests should fail (because the Client gets a 409 status code) - but the integration tests
should still pass.

This means we need better tests. Revert the change in the BookingController first.

### Introduce some unit testing

Add the following unit test:
```python
# In unit_tests.py
def test_cannot_book_seat_twice():
    seat = Seat(number="1A", booking_reference="abc123")
    with pytest.raises(AlreadyBooked):
        seat.book("def456")
```

And create the `Seat` and `AlreadyBooked` class so that it passes

Ditto for a test about the `Train`  class:

```python
# In unit_tests.py
def test_reserve_seats_from_empty_train():
    train = create_train(id="express_2000", seats=["1A", "2A", "3A", "4A"])
    seat = Seat(number="1A", booking_reference="abc123")
    with pytest.raises(AlreadyBooked):
        seat.book("def456")

def test_reserve_seats_from_empty_train():
    train = create_train(id="express_2000", seat_ids=["1A", "2A", "3A", "4A"])
    train.book(["1A", "2A"], booking_reference="abc123")

    assert train.get_seat("1A").booking_reference == "abc123"
```

## Better Client

Instead of returning a plain dict, replace the calls to `get_train_data()` by a call to `get_train()`,
while returning a `Train` instance


```diff
# In BookingController()
-       train_data = self.client.get_train_data(train_id)
-       available_seats = [s for s in train_data["seats"].values()][0:seat_count]
+       train = self.client.get_train(train_id)
+       available_seats = [s for s in train.seats][0:seat_count]

# In integration_tests:

def test_reserve_seats_from_empty_train():
    train_id = "express_2000"
-   fake_client = FakeClient()
+   train = create_train(id="express_2000", seat_ids=["1A", "2A", "3A", "4A"])
+   fake_client = FakeClient(train=train)
    controller = BookingController(client=fake_client)

```

Introduce the bug a second time in the controller

Add a new integration test:

```python
def test_reserve_additional_seats():
     train = create_train(train_id, seat_ids=["1A", "2A", "3A", "4A"])
     train.book(["1A", "2A"], booking_reference="old-reference")

    controller.reserve(seat_count=2, train_id=train_id)
```

Make sure that this time, the integration test  fails for the right reason - and revert the bug again

## Testing the core logic

Add a new unit test:

```python
def test_finding_seats():
     train = create_train(train_id, seat_ids=["1A", "2A", "3A", "4A"])
     train.book(["1A", "2A"], booking_reference="old-reference")

     found = find_seats(train, count=2)

     assert found == ["3A", "4A"]
 ```

 Make it pass by creating a `find_seats()` method.

 Refactor the controller to use this function.
