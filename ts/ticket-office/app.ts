import express from 'express'
import fetch from 'node-fetch'
import morgan from 'morgan'

import { Seat } from './seat'

const port = 8083

const app = express()
app.use(express.json())
app.use(morgan('tiny'))
// Function to handle reservation logic
const makeReservation = async (count : any, trainId: any) => {
  let response = await fetch('http://localhost:8082/booking_reference');
  const bookingReference = await response.text();

  response = await fetch(`http://localhost:8081/data_for_train/${trainId}`);
  const train = await response.json();
  const seatsInTrain: Seat[] = Object.values(train.seats);

  // TODO: Avoid hard-coding coach number
  const availableSeats = seatsInTrain.filter(s => s.coach === 'A').filter(s => !s.booking_reference);

  const toReserve = availableSeats.slice(0, count);
  const seatIds = toReserve.map(s => `${s.seat_number}${s.coach}`);
  const reservation = {
    booking_reference: bookingReference,
    seats: seatIds,
    train_id: trainId,
  };

  response = await fetch(`http://localhost:8081/reserve`, {
    method: 'POST',
    body: JSON.stringify(reservation),
    headers: { 'Content-Type': 'application/json' },
  });

  return response;
};

// Handle POST request for reservation
app.post('/reserve', async (req, res) => {
  const { count, train_id: trainId } = req.body;

  try {
    const response = await makeReservation(count, trainId);

    if (response.status !== 200) {
      const message = await response.text();
      res.status(500).send(message);
      return;
    }

    const reservation = await response.json();
    res.send(reservation);
  } catch (error) {
    console.error('Error making reservation:', error);
    res.status(500).send('Error making reservation');
  }
});

app.listen(port, () => {
  console.log(`Ticket Office listening on port ${port}`)
})