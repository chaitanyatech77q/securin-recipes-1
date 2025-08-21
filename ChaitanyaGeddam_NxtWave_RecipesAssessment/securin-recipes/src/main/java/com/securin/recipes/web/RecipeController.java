package com.securin.recipes.web;

import com.securin.recipes.dto.PageResponse;
import com.securin.recipes.dto.RecipeDto;
import com.securin.recipes.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipes")
@CrossOrigin
public class RecipeController {

    private final RecipeService service;

    @Autowired
    public RecipeController(RecipeService service) {
        this.service = service;
    }

    @GetMapping
    public PageResponse<RecipeDto> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return service.list(page, limit);
    }
}
