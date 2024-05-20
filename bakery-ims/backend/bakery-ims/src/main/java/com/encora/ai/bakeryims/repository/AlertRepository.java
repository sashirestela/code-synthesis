package com.encora.ai.bakeryims.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.encora.ai.bakeryims.model.Alert;

public interface AlertRepository extends JpaRepository<Alert, Long> {

    List<Alert> findByStatus(String status);

    List<Alert> findByItemIdAndStatus(Long id, String status);

}
