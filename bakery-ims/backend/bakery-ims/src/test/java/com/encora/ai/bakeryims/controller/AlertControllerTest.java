package com.encora.ai.bakeryims.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;

import com.encora.ai.bakeryims.model.Alert;
import com.encora.ai.bakeryims.repository.AlertRepository;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class AlertControllerTest {

    @InjectMocks
    private AlertController alertController;

    @Mock
    private AlertRepository alertRepository;

    @Test
    public void testUpdateAlertStatusWithExistingAlert() {
        Alert alert = new Alert();
        alert.setId(1L);
        alert.setStatus("oldStatus");
        when(alertRepository.findById(1L)).thenReturn(Optional.of(alert));
        when(alertRepository.save(any(Alert.class))).thenReturn(alert);

        Alert result = alertController.updateAlertStatus(1L, "newStatus");
        assertEquals("newStatus", result.getStatus());
    }

    @Test
    public void testUpdateAlertStatusWithNonExistingAlert() {
        when(alertRepository.findById(1L)).thenReturn(Optional.empty());

        Alert result = alertController.updateAlertStatus(1L, "newStatus");
        assertNull(result);
    }

    @Test
    public void testGetAlertCountWithStatus() {
        Alert alert = new Alert();
        alert.setStatus("status");
        when(alertRepository.count(Example.of(alert))).thenReturn(1L);

        long result = alertController.getAlertCount("status");
        assertEquals(1L, result);
    }

    @Test
    public void testGetAlertCountWithoutStatus() {
        when(alertRepository.count()).thenReturn(2L);

        long result = alertController.getAlertCount(null);
        assertEquals(2L, result);
    }

    @Test
    public void testGetAllAlertsWithStatus() {
        Alert alert = new Alert();
        alert.setStatus("status");
        when(alertRepository.findByStatus("status")).thenReturn(Arrays.asList(alert));

        List<Alert> result = alertController.getAllAlerts("status");
        assertEquals(1, result.size());
        assertEquals(alert, result.get(0));
    }

    @Test
    public void testGetAllAlertsWithoutStatus() {
        Alert alert1 = new Alert();
        alert1.setStatus("status1");
        Alert alert2 = new Alert();
        alert2.setStatus("status2");
        when(alertRepository.findAll()).thenReturn(Arrays.asList(alert1, alert2));

        List<Alert> result = alertController.getAllAlerts(null);
        assertEquals(2, result.size());
    }
}
