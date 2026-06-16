package com.File.Pricing.Strategy.System.repository;

import com.File.Pricing.Strategy.System.model.SimulationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SimulationHistoryRepository extends JpaRepository<SimulationHistory, Long> {

    List<SimulationHistory> findTop20ByOrderByCreatedAtDesc();
}
