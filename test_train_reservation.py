"""
Note: this is the guiding test for the whole kata.

It assumes the three webservices are running

"""
import httpx


def test_get_booking_reference():
    client = httpx.Client()
    r1 = client.get("http://127.0.0.1:8082/booking_reference")
    r1.raise_for_status()
    ref1 = r1.text
    r2 = client.get("http://127.0.0.1:8082/booking_reference")
    r2.raise_for_status()
    ref2 = r2.text
    assert ref1 != ref2


def test_get_data_for_train_no_such_train():
    client = httpx.Client()
    response = client.get("http://127.0.0.1:8081/data_for_train/no-such")
    assert response.status_code == 404


def test_get_data_for_existing_train():
    client = httpx.Client()
    response = client.get("http://127.0.0.1:8081/data_for_train/express_2000")
    response.raise_for_status()
    train_data = response.json()
    assert "seats" in train_data


def reset_train(train_id):
    client = httpx.Client()
    response = client.post(f"http://127.0.0.1:8081/reset/{train_id}")


def test_reset_train():
    client = httpx.Client()
    response = client.post("http://127.0.0.1:8081/reset/express_2000")
    assert response.status_code == 200


def test_reserve_ok():
    reset_train("express_2000")

    client = httpx.Client()
    payload = {
        "train_id": "express_2000",
        "booking_reference": "abc123",
        "seats": ["1A", "2A"],
    }
    response = client.post("http://127.0.0.1:8081/reserve", json=payload)
    assert response.status_code == 200, response.text
    train = response.json()
    assert train["seats"]["1A"]["booking_reference"] == "abc123"


def test_reserve_conflict():
    reset_train("express_2000")

    client = httpx.Client()
    # Book 2 seats
    payload = {
        "train_id": "express_2000",
        "booking_reference": "abc123",
        "seats": ["1A", "2A"],
    }
    response = client.post("http://127.0.0.1:8081/reserve", json=payload)
    # Book 2 more seats, creating a conflict
    payload = {
        "train_id": "express_2000",
        "booking_reference": "cde456",
        "seats": ["2A", "3A"],
    }
    response = client.post("http://127.0.0.1:8081/reserve", json=payload)

    assert response.status_code == 409
    body = response.text
    assert "abc123" in body
    assert "cde456" in body
    assert "2A" in body


def test_ticket_office():
    reset_train("express_2000")

    client = httpx.Client()
    payload = {"train_id": "express_2000", "count": 2}
    response = client.post("http://127.0.0.1:8083/reserve", json=payload)

    assert response.status_code == 200, response.text
    response = response.json()
    assert response["seats"] == ["1A", "2A"]
