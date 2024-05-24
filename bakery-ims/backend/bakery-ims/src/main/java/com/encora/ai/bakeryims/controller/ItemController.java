package com.encora.ai.bakeryims.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.encora.ai.bakeryims.model.Alert;
import com.encora.ai.bakeryims.model.Item;
import com.encora.ai.bakeryims.repository.AlertRepository;
import com.encora.ai.bakeryims.repository.ItemRepository;

import java.util.List;

@RestController
@RequestMapping("/items")
@Tag(name = "Item Controller", description = "Controller for managing items")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private AlertRepository alertRepository;

    @Operation(summary = "Get an item by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the item",
            content = { @Content(mediaType = "application/json",
            schema = @Schema(implementation = Item.class)) }),
        @ApiResponse(responseCode = "404", description = "Item not found",
            content = @Content)
    })
    @GetMapping("/{id}")
    public Item getItemById(@PathVariable Long id) {
        return itemRepository.findById(id).orElse(null);
    }

    @Operation(summary = "Create a new item")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Item created",
            content = { @Content(mediaType = "application/json",
            schema = @Schema(implementation = Item.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid input",
            content = @Content)
    })
    @PostMapping
    @Transactional
    public Item createItem(@RequestBody Item item) {
        Item newItem = itemRepository.save(item);

        if (item.getQuantity() == 0) {
            Alert alert = new Alert();
            alert.setItem(newItem);
            alert.setStatus("pending");
            alert.setMessage("The item stock level is low, take actions to replenish it.");
            alertRepository.save(alert);
        }

        return newItem;
    }

    @Operation(summary = "Delete an item by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item deleted"),
        @ApiResponse(responseCode = "404", description = "Item not found",
            content = @Content)
    })
    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        itemRepository.deleteById(id);
    }

    @Operation(summary = "Update an existing item")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item updated",
            content = { @Content(mediaType = "application/json",
            schema = @Schema(implementation = Item.class)) }),
        @ApiResponse(responseCode = "404", description = "Item not found",
            content = @Content)
    })
    @PutMapping("/{id}")
    @Transactional
    public Item updateItem(@PathVariable Long id, @RequestBody Item item) {
        Item existingItem = itemRepository.findById(id).orElse(null);
        if (existingItem == null) {
            return null;
        }

        existingItem.setName(item.getName());
        existingItem.setDescription(item.getDescription());
        existingItem.setQuantity(item.getQuantity());
        existingItem.setPrice(item.getPrice());

        if (item.getQuantity() == 0) {
            Alert alert = new Alert();
            alert.setItem(existingItem);
            alert.setStatus("pending");
            alert.setMessage("The item stock level is low, take actions to replenish it.");
            alertRepository.save(alert);
        }

        return itemRepository.save(existingItem);
    }

    @Operation(summary = "Get all items or search items by name or description")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found items",
            content = { @Content(mediaType = "application/json",
            schema = @Schema(implementation = Item.class)) }),
        @ApiResponse(responseCode = "404", description = "Items not found",
            content = @Content)
    })
    @GetMapping
    public List<Item> getAllItems(@RequestParam(required = false) String search) {
        if (search != null) {
            return itemRepository.findByNameContainingOrDescriptionContaining(search, search);
        } else {
            return itemRepository.findAll();
        }
    }

}
