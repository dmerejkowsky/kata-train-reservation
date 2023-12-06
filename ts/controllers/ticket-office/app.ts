import express from "express";
import fetch from "node-fetch";
import morgan from "morgan";

import { Seat } from "./seat";
import { getBookingReference } from "../booking-reference/app";
import { getTrainData, bookSeat } from "../train-data/app";

const router = express.Router();

router.post("/reserve", async (req, res) => {
  try {
    const { body } = req;
    const seatCount = body.count;
    const trainId = body.train_id;

    // Step 1: get a booking reference
    const bookingReference = await getBookingReference();

    // Step 2: fetch train data

    const train = await getTrainData(trainId);
    const seatsInTrain: Seat[] = Object.values(train.seats);

    // TODO: do not hard-code coach number
    const availableSeats = seatsInTrain
      .filter((s) => s.coach === "A")
      .filter((s) => !s.booking_reference);
    // Step 4: make reservation
    const toReserve = availableSeats.slice(0, seatCount);
    const seatIds = toReserve.map((s) => `${s.seat_number}${s.coach}`);
    const reservation = {
      booking_reference: bookingReference,
      seats: seatIds,
      train_id: trainId,
    };
    const response = await bookSeat(reservation);
    // Step 5: send back the reservation that was made
    res.send(reservation);
  } catch (error) {
    res.status(500).send("ERROR_SERVOR");
    console.log(error);
  }
});
module.exports = router;
