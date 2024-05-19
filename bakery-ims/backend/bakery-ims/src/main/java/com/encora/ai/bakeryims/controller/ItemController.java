package com.encora.ai.bakeryims.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.encora.ai.bakeryims.model.Item;
import com.encora.ai.bakeryims.repository.ItemRepository;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemRepository repository;

    @GetMapping("/{id}")
    public Item getItemById(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @PostMapping
    public Item createItem(@RequestBody Item item) {
        return repository.save(item);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @PutMapping("/{id}")
    public Item updateItem(@PathVariable Long id, @RequestBody Item item) {
        // Check if item exists
        Item existingItem = repository.findById(id).orElse(null);
        if (existingItem == null) {
            return null;
        }

        // Update item
        existingItem.setName(item.getName());
        existingItem.setDescription(item.getDescription());
        existingItem.setQuantity(item.getQuantity());
        existingItem.setPrice(item.getPrice());
        
        return repository.save(existingItem);
    }

    @GetMapping
    public List<Item> getAllItems(@RequestParam(required = false) String search) {
        if (search != null) {
            // Search for items
            return repository.findByNameContainingOrDescriptionContaining(search, search);
        } else {
            // Return all items
            return repository.findAll();
        }
    }

}
