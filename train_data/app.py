"""
You can get information about which each train has by using this
service. For test purposes, you can start a local service using this
code. You can assume the real service will behave the same way, but be
available on a different url.

Install [Python](http://python.org) and
[Flask](https://flask.palletsprojects.com), then start the server by running:

  FLASK_DEBUG=true flask run --port 8081

You can use this service to get data for example about the train with id "express_2000" like this:

    http://localhost:8081/data_for_train/express_2000

this will return a json document with information about the seats that
this train has. The document you get back will look for example like
this:

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

Note I've left out all the extraneous details about where the train is
going to and from, at what time, whether there's a buffet car etc. All
that's there is which seats the train has, and if they are already
booked. A seat is available if the "booking_reference" field contains an
empty string. To reserve seats on a train, you'll need to make a POST
request to this url:

    http://localhost:8081/reserve

and attach form data for which seats to reserve. There should a json-encoded
body looking like this:

    {
        "train_id": "express_2000",
        "seats", : ["1A", "2A"],
        "booking_reference": "abc123def"
   }

Note that the server will prevent you from booking a seat that is
already reserved with another booking reference.

The service has one additional method, that will remove all reservations
on a particular train. Use it with care!:

    http://localhost:8081/reset/express_2000
"""

import json
from flask import Flask, jsonify, request


def create_app():
    with open("trains.json", "r") as f:
        trains = json.load(f)
        foos = []

    app = Flask("train_data")

    @app.get("/data_for_train/<train_id>")
    def data_for_train(train_id):
        train = trains.get(train_id)
        if not train:
            return f"No train with id '{train_id}'", 404
        return jsonify(train)

    @app.post("/foo")
    def foo():
        foos.append("foo")
        return jsonify(foos)

    @app.post("/reset/<train_id>")
    def reset(train_id):
        train = trains.get(train_id)
        if not train:
            return jsonify({})

        for seat_id, seat in train["seats"].items():
            seat["booking_reference"] = ""
        return jsonify({"reset": train_id})

    @app.post("/reserve")
    def reserve():
        payload = request.json

        train_id = payload.get("train_id")
        if not train_id:
            return "Missing 'train_id' in body", 400

        train = trains.get(train_id)
        if not train:
            return f"No train with id '{train_id}'", 404

        seats = payload.get("seats")
        if not seats:
            return "Missing 'seats' in body", 400

        booking_reference = payload.get("booking_reference")
        if not booking_reference:
            return "Missing 'booking_reference' in body", 400

        for seat in seats:
            if not seat in train["seats"]:
                return f"No seat found with number {seat}", 404
            existing_reservation = train["seats"][seat]["booking_reference"]
            if existing_reservation and existing_reservation != booking_reference:
                return f"already booked with reference: {existing_reservation}", 409

        for seat in seats:
            train["seats"][seat]["booking_reference"] = booking_reference

        return jsonify(train)

    return app
