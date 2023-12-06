import express from "express";
import morgan from "morgan";

const router = express.Router();

const trains = require("./trains.json");

export const getTrainData = async (trainId: string) => {
  if (!trains[trainId]) throw new Error(`No such train: ${trainId}`);
  return trains[trainId];
};

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

export const bookSeat = async ({ booking_reference, train_id, seats }: any) => {
  const train = trains[train_id];
  if (!train) {
    throw new Error(`No such train: ${train_id}`);
  }

  for (const seatId of seats) {
    const seat = train.seats[seatId];
    const previousBookingReference = seat.booking_reference;
    if (
      previousBookingReference != "" &&
      previousBookingReference != booking_reference
    ) {
      throw new Error(
        `Could not book '${seatId}' with '${booking_reference}' - already booked with '${previousBookingReference}'`
      );
    }
    seat.booking_reference = booking_reference;
  }

  return train;
};

module.exports = router;
