package com.banasthali.backend.controller;

import com.banasthali.backend.dto.ItemRequest;
import com.banasthali.backend.dto.ItemResponse;
import com.banasthali.backend.model.User;
import com.banasthali.backend.service.ItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
@Tag(name = "Items", description = "Item APIs")
public class ItemController {

    private final ItemService itemService;

    // CREATE ITEM WITH IMAGE
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Create item")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> createItem(

            @RequestParam("title") String title,

            @RequestParam("description") String description,

            @RequestParam("price") BigDecimal price,

            @RequestParam(value = "category", required = false) String category,

            @RequestParam(value = "image", required = false) MultipartFile image,

            @RequestParam("sellerPhone") String sellerPhone,

            @RequestParam("sellerHostel") String sellerHostel,

            @RequestParam(value = "sellerRoom", required = false) String sellerRoom,

            @AuthenticationPrincipal User seller
    ){

        if(seller == null){

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)

                    .body(Map.of("error","Login required"));

        }

        ItemRequest request = new ItemRequest();

        request.setTitle(title);

        request.setDescription(description);

        request.setPrice(price);

        request.setCategory(category);

        request.setSellerPhone(sellerPhone);

        request.setSellerHostel(sellerHostel);

        request.setSellerRoom(sellerRoom);

        ItemResponse response =
                itemService.createItem(request,image,seller);

        return ResponseEntity.status(HttpStatus.CREATED)

                .body(response);

    }


    // GET ALL ITEMS
    @GetMapping
    public ResponseEntity<List<ItemResponse>> getAllItems(){

        return ResponseEntity.ok(

                itemService.getAllItems()

        );

    }


    // SEARCH ITEMS
    @GetMapping("/search")
    public ResponseEntity<List<ItemResponse>> searchItems(

            @RequestParam("query") String query
    ){

        return ResponseEntity.ok(

                itemService.searchItems(query)

        );

    }


    // FILTER BY CATEGORY
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ItemResponse>> getItemsByCategory(

            @PathVariable String category
    ){

        return ResponseEntity.ok(

                itemService.getItemsByCategory(category)

        );

    }


    // GET ITEM BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getItemById(

            @PathVariable String id
    ){

        try{

            return ResponseEntity.ok(

                    itemService.getItemById(id)

            );

        }
        catch(Exception e){

            return ResponseEntity.status(HttpStatus.NOT_FOUND)

                    .body(Map.of("error","Item not found"));

        }

    }


    // GET ITEMS BY SELLER
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<ItemResponse>> getItemsBySeller(

            @PathVariable String sellerId
    ){

        return ResponseEntity.ok(

                itemService.getItemsBySeller(sellerId)

        );

    }


    // UPDATE ITEM
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateItem(

            @PathVariable String id,

            @Valid @RequestBody ItemRequest request,

            @AuthenticationPrincipal User seller
    ){

        try{

            ItemResponse response =
                    itemService.updateItem(id,request,seller);

            return ResponseEntity.ok(response);

        }
        catch(Exception e){

            return ResponseEntity.badRequest()

                    .body(Map.of("error",e.getMessage()));

        }

    }


    // DELETE ITEM
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteItem(

            @PathVariable String id,

            @AuthenticationPrincipal User seller
    ){

        try{

            itemService.deleteItem(id,seller);

            return ResponseEntity.ok(

                    Map.of("message","Item deleted")

            );

        }
        catch(Exception e){

            return ResponseEntity.badRequest()

                    .body(Map.of("error",e.getMessage()));

        }

    }


    // MARK ITEM SOLD
    @PatchMapping("/{id}/sold")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> markAsSold(

            @PathVariable String id,

            @AuthenticationPrincipal User seller
    ){

        try{

            ItemResponse response =
                    itemService.markAsSold(id,seller);

            return ResponseEntity.ok(response);

        }
        catch(Exception e){

            return ResponseEntity.badRequest()

                    .body(Map.of("error",e.getMessage()));

        }

    }

}