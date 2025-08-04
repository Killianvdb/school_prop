package com.example.demo.Controller;

import com.example.demo.Entities.Item;
import com.example.demo.Service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    // GET all items
    @GetMapping
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }

    // GET items by category (e.g., ?category=lampen)
    @GetMapping("/search")
    public List<Item> getByCategory(@RequestParam(value = "category", required = false) String category) {
        if (category == null || category.isEmpty()) {
            return itemService.getAllItems();
        }
        return itemService.getItemsByCategory(category);
    }

    // POST create new item
    @PostMapping("/add")
    public ResponseEntity<Item> addItem(@RequestBody Item item) {
        Item saved = itemService.addItem(item);
        return ResponseEntity.ok(saved);
    }
}
