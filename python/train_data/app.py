import json
from flask import Flask, jsonify, request


def create_app():
    with open("trains.json", "r") as f:
        trains = json.load(f)

    app = Flask("train_data")

    @app.get("/data_for_train/<train_id>")
    def data_for_train(train_id):
        train = trains.get(train_id)
        if not train:
            return f"No train with id '{train_id}'", 404
        return jsonify(train)

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
                return (
                    f"Cannot book seat {seat} with {booking_reference} - "
                    + f"already booked with {existing_reservation}",
                    409,
                )

        for seat in seats:
            train["seats"][seat]["booking_reference"] = booking_reference

        return jsonify(train)

    return app

if __name__ == '__main__':
    app = create_app()
    app.run(debug=True, port=8081)
