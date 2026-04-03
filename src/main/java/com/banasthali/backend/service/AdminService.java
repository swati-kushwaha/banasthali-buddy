package com.banasthali.backend.service;

import com.banasthali.backend.model.Item;
import com.banasthali.backend.model.Role;
import com.banasthali.backend.model.User;
import com.banasthali.backend.repository.ItemRepository;
import com.banasthali.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    // users
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public void deleteUser(String id){
        userRepository.deleteById(id);
    }

    // items
    public List<Item> getAllItems(){
        return itemRepository.findAll();
    }

    public void deleteItem(String id){
        itemRepository.deleteById(id);
    }

    // stats
    public long totalUsers(){
        return userRepository.count();
    }

    public long totalItems(){
        return itemRepository.count();
    }

    public long totalDrivers(){return userRepository.countByRole(Role.DRIVER);}

}