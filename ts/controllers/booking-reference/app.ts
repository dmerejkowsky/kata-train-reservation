import express from "express";
const router = express.Router();
class Counter {
  private _count: number;

  constructor() {
    this._count = 123456789;
  }

  increment() {
    this._count += 1;
  }

  value() {
    return this._count.toString();
  }
}

const counter = new Counter();

router.get("/booking_reference", (req, res) => {
  counter.increment();
  res.send(counter.value());
});

module.exports = router;
