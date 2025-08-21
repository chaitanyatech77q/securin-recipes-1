package com.securin.recipes.web;

import com.securin.recipes.dto.PageResponse;
import com.securin.recipes.dto.RecipeDto;
import com.securin.recipes.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipes/search")
@CrossOrigin
public class SearchController {

    private final RecipeService service;

    @Autowired
    public SearchController(RecipeService service) {
        this.service = service;
    }

    @GetMapping
    public PageResponse<RecipeDto> search(
            @RequestParam(required=false) String calories,
            @RequestParam(required=false) String title,
            @RequestParam(required=false) String cuisine,
            @RequestParam(name="total_time", required=false) String totalTime,
            @RequestParam(required=false) String rating,
            @RequestParam(defaultValue="1") int page,
            @RequestParam(defaultValue="10") int limit,
            @RequestParam(name="sort", required=false) String sortField,
            @RequestParam(name="order", required=false) String sortDir
    ) {
        return service.search(title, cuisine, rating, totalTime, calories, page, limit, sortField, sortDir);
    }
}
