from flask import Flask


class BookingReference:
    def __init__(self):
        self._count = 123456789

    def increment(self):
        self._count += 1

    def value(self):
        return str(hex(self._count))[2:]


def create_app():
    booking_reference = BookingReference()
    app = Flask("booking_reference")

    @app.get("/booking_reference")
    def get_booking_reference():
        booking_reference.increment()
        return booking_reference.value()

    return app


if __name__ == "__main__":
    app = create_app()
    app.run(debug=True, port=8082)
