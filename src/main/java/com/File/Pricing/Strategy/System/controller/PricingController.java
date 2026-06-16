package com.File.Pricing.Strategy.System.controller;

import com.File.Pricing.Strategy.System.service.PricingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/pricing")
@CrossOrigin
public class PricingController {

    @Autowired
    private PricingService pricingService;

    @GetMapping("/{productId:\\d+}")
    public ResponseEntity<?> getPrice(@PathVariable Long productId) {
        return ResponseEntity.ok(pricingService.getPriceWithExplanation(productId));
    }

    @GetMapping("/simulate/revenue")
    public ResponseEntity<?> simulateRevenue(
            @RequestParam Long productId,
            @RequestParam double price) {

        double revenue = pricingService.simulateRevenue(productId, price);
        return ResponseEntity.ok(
                Map.of(
                        "productId", productId,
                        "inputPrice", price,
                        "estimatedRevenue", revenue
                )
        );
    }

    @GetMapping("/simulate/scenario")
    public ResponseEntity<?> simulateScenario(
            @RequestParam double demand,
            @RequestParam double stock,
            @RequestParam double competitorPrice) {

        return ResponseEntity.ok(
                pricingService.simulateScenario(demand, stock, competitorPrice)
        );
    }

    @GetMapping("/simulate/history")
    public ResponseEntity<?> simulationHistory() {
        return ResponseEntity.ok(pricingService.getRecentSimulationHistory());
    }

    @GetMapping("/strategy")
    public ResponseEntity<?> getPriceWithStrategy(
            @RequestParam Long productId,
            @RequestParam String strategy) {

        return ResponseEntity.ok(
                pricingService.getPriceWithStrategy(productId, strategy)
        );
    }
}
