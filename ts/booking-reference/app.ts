import express from 'express'
import morgan from 'morgan'
import {Counter} from './counter.js'

const port = 8082

const app = express()
app.use(morgan('tiny'))

const counter = new Counter()

app.get("/booking_reference", (req, res) => {
  counter.increment()
  res.send(counter.value())
})


app.listen(port, () => {
  console.log(`Booking Reference listening on port ${port}`)
}) 

