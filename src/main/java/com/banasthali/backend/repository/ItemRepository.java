package com.banasthali.backend.repository;

import com.banasthali.backend.model.Item;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ItemRepository extends MongoRepository<Item, String> {
    
    List<Item> findByAvailableTrueOrderByCreatedAtDesc();
    
    List<Item> findBySellerIdOrderByCreatedAtDesc(String sellerId);
    
    @Query("{ $or: [ " +
           "{ 'title': { $regex: ?0, $options: 'i' } }, " +
           "{ 'description': { $regex: ?0, $options: 'i' } }, " +
           "{ 'category': { $regex: ?0, $options: 'i' } } " +
           "], 'available': true }")
    List<Item> searchItems(String query);
    
    List<Item> findByCategoryIgnoreCaseAndAvailableTrue(String category);
}
