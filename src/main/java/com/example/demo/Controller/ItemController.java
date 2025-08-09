package com.example.demo.Controller;

import com.example.demo.Entities.Item;
import com.example.demo.Repository.ItemRepository;
import com.example.demo.Service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
    // GET all items
    @GetMapping(produces = "application/json")
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }


    // GET items by category (e.g., ?category=lampen)
    @GetMapping("/search")
    public List<Item> getByCategory(@RequestParam(value = "category", required = false) String category) {
        if (category == null || category.isEmpty()) {
            return itemRepository.findAll();
        }
        return itemRepository.findAllByCategory(category);
    }

    // POST create new item
    @PostMapping("/add")
    public ResponseEntity<Item> addItem(@RequestBody Item item) {
        Item saved = itemRepository.save(item);
        return ResponseEntity.ok(saved);
    }
    @GetMapping("/available")
    public List<Item> getAvailableItems() {
        return itemRepository.findAllByAvailable(true);
    }


}
