package com.modus.DatsOrangeHackathon.Controllers;

import com.modus.DatsOrangeHackathon.Models.Orange;
import com.modus.DatsOrangeHackathon.Repositories.OrangeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Tag(name = "Oranges", description = "Operations for interacting with oranges")
@RestController
@RequestMapping("/oranges")
public class OrangeController {
    private final OrangeRepository orangeRepository;

    public OrangeController(OrangeRepository orangeRepository) {
        this.orangeRepository = orangeRepository;
    }

    @Operation(summary = "Get all oranges", description = "Endpoint to get a list of all oranges.")
    @GetMapping("/")
    public List<Orange> getAllOranges() {
        return orangeRepository.findAll();
    }

    @Operation(
            summary = "Create new orange",
            description = "Endpoint to create new orange.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "New Orange object",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Orange.class), examples = {
//                            @ExampleObject(name = "promotion", value = "{\"establishment\":{\"establishmentID\":1, \"name\":\"Sherlock cafe\", \"street\":\"221B Baker Street\"},\"type\":\"DISCOUNT\",\"targetAmount\":100}")
                    })
            )
    )
    @PostMapping("/")
    public Orange createOrange(@RequestBody Orange orange) {
        return orangeRepository.save(orange);
    }

    @Operation(
            summary = "Update orange information",
            description = "Endpoint to update existing orange information.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User object containing updated details.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Orange.class), examples = {
//                            @ExampleObject(name = "user", value = "{\"username\":\"mrthursday\",\"password\":\"password\",\"email\":\"mrthursday@gmail.com\"}")
                    })
            )
    )
    @PutMapping("/{id}")
    public Orange updateOrange(@PathVariable Integer id, @RequestBody Orange orange) {
        return orangeRepository.findById(id)
                .map(existingOrange -> {
                    BeanUtils.copyProperties(orange, existingOrange, "id");
                    return orangeRepository.save(existingOrange);
                })
                .orElseThrow(this::orangeNotFound);
    }

    @Operation(
            summary = "Delete orange",
            description = "Endpoint to delete a orange by their ID."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrange(@PathVariable Integer id) {
        orangeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private ResponseStatusException orangeNotFound() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }

}