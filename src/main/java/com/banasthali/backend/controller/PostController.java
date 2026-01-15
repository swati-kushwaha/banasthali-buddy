package com.banasthali.backend.controller;

import com.banasthali.backend.model.Post;
import com.banasthali.backend.repository.PostRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "Posts", description = "Predefined booking stations")
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepository;

    @GetMapping
    @Operation(summary = "List all posts")
    public List<Post> list() {
        return postRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a post (admin only)")
    public ResponseEntity<Post> create(@RequestBody Post p) {
        Post saved = postRepository.save(p);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a post (admin only)")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        postRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
