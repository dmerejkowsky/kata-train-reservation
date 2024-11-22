const express = require('express')
const morgan = require('morgan')

const port = 8081

const app = express()
app.use(express.json())
app.use(morgan('tiny'))

const trains = require('./trains.json')

app.get("/data_for_train/:trainId", (req, res) => {
  const trainId = req.params.trainId
  const train = trains[trainId]
  if (!train) {
    res.status(404).send(`No such train: ${trainId}`)
    return
  }
  res.send(train)
})

app.post("/reset/:trainId", (req, res) => {
  const trainId = req.params.trainId
  const train = trains[trainId]
  if (!train) {
    res.status(404).send(`No such train: ${trainId}`)
    return
  }
  const { seats } = train

  for (const seatId in seats) {
    const seat = seats[seatId]
    seat.booking_reference = ""
  }
  res.send("ok")
})

app.post("/reserve/", (req, res) => {
  const { booking_reference, train_id, seats } = req.body
  const train = trains[train_id]
  if (!train) {
    res.status(404).send(`No such train: ${train_id}`)
    return
  }

  for (const seatId of seats) {
    const seat = train.seats[seatId]
    const previousBookingReference = seat.booking_reference
    if (previousBookingReference != "" && previousBookingReference != booking_reference) {
      res.status(409).send(`Could not book '${seatId}' with '${booking_reference}' - already booked with '${previousBookingReference}'`)
      return
    }
    seat.booking_reference = booking_reference
  }

  res.send(train)
})

app.listen(port, () => {
  console.log(`Train Data listening on port ${port}`)
}) 
