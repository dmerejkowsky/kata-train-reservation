from requests import Session
import json


from flask import Flask, request


def create_app():
    app = Flask("ticket_office")

    @app.post("/reserve")
    def reserve():
        payload = request.json
        seat_count = payload["count"]
        train_id = payload["train_id"]

        session = Session()

        booking_reference = session.get("http://localhost:8082/booking_reference").text

        train_data = session.get(
            "http://localhost:8081/data_for_train/" + train_id
        ).json()
        seats = sorted(
            train_data["seats"].values(), key=lambda s: s["coach"] + s["seat_number"]
        )
        available_seats = [s for s in seats if not s["booking_reference"]][0:seat_count]
        seat_ids = [s["seat_number"] + s["coach"] for s in available_seats]
        reservation = {
            "train_id": train_id,
            "booking_reference": booking_reference,
            "seats": seat_ids,
        }

        reservation_payload = {
            "train_id": reservation["train_id"],
            "seats": reservation["seats"],
            "booking_reference": reservation["booking_reference"],
        }

        response = session.post(
            "http://localhost:8081/reserve",
            json=reservation_payload,
        )
        assert response.status_code == 200, response.text
        response = response.json()

        return json.dumps(reservation)

    return app


if __name__ == "__main__":
    app = create_app()
    app.run(debug=True, port=8083)
