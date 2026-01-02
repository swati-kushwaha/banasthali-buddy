package com.banasthali.backend.controller;

import com.banasthali.backend.dto.ItemRequest;
import com.banasthali.backend.dto.ItemResponse;
import com.banasthali.backend.model.User;
import com.banasthali.backend.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/items")
@Tag(name = "Items", description = "Item listing and management endpoints")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Post a new item", description = "Creates a new item listing with image upload. Requires authentication.")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> createItem(
            @Parameter(description = "Item title") @RequestParam("title") String title,
            @Parameter(description = "Item description") @RequestParam("description") String description,
            @Parameter(description = "Item price (must be positive)") @RequestParam("price") BigDecimal price,
            @Parameter(description = "Item category") @RequestParam(value = "category", required = false) String category,
            @Parameter(description = "Item image") @RequestParam("image") MultipartFile image,
            @AuthenticationPrincipal User seller) {
        
        try {
            ItemRequest request = new ItemRequest();
            request.setTitle(title);
            request.setDescription(description);
            request.setPrice(price);
            request.setCategory(category);

            ItemResponse response = itemService.createItem(request, image, seller);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    @Operation(summary = "Get all items", description = "Retrieves all available item listings")
    public ResponseEntity<List<ItemResponse>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    @GetMapping("/search")
    @Operation(summary = "Search items", description = "Search items by title, description, or category")
    public ResponseEntity<List<ItemResponse>> searchItems(
            @Parameter(description = "Search query") @RequestParam("query") String query) {
        return ResponseEntity.ok(itemService.searchItems(query));
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get items by category", description = "Retrieves items filtered by category")
    public ResponseEntity<List<ItemResponse>> getItemsByCategory(
            @Parameter(description = "Category name") @PathVariable String category) {
        return ResponseEntity.ok(itemService.getItemsByCategory(category));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get item by ID", description = "Retrieves a specific item by its ID")
    public ResponseEntity<?> getItemById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(itemService.getItemById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/seller/{sellerId}")
    @Operation(summary = "Get items by seller", description = "Retrieves all items listed by a specific seller")
    public ResponseEntity<List<ItemResponse>> getItemsBySeller(@PathVariable String sellerId) {
        return ResponseEntity.ok(itemService.getItemsBySeller(sellerId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update item", description = "Updates an existing item. Only the seller can update their items.")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> updateItem(
            @PathVariable String id,
            @Valid @RequestBody ItemRequest request,
            @AuthenticationPrincipal User seller) {
        try {
            ItemResponse response = itemService.updateItem(id, request, seller);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete item", description = "Deletes an item. Only the seller can delete their items.")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> deleteItem(
            @PathVariable String id,
            @AuthenticationPrincipal User seller) {
        try {
            itemService.deleteItem(id, seller);
            return ResponseEntity.ok(Map.of("message", "Item deleted successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/sold")
    @Operation(summary = "Mark item as sold", description = "Marks an item as sold/unavailable. Only the seller can do this.")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> markAsSold(
            @PathVariable String id,
            @AuthenticationPrincipal User seller) {
        try {
            ItemResponse response = itemService.markAsSold(id, seller);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
