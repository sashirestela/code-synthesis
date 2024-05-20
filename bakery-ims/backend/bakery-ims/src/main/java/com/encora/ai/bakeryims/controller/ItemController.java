package com.encora.ai.bakeryims.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.encora.ai.bakeryims.model.Alert;
import com.encora.ai.bakeryims.model.Item;
import com.encora.ai.bakeryims.repository.AlertRepository;
import com.encora.ai.bakeryims.repository.ItemRepository;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private AlertRepository alertRepository;

    @GetMapping("/{id}")
    public Item getItemById(@PathVariable Long id) {
        return itemRepository.findById(id).orElse(null);
    }

    @PostMapping
    @Transactional
    public Item createItem(@RequestBody Item item) {
        Item newItem = itemRepository.save(item);

        // Check if quantity is zero and create an alert
        if (item.getQuantity() == 0) {
            Alert alert = new Alert();
            alert.setItem(newItem);
            alert.setStatus("pending");
            alert.setMessage("The item stock level is low, take actions to replenish it.");
            alertRepository.save(alert);
        }

        return newItem;
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        itemRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    @Transactional
    public Item updateItem(@PathVariable Long id, @RequestBody Item item) {
        // Check if item exists
        Item existingItem = itemRepository.findById(id).orElse(null);
        if (existingItem == null) {
            return null;
        }

        // Update item
        existingItem.setName(item.getName());
        existingItem.setDescription(item.getDescription());
        existingItem.setQuantity(item.getQuantity());
        existingItem.setPrice(item.getPrice());

        // Check if quantity is zero and create an alert
        if (item.getQuantity() == 0) {
            Alert alert = new Alert();
            alert.setItem(existingItem);
            alert.setStatus("pending");
            alert.setMessage("The item stock level is low, take actions to replenish it.");
            alertRepository.save(alert);
        }

        return itemRepository.save(existingItem);
    }

    @GetMapping
    public List<Item> getAllItems(@RequestParam(required = false) String search) {
        if (search != null) {
            // Search for items
            return itemRepository.findByNameContainingOrDescriptionContaining(search, search);
        } else {
            // Return all items
            return itemRepository.findAll();
        }
    }

}
