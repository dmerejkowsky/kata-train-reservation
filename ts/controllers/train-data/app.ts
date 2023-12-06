import express from "express";
import morgan from "morgan";

const router = express.Router();

const trains = require("./trains.json");

router.get("/data_for_train/:trainId", (req, res) => {
  const trainId = req.params.trainId;
  const train = trains[trainId];
  if (!train) {
    res.status(404).send(`No such train: ${trainId}`);
    return;
  }
  res.send(train);
});

router.post("/reset/:trainId", (req, res) => {
  const trainId = req.params.trainId;
  const train = trains[trainId];
  if (!train) {
    res.status(404).send(`No such train: ${trainId}`);
    return;
  }
  const { seats } = train;

  for (const seatId in seats) {
    const seat = seats[seatId];
    seat.booking_reference = "";
  }
  res.send("ok");
});

router.post("/reserve/", (req, res) => {
  const { booking_reference, train_id, seats } = req.body;
  const train = trains[train_id];
  if (!train) {
    res.status(404).send(`No such train: ${train_id}`);
    return;
  }

  for (const seatId of seats) {
    const seat = train.seats[seatId];
    const previousBookingReference = seat.booking_reference;
    if (
      previousBookingReference != "" &&
      previousBookingReference != booking_reference
    ) {
      res
        .status(409)
        .send(
          `Could not book '${seatId}' with '${booking_reference}' - already booked with '${previousBookingReference}'`
        );
      return;
    }
    seat.booking_reference = booking_reference;
  }

  res.send(train);
});

module.exports = router;
