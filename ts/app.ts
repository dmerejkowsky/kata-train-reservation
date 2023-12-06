import express from "express";

const app = express();
app.enable("trust proxy");
app.use(express.json({ limit: "10mb" }));
app.use(express.urlencoded({ extended: false, limit: "10mb" }));
const PORT = 8080;

// Routes
app.use("/booking-reference", require("./controllers/booking-reference/app"));
app.use("/ticket-office", require("./controllers/ticket-office/app"));
app.use("/train-data", require("./controllers/train-data/app"));

app.get("/", (req, res) => {
  res.send("Hello World");
});

app.listen(PORT, () => {
  console.log(`Ticket Office listening on port ${PORT}`);
});
