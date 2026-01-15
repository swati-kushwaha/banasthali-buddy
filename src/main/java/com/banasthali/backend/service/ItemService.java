package com.banasthali.backend.service;

import com.banasthali.backend.dto.ItemRequest;
import com.banasthali.backend.dto.ItemResponse;
import com.banasthali.backend.model.Item;
import com.banasthali.backend.model.User;
import com.banasthali.backend.repository.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (IOException e) {
            log.error("Could not create upload directory {}", uploadDir, e);
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    public ItemResponse createItem(ItemRequest request, MultipartFile image, User seller) {
        if (request.getPrice() == null || request.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }

        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = saveImage(image);
        }

        Item item = Item.builder()
            .title(request.getTitle())
            .description(request.getDescription())
            .price(request.getPrice())
            .imageUrl(imageUrl)
            .category(request.getCategory())
            .sellerId(seller.getId())
            .sellerName(seller.getDisplayName())
            .available(true)
            .createdAt(LocalDateTime.now())
            .build();

        Item savedItem = itemRepository.save(item);
        return ItemResponse.fromItem(savedItem);
    }

    public List<ItemResponse> getAllItems() {
        return itemRepository.findByAvailableTrueOrderByCreatedAtDesc()
            .stream()
            .map(ItemResponse::fromItem)
            .collect(Collectors.toList());
    }

    public List<ItemResponse> searchItems(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllItems();
        }
        return itemRepository.searchItems(query.trim())
            .stream()
            .map(ItemResponse::fromItem)
            .collect(Collectors.toList());
    }

    public List<ItemResponse> getItemsByCategory(String category) {
        return itemRepository.findByCategoryIgnoreCaseAndAvailableTrue(category)
            .stream()
            .map(ItemResponse::fromItem)
            .collect(Collectors.toList());
    }

    public ItemResponse getItemById(String id) {
        Item item = itemRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Item not found"));
        return ItemResponse.fromItem(item);
    }

    public List<ItemResponse> getItemsBySeller(String sellerId) {
        return itemRepository.findBySellerIdOrderByCreatedAtDesc(sellerId)
            .stream()
            .map(ItemResponse::fromItem)
            .collect(Collectors.toList());
    }

    public ItemResponse updateItem(String id, ItemRequest request, User seller) {
        Item item = itemRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        if (!item.getSellerId().equals(seller.getId())) {
            throw new IllegalArgumentException("You can only update your own items");
        }

        if (request.getTitle() != null) item.setTitle(request.getTitle());
        if (request.getDescription() != null) item.setDescription(request.getDescription());
        if (request.getPrice() != null) {
            if (request.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Price must be positive");
            }
            item.setPrice(request.getPrice());
        }
        if (request.getCategory() != null) item.setCategory(request.getCategory());

        Item updatedItem = itemRepository.save(item);
        return ItemResponse.fromItem(updatedItem);
    }

    public void deleteItem(String id, User seller) {
        Item item = itemRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        if (!item.getSellerId().equals(seller.getId())) {
            throw new IllegalArgumentException("You can only delete your own items");
        }

        itemRepository.delete(item);
    }

    public ItemResponse markAsSold(String id, User seller) {
        Item item = itemRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        if (!item.getSellerId().equals(seller.getId())) {
            throw new IllegalArgumentException("You can only update your own items");
        }

        item.setAvailable(false);
        Item updatedItem = itemRepository.save(item);
        return ItemResponse.fromItem(updatedItem);
    }

    private String saveImage(MultipartFile image) {
        try {
            String originalFilename = image.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String filename = UUID.randomUUID().toString() + extension;
            Path targetPath = Paths.get(uploadDir, filename);
            Files.copy(image.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            // Return relative URL for the image
            return "/uploads/" + filename;
        } catch (IOException e) {
            log.error("Failed to save image", e);
            throw new RuntimeException("Failed to save image", e);
        }
    }
}
