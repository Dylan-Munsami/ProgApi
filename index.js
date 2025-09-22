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

// Start server
app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});
