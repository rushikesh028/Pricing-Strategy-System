import React, { useState, useEffect } from "react";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap-icons/font/bootstrap-icons.css";

import {
  Line,
  Bar,
  Scatter
} from "react-chartjs-2";

import {
  Chart as ChartJS,
  LineElement,
  BarElement,
  PointElement,
  LinearScale,
  CategoryScale,
  Tooltip,
  Legend
} from "chart.js";

ChartJS.register(
  LineElement,
  BarElement,
  PointElement,
  LinearScale,
  CategoryScale,
  Tooltip,
  Legend
);

function App() {

  // ================= STATE =================
  const [scenario, setScenario] = useState({
    demand: "",
    stock: "",
    competitorPrice: ""
  });

  const [result, setResult] = useState({});
  const [history, setHistory] = useState([]);

  // Dummy KPIs (replace with API later)
  const totalRevenue = 85000;
  const avgPrice = 135;

  // ================= HANDLERS =================
  const handleChange = (e) => {
    setScenario({
      ...scenario,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const res = await axios.get(
        "http://localhost:8080/api/pricing/simulate/scenario",
        { params: scenario }
      );

      setResult(res.data);
      fetchHistory();
    } catch (err) {
      console.error("API ERROR", err);
    }
  };

  const fetchHistory = async () => {
    try {
      const res = await axios.get(
        "http://localhost:8080/api/pricing/simulate/history"
      );
      setHistory(res.data);
    } catch (err) {}
  };

  useEffect(() => {
    fetchHistory();
  }, []);

  // ================= CHART DATA =================

  const demandChart = {
    labels: ["Mon", "Tue", "Wed", "Thu", "Fri"],
    datasets: [{
      label: "Demand",
      data: [20, 40, 35, 60, 80],
      borderColor: "#0d6efd",
      backgroundColor: "rgba(13,110,253,0.2)",
      fill: true,
      tension: 0.4
    }]
  };

  const revenueChart = {
    labels: ["Price", "Revenue"],
    datasets: [{
      label: "Analysis",
      data: [
        result.predictedPrice || 0,
        result.expectedRevenue || 0
      ],
      backgroundColor: ["#0d6efd", "#198754"]
    }]
  };

  const scatterData = {
    datasets: [{
      label: "Revenue vs Demand",
      data: [
        { x: 10, y: 2000 },
        { x: 20, y: 5000 },
        { x: 30, y: 9000 },
        { x: 50, y: 15000 }
      ],
      borderColor: "#198754",
      showLine: true
    }]
  };

  // ================= UI =================

  return (
    <div className="container py-4">

      {/* TITLE */}
      <h2 className="text-center fw-bold mb-4">
        🚀 AI Pricing Dashboard
      </h2>

      {/* KPI CARDS */}
      <div className="row g-3 mb-4">

        <div className="col-md-4">
          <div className="card p-3 shadow-sm">
            <h6>Total Revenue</h6>
            <h3 className="text-success">₹ {totalRevenue}</h3>
            <i className="bi bi-cash-stack"></i>
          </div>
        </div>

        <div className="col-md-4">
          <div className="card p-3 shadow-sm">
            <h6>Average Price</h6>
            <h3 className="text-primary">₹ {avgPrice}</h3>
            <i className="bi bi-graph-up"></i>
          </div>
        </div>

        <div className="col-md-4">
          <div className="card p-3 shadow-sm">
            <h6>Demand Trend</h6>
            <Line data={demandChart} />
          </div>
        </div>

      </div>

      {/* SCENARIO SIMULATION */}
      <div className="card p-4 shadow mb-4">
        <h4 className="mb-3">Revenue Simulation</h4>

        <form onSubmit={handleSubmit} className="row g-3">

          <div className="col-md-4">
            <input
              type="number"
              name="demand"
              placeholder="Demand"
              className="form-control"
              onChange={handleChange}
              required
            />
          </div>

          <div className="col-md-4">
            <input
              type="number"
              name="stock"
              placeholder="Stock"
              className="form-control"
              onChange={handleChange}
              required
            />
          </div>

          <div className="col-md-4">
            <input
              type="number"
              name="competitorPrice"
              placeholder="Competitor Price"
              className="form-control"
              onChange={handleChange}
              required
            />
          </div>

          <div className="col-12">
            <button className="btn btn-primary w-100">
              Predict Scenario
            </button>
          </div>

        </form>

        {/* RESULTS */}
        <div className="row mt-4">

          <div className="col-md-4">
            <h6>Predicted Price</h6>
            <h4 className="text-primary">
              ₹ {result.predictedPrice?.toFixed(2) || "-"}
            </h4>
          </div>

          <div className="col-md-4">
            <h6>Expected Revenue</h6>
            <h4 className="text-success">
              ₹ {result.expectedRevenue?.toFixed(2) || "-"}
            </h4>
          </div>

          <div className="col-md-4">
            <h6>Best Price</h6>
            <h4 className="text-warning">
              ₹ {result.bestPriceSuggestion?.toFixed(2) || "-"}
            </h4>
          </div>

        </div>

        {/* AI INSIGHT */}
        <div className="alert alert-success mt-3">
          🔥 {result.explanation || "AI insight will appear here"}
        </div>

      </div>

      {/* CHARTS */}
      <div className="row g-3 mb-4">

        <div className="col-md-6">
          <div className="card p-3 shadow-sm">
            <h5>Revenue Analysis</h5>
            <Bar data={revenueChart} />
          </div>
        </div>

        <div className="col-md-6">
          <div className="card p-3 shadow-sm">
            <h5>Revenue vs Demand</h5>
            <Scatter data={scatterData} />
          </div>
        </div>

      </div>

      {/* HISTORY */}
      <div className="card p-4 shadow">
        <h4>Simulation History</h4>

        <table className="table table-striped mt-3">
          <thead>
            <tr>
              <th>Demand</th>
              <th>Stock</th>
              <th>Competitor</th>
              <th>Price</th>
              <th>Revenue</th>
            </tr>
          </thead>

          <tbody>
            {history.map((h, i) => (
              <tr key={i}>
                <td>{h.demand}</td>
                <td>{h.stock}</td>
                <td>{h.competitorPrice}</td>
                <td>{h.predictedPrice}</td>
                <td>{h.expectedRevenue}</td>
              </tr>
            ))}
          </tbody>
        </table>

      </div>

    </div>
  );
}

export default App;