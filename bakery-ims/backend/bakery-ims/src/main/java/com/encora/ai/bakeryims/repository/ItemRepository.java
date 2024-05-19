package com.encora.ai.bakeryims.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.encora.ai.bakeryims.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByNameContainingOrDescriptionContaining(String name, String description);

}
