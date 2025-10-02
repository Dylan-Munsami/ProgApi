const express = require("express");
const app = express();
const PORT = process.env.PORT || 10000;

// Middleware
app.use(express.json());

// In-memory orders
let orders = [];


app.get("/", (req, res) => {
  res.json({ message: "Hello from your Render wjghqefhwefvbv API!" });
});

// Place a new order
app.post("/orders", (req, res) => {
  const order = req.body;
  order.id = (orders.length + 1).toString();
  order.status = "PENDING"; // default status
  orders.push(order);
  res.json(order);
});

// Get orders by user ID
app.get("/orders/:userId", (req, res) => {
  const userOrders = orders.filter(o => o.userId === req.params.userId);
  res.json(userOrders);
});

// Get all orders
app.get("/orders", (req, res) => {
  res.json(orders);
});

// Update order status
app.patch("/orders/:orderId/status", (req, res) => {
  const { status } = req.body;
  const order = orders.find(o => o.id === req.params.orderId);
  if (!order) return res.status(404).json({ message: "Order not found" });
  order.status = status;
  res.json(order);
});

// Start server
app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});
