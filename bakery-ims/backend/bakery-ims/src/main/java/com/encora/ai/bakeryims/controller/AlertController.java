package com.encora.ai.bakeryims.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;

import com.encora.ai.bakeryims.model.Alert;
import com.encora.ai.bakeryims.repository.AlertRepository;

import java.util.List;

@RestController
@RequestMapping("/alerts")
@Tag(name = "Alert Controller", description = "Controller for managing alerts")
public class AlertController {

    @Autowired
    private AlertRepository alertRepository;

    @Operation(summary = "Update alert status by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Alert status updated",
            content = { @Content(mediaType = "application/json",
            schema = @Schema(implementation = Alert.class)) }),
        @ApiResponse(responseCode = "404", description = "Alert not found",
            content = @Content)
    })
    @PatchMapping("/{id}")
    public Alert updateAlertStatus(@PathVariable Long id, @RequestParam String status) {
        Alert existingAlert = alertRepository.findById(id).orElse(null);
        if (existingAlert == null) {
            return null;
        }

        existingAlert.setStatus(status);
        return alertRepository.save(existingAlert);
    }

    @Operation(summary = "Get alert count by status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Count of alerts",
            content = { @Content(mediaType = "application/json",
            schema = @Schema(implementation = Long.class)) })
    })
    @GetMapping("/count")
    public long getAlertCount(@RequestParam(required = false) String status) {
        if (status != null) {
            Alert exampleAlert = new Alert();
            exampleAlert.setStatus(status);
            return alertRepository.count(Example.of(exampleAlert));
        } else {
            return alertRepository.count();
        }
    }

    @Operation(summary = "Get all alerts or search alerts by status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found alerts",
            content = { @Content(mediaType = "application/json",
            schema = @Schema(implementation = Alert.class)) }),
        @ApiResponse(responseCode = "404", description = "Alerts not found",
            content = @Content)
    })
    @GetMapping
    public List<Alert> getAllAlerts(@RequestParam(required = false) String status) {
        if (status != null) {
            return alertRepository.findByStatus(status);
        } else {
            return alertRepository.findAll();
        }
    }

}
