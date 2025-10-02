const express = require("express");
const app = express();
const PORT = process.env.PORT || 10000;

// Middleware
app.use(express.json());

// In-memory orders
let orders = [];
let orderIdCounter = 1;

app.get("/", (req, res) => {
  res.json({ message: "Hello from your Render API!" });
});

// Place a new order - UPDATED TO MATCH APP
app.post("/orders", (req, res) => {
  const order = req.body;
  order.id = orderIdCounter.toString();
  order.status = "ORDER_RECEIVED"; // Changed from "PENDING"
  order.createdAt = new Date().toISOString();
  order.estimatedDelivery = new Date(Date.now() + 45 * 60000).toISOString();
  order.statusHistory = [
    { 
      status: "ORDER_RECEIVED", 
      timestamp: new Date().toISOString(), 
      message: "Order received and confirmed" 
    }
  ];
  
  orders.push(order);
  orderIdCounter++;
  console.log("New order placed:", order);
  res.json(order);
});

// Get orders by user ID - UPDATED
app.get("/orders/:userId", (req, res) => {
  const userOrders = orders.filter(o => o.userId === req.params.userId);
  console.log(`Returning ${userOrders.length} orders for user ${req.params.userId}`);
  res.json(userOrders);
});

// Get specific order by ID
app.get("/order/:orderId", (req, res) => {
  const order = orders.find(o => o.id === req.params.orderId);
  if (!order) return res.status(404).json({ message: "Order not found" });
  res.json(order);
});

// Update order status - UPDATED
app.patch("/orders/:orderId/status", (req, res) => {
  const { status, message } = req.body;
  const order = orders.find(o => o.id === req.params.orderId);
  
  if (!order) return res.status(404).json({ message: "Order not found" });
  
  order.status = status;
  
  // Add to status history
  order.statusHistory.push({
    status: status,
    timestamp: new Date().toISOString(),
    message: message || getStatusMessage(status)
  });
  
  console.log(`Order ${order.id} status updated to: ${status}`);
  res.json(order);
});

// Get all orders
app.get("/orders", (req, res) => {
  res.json(orders);
});

// Helper function for status messages
function getStatusMessage(status) {
  const messages = {
    "ORDER_RECEIVED": "Order received and confirmed",
    "PREPARING": "Kitchen is preparing your food", 
    "READY_FOR_PICKUP": "Your order is ready for pickup",
    "OUT_FOR_DELIVERY": "Driver is on the way with your order",
    "DELIVERED": "Order delivered! Enjoy your meal!",
    "CANCELLED": "Order has been cancelled"
  };
  return messages[status] || "Order status updated";
}

// Start server
app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});

// Add this to your index.js for testing status progression
function simulateStatusProgression() {
    orders.forEach(order => {
        if (order.status === "ORDER_RECEIVED") {
            // 30% chance to move to PREPARING
            if (Math.random() < 0.3) {
                order.status = "PREPARING";
                order.statusHistory.push({
                    status: "PREPARING",
                    timestamp: new Date().toISOString(),
                    message: "Kitchen started preparing your order"
                });
            }
        } else if (order.status === "PREPARING") {
            // 25% chance to move to READY_FOR_PICKUP
            if (Math.random() < 0.25) {
                order.status = "READY_FOR_PICKUP";
                order.statusHistory.push({
                    status: "READY_FOR_PICKUP", 
                    timestamp: new Date().toISOString(),
                    message: "Your order is ready for delivery"
                });
            }
        } else if (order.status === "READY_FOR_PICKUP") {
            // 20% chance to move to OUT_FOR_DELIVERY
            if (Math.random() < 0.2) {
                order.status = "OUT_FOR_DELIVERY";
                order.statusHistory.push({
                    status: "OUT_FOR_DELIVERY",
                    timestamp: new Date().toISOString(), 
                    message: "Driver is on the way with your order"
                });
            }
        } else if (order.status === "OUT_FOR_DELIVERY") {
            
            if (Math.random() < 0.15) {
                order.status = "DELIVERED";
                order.statusHistory.push({
                    status: "DELIVERED",
                    timestamp: new Date().toISOString(),
                    message: "Order delivered! Enjoy your meal!"
                });
            }
        }
    });
}


setInterval(simulateStatusProgression, 10000);

app.get("/orders/:userId", (req, res) => {
    simulateStatusProgression(); // Update statuses before returning
    const userOrders = orders.filter(o => o.userId === req.params.userId);
    res.json(userOrders);
});