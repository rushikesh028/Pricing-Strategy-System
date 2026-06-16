package com.File.Pricing.Strategy.System.service;

import com.File.Pricing.Strategy.System.ml.WekaPricingModel;
import com.File.Pricing.Strategy.System.model.Product;
import com.File.Pricing.Strategy.System.model.SimulationHistory;
import com.File.Pricing.Strategy.System.repository.ProductRepository;
import com.File.Pricing.Strategy.System.repository.SimulationHistoryRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PricingService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WekaPricingModel model;

    @Autowired
    private SimulationHistoryRepository simulationHistoryRepository;

    @PostConstruct
    public void init() {
        model.trainModel();
    }

    public Map<String, Object> getPriceWithExplanation(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        double price = predictPrice(productId);
        String explanation = explainPrice(product);

        Map<String, Object> result = new HashMap<>();
        result.put("productId", productId);
        result.put("price", price);
        result.put("explanation", explanation);

        return result;
    }

    public double predictPrice(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return model.predict(
                product.getDemand(),
                product.getCompetitorPrice(),
                product.getStock()
        );
    }

    public String explainPrice(Product product) {
        if (product.getDemand() > 80) {
            return "High demand -> price increased";
        } else if (product.getStock() < 20) {
            return "Low stock -> price increased";
        } else {
            return "Stable market conditions -> standard pricing applied.";
        }
    }

    public double simulateRevenue(Long productId, double price) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return price * product.getDemand();
    }

    public Map<String, Object> simulateScenario(double demand, double stock, double competitorPrice) {
        double predictedPrice = model.predict(demand, competitorPrice, stock);
        double expectedRevenue = predictedPrice * demand;
        String explanation = explainScenario(demand, stock, competitorPrice, predictedPrice);

        Map<String, Double> bestSuggestion = calculateBestPriceSuggestion(demand, predictedPrice);
        double bestPriceSuggestion = bestSuggestion.get("price");
        double bestExpectedRevenue = bestSuggestion.get("revenue");

        SimulationHistory history = new SimulationHistory();
        history.setCreatedAt(LocalDateTime.now());
        history.setDemand(demand);
        history.setStock(stock);
        history.setCompetitorPrice(competitorPrice);
        history.setPredictedPrice(predictedPrice);
        history.setExpectedRevenue(expectedRevenue);
        history.setExplanation(explanation);
        history.setBestPriceSuggestion(bestPriceSuggestion);
        history.setBestExpectedRevenue(bestExpectedRevenue);
        simulationHistoryRepository.save(history);

        Map<String, Object> result = new HashMap<>();
        result.put("demand", demand);
        result.put("stock", stock);
        result.put("competitorPrice", competitorPrice);
        result.put("predictedPrice", predictedPrice);
        result.put("expectedRevenue", expectedRevenue);
        result.put("explanation", explanation);
        result.put("bestPriceSuggestion", bestPriceSuggestion);
        result.put("bestExpectedRevenue", bestExpectedRevenue);
        return result;
    }

    public List<Map<String, Object>> getRecentSimulationHistory() {
        List<SimulationHistory> records = simulationHistoryRepository.findTop20ByOrderByCreatedAtDesc();
        List<Map<String, Object>> response = new ArrayList<>();

        for (SimulationHistory record : records) {
            Map<String, Object> row = new HashMap<>();
            row.put("createdAt", record.getCreatedAt());
            row.put("demand", record.getDemand());
            row.put("stock", record.getStock());
            row.put("competitorPrice", record.getCompetitorPrice());
            row.put("predictedPrice", record.getPredictedPrice());
            row.put("expectedRevenue", record.getExpectedRevenue());
            row.put("explanation", record.getExplanation());
            row.put("bestPriceSuggestion", record.getBestPriceSuggestion());
            row.put("bestExpectedRevenue", record.getBestExpectedRevenue());
            response.add(row);
        }

        return response;
    }

    public Map<String, Object> getPriceWithStrategy(Long productId, String strategy) {
        double basePrice = predictPrice(productId);
        double finalPrice = applyStrategy(basePrice, strategy);

        Map<String, Object> result = new HashMap<>();
        result.put("productId", productId);
        result.put("basePrice", basePrice);
        result.put("strategy", strategy.toUpperCase());
        result.put("finalPrice", finalPrice);

        return result;
    }

    public double applyStrategy(double price, String strategy) {
        switch (strategy.toUpperCase()) {
            case "PROFIT":
                return price * 1.2;
            case "PENETRATION":
                return price * 0.9;
            case "COMPETITIVE":
                return price;
            default:
                return price;
        }
    }

    private String explainScenario(double demand, double stock, double competitorPrice, double predictedPrice) {
        String demandNote = demand > 80 ? "High demand supports stronger pricing." : "Demand is moderate/low, so pricing remains balanced.";
        String stockNote = stock < 20 ? "Low stock pushes price upward." : "Healthy stock limits aggressive markup.";
        String competitorNote = predictedPrice > competitorPrice
                ? "Model recommends pricing above competitor level."
                : "Model keeps pricing near/below competitor level.";

        return demandNote + " " + stockNote + " " + competitorNote;
    }

    private Map<String, Double> calculateBestPriceSuggestion(double demand, double predictedPrice) {
        double[] factors = {0.90, 1.00, 1.10, 1.20};
        double bestPrice = predictedPrice;
        double bestRevenue = predictedPrice * demand;

        for (double factor : factors) {
            double candidatePrice = predictedPrice * factor;
            double elasticity = 0.6;
            double demandMultiplier = Math.max(0.10, 1.0 - elasticity * (factor - 1.0));
            double adjustedDemand = demand * demandMultiplier;
            double revenue = candidatePrice * adjustedDemand;

            if (revenue > bestRevenue) {
                bestRevenue = revenue;
                bestPrice = candidatePrice;
            }
        }

        Map<String, Double> result = new HashMap<>();
        result.put("price", bestPrice);
        result.put("revenue", bestRevenue);
        return result;
    }
}
