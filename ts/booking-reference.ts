import express from 'express'

const app = express()
const port = 8082

class Counter {
    private _count: number

    constructor() {
        this._count = 123456789
    }

    increment() {
        this._count += 1
    }

    value() { return this._count.toString() }
}

const counter = new Counter()

app.get("/booking_reference", (req, res) => {
    counter.increment()
    res.send(counter.value())
})


app.listen(port, () => {
    console.log(`Booking Reference listening on port ${port}`)
}) 