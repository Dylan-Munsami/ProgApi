const express = require("express");
const app = express();
const PORT = process.env.PORT || 10000;

// Middleware
app.use(express.json());

// Example GET endpoint
app.get("/", (req, res) => {
  res.json({ message: "Hello from your Render API!" });
});

// Example POST endpoint
app.post("/echo", (req, res) => {
  res.json({ youSent: req.body });
});

// Mock data
let restaurants = [
  {
    id: "1",
    name: "Mock Pizza Place",
    menu: [{ id: "1", name: "Margherita", price: 50 }]
  },
  {
    id: "2",
    name: "Mock Burger Joint",
    menu: [{ id: "2", name: "Cheeseburger", price: 40 }]
  }
];

let orders = [];

// GET all restaurants
app.get("/restaurants", (req, res) => {
  res.json(restaurants);
});

// POST a new order
app.post("/orders", (req, res) => {
  const order = req.body;
  order.id = (orders.length + 1).toString(); // generate simple ID
  orders.push(order);
  res.json(order);
});

// GET orders by userId
app.get("/orders/:userId", (req, res) => {
  const userOrders = orders.filter(o => o.userId === req.params.userId);
  res.json(userOrders);
});

// Start server
app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});
