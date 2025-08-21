package com.securin.recipes.ingest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.securin.recipes.domain.Recipe;
import com.securin.recipes.repo.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class DataLoader implements CommandLineRunner {

    private final RecipeRepository repo;
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public DataLoader(RecipeRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run(String... args) throws Exception {
        if (repo.count() > 0) return;
        try {
            var resource = new ClassPathResource("data/US_recipes.json");
            String json;
            try(var in = resource.getInputStream()){
                json = new String(in.readAllBytes());
            }
            List<RecipeImport> rows = mapper.readValue(json, new TypeReference<List<RecipeImport>>(){});
            for (var r : rows) {
                Recipe entity = new Recipe();
                entity.setCuisine(nullIfBlank(r.cuisine));
                entity.setTitle(Objects.requireNonNullElse(r.title, "Untitled"));
                entity.setRating(toFloat(r.rating));
                entity.setPrepTime(toInt(r.prep_time));
                entity.setCookTime(toInt(r.cook_time));
                entity.setTotalTime(toInt(r.total_time));
                entity.setDescription(nullIfBlank(r.description));
                entity.setNutrients(r.nutrients == null ? null : mapper.writeValueAsString(r.nutrients));
                entity.setServes(nullIfBlank(r.serves));
                repo.save(entity);
            }
        } catch (Exception e) {
            // If resource not bundled, ignore to allow running without data
            System.out.println("DataLoader notice: " + e.getMessage());
        }
    }

    private String nullIfBlank(String s){ return (s==null || s.isBlank())?null:s; }

    private Float toFloat(Object v){
        if (v == null) return null;
        try {
            String s = v.toString().trim();
            if ("NaN".equalsIgnoreCase(s)) return null;
            return Float.parseFloat(s);
        } catch (Exception e){ return null; }
    }

    private Integer toInt(Object v){
        if (v == null) return null;
        try {
            String s = v.toString().trim();
            if ("NaN".equalsIgnoreCase(s)) return null;
            return (int)Math.round(Double.parseDouble(s));
        } catch (Exception e){ return null; }
    }
}
