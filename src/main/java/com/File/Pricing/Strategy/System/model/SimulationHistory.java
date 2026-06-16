package com.File.Pricing.Strategy.System.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class SimulationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt;
    private double demand;
    private double stock;
    private double competitorPrice;
    private double predictedPrice;
    private double expectedRevenue;
    private String explanation;
    private double bestPriceSuggestion;
    private double bestExpectedRevenue;

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public double getDemand() {
        return demand;
    }

    public void setDemand(double demand) {
        this.demand = demand;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public double getCompetitorPrice() {
        return competitorPrice;
    }

    public void setCompetitorPrice(double competitorPrice) {
        this.competitorPrice = competitorPrice;
    }

    public double getPredictedPrice() {
        return predictedPrice;
    }

    public void setPredictedPrice(double predictedPrice) {
        this.predictedPrice = predictedPrice;
    }

    public double getExpectedRevenue() {
        return expectedRevenue;
    }

    public void setExpectedRevenue(double expectedRevenue) {
        this.expectedRevenue = expectedRevenue;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public double getBestPriceSuggestion() {
        return bestPriceSuggestion;
    }

    public void setBestPriceSuggestion(double bestPriceSuggestion) {
        this.bestPriceSuggestion = bestPriceSuggestion;
    }

    public double getBestExpectedRevenue() {
        return bestExpectedRevenue;
    }

    public void setBestExpectedRevenue(double bestExpectedRevenue) {
        this.bestExpectedRevenue = bestExpectedRevenue;
    }
}
