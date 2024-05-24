package com.encora.ai.bakeryims.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

import com.encora.ai.bakeryims.model.Alert;
import com.encora.ai.bakeryims.model.Item;
import com.encora.ai.bakeryims.repository.AlertRepository;
import com.encora.ai.bakeryims.repository.ItemRepository;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ItemControllerTest {

    @InjectMocks
    private ItemController itemController;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private AlertRepository alertRepository;

    @Test
    public void testGetItemById() {
        Item item = new Item();
        item.setId(1L);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        Item result = itemController.getItemById(1L);
        assertEquals(item, result);
    }

    @Test
    public void testCreateItemWithZeroQuantity() {
        Item item = new Item();
        item.setQuantity(0);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        Item result = itemController.createItem(item);
        verify(alertRepository, times(1)).save(any(Alert.class));
        assertEquals(item, result);
    }

    @Test
    public void testCreateItemWithNonZeroQuantity() {
        Item item = new Item();
        item.setQuantity(10);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        Item result = itemController.createItem(item);
        verify(alertRepository, times(0)).save(any(Alert.class));
        assertEquals(item, result);
    }

    @Test
    public void testDeleteItem() {
        doNothing().when(itemRepository).deleteById(1L);
        itemController.deleteItem(1L);
        verify(itemRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testUpdateItemWithZeroQuantity() {
        Item item = new Item();
        item.setId(1L);
        item.setQuantity(0);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        Item result = itemController.updateItem(1L, item);
        verify(alertRepository, times(1)).save(any(Alert.class));
        assertEquals(item, result);
    }

    @Test
    public void testUpdateItemWithNonZeroQuantity() {
        Item item = new Item();
        item.setId(1L);
        item.setQuantity(10);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        Item result = itemController.updateItem(1L, item);
        verify(alertRepository, times(0)).save(any(Alert.class));
        assertEquals(item, result);
    }

    @Test
    public void testGetAllItemsWithSearch() {
        Item item1 = new Item();
        item1.setName("bread");
        Item item2 = new Item();
        item2.setName("cake");
        when(itemRepository.findByNameContainingOrDescriptionContaining("bread", "bread")).thenReturn(Arrays.asList(item1));

        List<Item> result = itemController.getAllItems("bread");
        assertEquals(1, result.size());
        assertEquals(item1, result.get(0));
    }

    @Test
    public void testGetAllItemsWithoutSearch() {
        Item item1 = new Item();
        item1.setName("bread");
        Item item2 = new Item();
        item2.setName("cake");
        when(itemRepository.findAll()).thenReturn(Arrays.asList(item1, item2));

        List<Item> result = itemController.getAllItems(null);
        assertEquals(2, result.size());
    }
}
