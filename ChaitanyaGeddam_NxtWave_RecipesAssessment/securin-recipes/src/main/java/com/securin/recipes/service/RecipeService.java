package com.securin.recipes.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.securin.recipes.domain.Recipe;
import com.securin.recipes.dto.PageResponse;
import com.securin.recipes.dto.RecipeDto;
import com.securin.recipes.repo.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository repo;
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public RecipeService(RecipeRepository repo) {
        this.repo = repo;
    }

    public PageResponse<RecipeDto> list(int page, int limit) {
        Pageable pageable = PageRequest.of(Math.max(page-1,0), limit, Sort.by(Sort.Direction.DESC, "rating"));
        Page<Recipe> p = repo.findAll(pageable);
        return toPageResponse(p, page, limit);
    }

    public PageResponse<RecipeDto> search(
            String title, String cuisine,
            String ratingExpr, String totalTimeExpr, String caloriesExpr,
            int page, int limit, String sortField, String sortDir
    ) {
        Sort sort = (sortField == null || sortField.isBlank())
                ? Sort.by(Sort.Direction.DESC, "rating")
                : Sort.by("desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC, toDb(sortField));

        Pageable pageable = PageRequest.of(Math.max(page-1,0), limit, sort);

        Range rating = parseExpr(ratingExpr);
        Range total  = parseExpr(totalTimeExpr);
        Range cal    = parseExpr(caloriesExpr);

        Page<Recipe> p = repo.search(
                nullIfBlank(title),
                nullIfBlank(cuisine),
                rating.min, rating.max,
                total.minI, total.maxI,
                cal.op, cal.valI,
                pageable
        );
        return toPageResponse(p, page, limit);
    }

    private String toDb(String f) {
        if ("rating".equals(f)) return "rating";
        if ("total_time".equals(f)) return "total_time";
        if ("title".equals(f)) return "title";
        return "rating";
    }

    private static class Range {
        Float min, max;
        Integer minI, maxI;
        String op; Integer valI;
    }

    private Range parseExpr(String expr) {
        Range r = new Range();
        if (expr == null || expr.isBlank()) return r;
        expr = expr.trim();
        Pattern p = Pattern.compile("^(<=|>=|=|<|>)(\s*[0-9]+(?:\.[0-9]+)?)$");
        Matcher m = p.matcher(expr);
        if (m.find()) {
            String op = m.group(1);
            String v  = m.group(2).trim();
            if (v.contains(".")) {
                Float fv = Float.parseFloat(v);
                switch (op) {
                    case ">=" -> r.min = fv;
                    case "<=" -> r.max = fv;
                    case ">"  -> r.min = fv + Float.MIN_NORMAL;
                    case "<"  -> r.max = fv - Float.MIN_NORMAL;
                    default   -> { r.min = fv; r.max = fv; }
                }
            } else {
                Integer iv = Integer.parseInt(v);
                r.op = op; r.valI = iv;
                if (">=".equals(op) || ">".equals(op)) r.minI = iv;
                if ("<=".equals(op) || "<".equals(op)) r.maxI = iv;
            }
            return r;
        }
        m = Pattern.compile("^(\d+)\.\.(\d+)$").matcher(expr);
        if (m.find()) {
            int a = Integer.parseInt(m.group(1)), b = Integer.parseInt(m.group(2));
            r.minI = Math.min(a,b); r.maxI = Math.max(a,b); r.op = ">="; r.valI = r.minI;
        }
        return r;
    }

    private String nullIfBlank(String s){ return (s==null || s.isBlank())?null:s; }

    private PageResponse<RecipeDto> toPageResponse(Page<Recipe> p, int page, int limit) {
        List<RecipeDto> data = p.getContent().stream().map(r -> {
            Map<String,Object> nutrients = null;
            try {
                nutrients = r.getNutrients()==null ? null :
                        mapper.readValue(r.getNutrients(), new TypeReference<Map<String,Object>>() {});
            } catch (Exception ignored) {}
            return new RecipeDto(
                    r.getId(), r.getTitle(), r.getCuisine(), r.getRating(),
                    r.getPrepTime(), r.getCookTime(), r.getTotalTime(),
                    r.getDescription(), nutrients, r.getServes()
            );
        }).collect(Collectors.toList());
        return new PageResponse<>(page, limit, p.getTotalElements(), data);
    }
}
