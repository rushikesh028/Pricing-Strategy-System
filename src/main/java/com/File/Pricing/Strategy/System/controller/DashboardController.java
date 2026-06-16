package com.File.Pricing.Strategy.System.controller;

import com.File.Pricing.Strategy.System.model.Product;
import com.File.Pricing.Strategy.System.service.ProductService;
import com.File.Pricing.Strategy.System.service.PricingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    @Autowired
    private ProductService productService;

    @Autowired
    private PricingService pricingService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Product> products = productService.getAllProducts();

        double totalRevenue = products.stream()
                .mapToDouble(p -> p.getBasePrice() * p.getDemand())
                .sum();

        double avgPrice = products.stream()
                .mapToDouble(Product::getBasePrice)
                .average()
                .orElse(0.0);

        List<Product> topProducts = products.stream()
                .sorted(Comparator.comparingDouble((Product p) -> p.getBasePrice() * p.getDemand()).reversed())
                .limit(5)
                .toList();

        List<String> demandTrendLabels = products.stream()
                .sorted(Comparator.comparing(Product::getId))
                .map(Product::getName)
                .collect(Collectors.toList());

        List<Double> demandTrendValues = products.stream()
                .sorted(Comparator.comparing(Product::getId))
                .map(Product::getDemand)
                .collect(Collectors.toList());

        List<Map<String, Double>> revenueDemandCurvePoints = products.stream()
                .sorted(Comparator.comparingDouble(Product::getDemand))
                .map(p -> {
                    Map<String, Double> point = new HashMap<>();
                    point.put("x", p.getDemand());
                    point.put("y", p.getBasePrice() * p.getDemand());
                    return point;
                })
                .collect(Collectors.toList());

        model.addAttribute("products", products);
        model.addAttribute("topProducts", topProducts);
        model.addAttribute("demandTrendLabels", demandTrendLabels);
        model.addAttribute("demandTrendValues", demandTrendValues);
        model.addAttribute("revenueDemandCurvePoints", revenueDemandCurvePoints);
        model.addAttribute("simulationHistory", pricingService.getRecentSimulationHistory());
        model.addAttribute("totalRevenue", formatNumber(totalRevenue));
        model.addAttribute("avgPrice", formatNumber(avgPrice));
        return "dashboard";
    }

    @GetMapping("/price/{id}")
    public String getPrice(@PathVariable Long id, Model model) {
        Map<String, Object> result = pricingService.getPriceWithExplanation(id);
        model.addAttribute("result", result);
        return "result";
    }

    private String formatNumber(double value) {
        DecimalFormat formatter = new DecimalFormat("#,##0.00");
        return formatter.format(value);
    }
}
