"""
You can get a unique booking reference using this service. For test
purposes, you can start a local service using this code. You can assume
the real service will behave the same way, but be available on a
different url.

Install [Python](http://python.org) and
[Flask](https://flask.palletsprojects.com), then start the server by running:

  FLASK_DEBUG=true flask run --port 8082

You can use this service to get a unique booking reference. Make a GET request to:

    http://localhost:8082/booking_reference

This will return a string that looks a bit like this:

	75bcd15
"""

from flask import Flask


class Counter:
    def __init__(self):
        self._count = 123456789

    def increment(self):
        self._count += 1

    def value(self):
        return str(hex(self._count))[2:]


def create_app():
    counter = Counter()
    app = Flask("booking_reference")

    @app.get("/booking_reference")
    def get_booking_reference():
        counter.increment()
        return counter.value()

    return app
