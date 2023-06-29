import test from 'tape'
import fetch, { Body } from 'node-fetch'

test('booking four seats from empty train', async (t) => {
  // Reset the train
  const trainId = 'express_2000'
  let response = await fetch(
    `http://127.0.0.1:8081/reset/${trainId}`,
    { method: 'POST' }
  )
  const text = await response.text()
  t.equal(response.status, 200, text)


  // Try to make a reservation
  const payload = { train_id: trainId, count: 4 }
  response = await fetch(
    `http://127.0.0.1:8083/reserve`,
    {
      method: 'POST',
      body: JSON.stringify(payload),
      headers: { 'Content-Type': 'application/json' }
    }
  )
  t.equal(response.status, 200)
  const reservation = await response.json()

  // Check which seats have been reserved
  t.equal(reservation.train_id, trainId)
  t.deepEqual(reservation.seats, ['1A', '2A', '3A', '4A'])
  t.end()
})


test('booking four additional seats', async (t) => {
  // Reset the train
  const trainId = 'express_2000'
  let response = await fetch(
    `http://127.0.0.1:8081/reset/${trainId}`,
    { method: 'POST' }
  )
  let text = await response.text()
  t.equal(response.status, 200, text)


  // Make a first reservation for 4 seats
  let payload = { train_id: trainId, count: 4 }
  response = await fetch(
    `http://127.0.0.1:8083/reserve`,
    {
      method: 'POST',
      body: JSON.stringify(payload),
      headers: { 'Content-Type': 'application/json' }
    }
  )
  t.equal(response.status, 200)

  // Make a second reservation with 4 seats

  const reservation = await response.json()
  payload = { train_id: trainId, count: 4 }
  response = await fetch(
    `http://127.0.0.1:8083/reserve`,
    {
      method: 'POST',
      body: JSON.stringify(payload),
      headers: { 'Content-Type': 'application/json' }
    }
  )
  text = await response.text()
  // TODO: this fails with 500 because train-data returns 409
  t.equal(response.status, 200, text)
})

