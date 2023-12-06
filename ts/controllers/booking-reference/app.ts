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

export const getBookingReference = async () => {
  counter.increment();
  return counter.value();
};
