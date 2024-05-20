package com.encora.ai.bakeryims.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.encora.ai.bakeryims.model.Alert;
import com.encora.ai.bakeryims.repository.AlertRepository;

@RestController
@RequestMapping("/alerts")
public class AlertController {

    @Autowired
    private AlertRepository alertRepository;

    @PatchMapping("/{id}")
    public Alert updateAlertStatus(@PathVariable Long id, @RequestParam String status) {
        // Check if alert exists
        Alert existingAlert = alertRepository.findById(id).orElse(null);
        if (existingAlert == null) {
            return null;
        }

        // Update alert status
        existingAlert.setStatus(status);

        return alertRepository.save(existingAlert);
    }

    @GetMapping("/count")
    public long getAlertCount(@RequestParam(required = false) String status) {
        if (status != null) {
            // Create an example alert with the given status
            Alert exampleAlert = new Alert();
            exampleAlert.setStatus(status);

            // Count the alerts that match the example
            return alertRepository.count(Example.of(exampleAlert));
        } else {
            // Count all alerts
            return alertRepository.count();
        }
    }

    @GetMapping
    public List<Alert> getAllAlerts(@RequestParam(required = false) String status) {
        if (status != null) {
            // Search for alerts
            return alertRepository.findByStatus(status);
        } else {
            // Return all alerts
            return alertRepository.findAll();
        }
    }

}