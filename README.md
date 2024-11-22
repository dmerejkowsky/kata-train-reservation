# Train Reservation kata

Original starting point, by Emily Bache:

=> https://github.com/emilybache/KataTrainReservation

# Context

Railway operators aren't always known for their use of cutting edge
technology, and in this case they're a little behind the times. The
railway people want you to help them to improve their online booking
service. They'd like to be able to not only sell tickets online, but to
decide exactly which seats should be reserved, at the time of booking.

You're working on the "TicketOffice" service, and your next task is to
implement the feature for reserving seats on a particular train. The
railway operator has a service-oriented architecture, and both the
interface you'll need to fulfill, and the two services you'll need to use
are already implemented - they are named 'train-data' and 'booking-reference'
and their API is specified below. The third service is named 'ticket-office'
and it's your job to finish implementing it (but keep reading till the end
before jumping to the implementation!)

# Business Rules around reservations

There are various business rules and policies around which seats may be
reserved. For a train overall, no more than 70% of seats may be reserved
in advance, and ideally no individual coach should have no more than 70%
reserved seats either. However, there is another business rule that says
you must put all the seats for one reservation in the same coach. This
could make you and go over 70% for some coaches, just make sure to keep
to 70% for the whole train.


# Booking Reference API

To get a new booking reference, simply call:

`GET http://localhost:8082/booking_reference`

The booking reference will be different each time your call it

# Train Data API

## Getting train data

`GET http://localhost:8081/data_for_train/<train_id>`

This will return a json document with information about the seats that
this train has. The document you get back will look for example like
this:


```json
{
    "seats": {
        "1A": {
            "booking_reference": "abc123def",
            "seat_number": "1",
            "coach": "A"
        },
        "2A": {
            "booking_reference": "",
            "seat_number": "2",
            "coach": "A"
        }
    }
}
```

Here, seat "1A" is booked, but seat "2A" is free.

## Booking some seats

`POST http://localhost:8081/reserve`

The body should look like:

```json
    {
        "train_id": "express_2000",
        "seats": ["1A", "2A"],
        "booking_reference": "abc123def"
   }
```

Note that the server will prevent you from booking a seat that is
already reserved with another booking reference, by returning a `409
conflict` status.

It is however OK to try and book the same seat with twice with the same booking reference.

## Resetting the train

Simply call:

`POST  http://localhost:8081/reset/<train-id>`

This should only be used for tests, of course


# TicketOffice API

The Ticket Office service needs to respond to a HTTP POST request that
comes with form data telling you which train the customer wants to
reserve seats on, and how many they want.

For instance:

```json
{
  "train_id": "express_2000",
  "count": 2
}
```

It should return a json document detailing the reservation that has been
made, containing the booking reference, and the ids of the seats that
have been reserved, and the name of the train:

```json
{
   "booking_reference" : "75bcd15",
   "train_id": "express_2000",
   "seats" : [
      "1A",
      "1B"
   ]
}
```

If it is not possible to find suitable seats to reserve, the service
should instead return a 400 status code.

# Instructions

Choose a language and open the matching folder in your favorite IDE. Follow the README
to run the 3 webservices and the tests.

You'll notice that all the tests are passing - but the code does not
implement all of the business rules - for instance, it does not check
coach occupancy at all.

Your goal is to switch to a better architecture - (hexagonal, for
instance, with domain seperated from infra) - and only *after* implement
the rest of the specifications.

In particular, you should put domain-specific code in a separate file,
and add some unit tests for it that can run *without* making any HTTP
calls or parsing JSON.

Note that you should *not* touch the code of the `booking_reference` and `train_data`
services - and yould should probably keep the "end-to-end" tests as is.

Finally, please note there's a pre-requisite if you choose to do this kata in Java: you
need to know SpringBoot well.

Have fun!
