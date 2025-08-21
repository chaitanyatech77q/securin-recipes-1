package com.securin.recipes.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cuisine;

    @Column(nullable = false)
    private String title;

    private Float rating;
    @Column(name = "prep_time")
    private Integer prepTime;
    @Column(name = "cook_time")
    private Integer cookTime;
    @Column(name = "total_time")
    private Integer totalTime;

    @Column(columnDefinition = "text")
    private String description;

    @Column(columnDefinition = "jsonb")
    private String nutrients;

    private String serves;

    @Column(name = "calories_int", insertable = false, updatable = false)
    private Integer caloriesInt;

    public Recipe() {}

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCuisine() { return cuisine; }
    public void setCuisine(String cuisine) { this.cuisine = cuisine; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Float getRating() { return rating; }
    public void setRating(Float rating) { this.rating = rating; }

    public Integer getPrepTime() { return prepTime; }
    public void setPrepTime(Integer prepTime) { this.prepTime = prepTime; }

    public Integer getCookTime() { return cookTime; }
    public void setCookTime(Integer cookTime) { this.cookTime = cookTime; }

    public Integer getTotalTime() { return totalTime; }
    public void setTotalTime(Integer totalTime) { this.totalTime = totalTime; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getNutrients() { return nutrients; }
    public void setNutrients(String nutrients) { this.nutrients = nutrients; }

    public String getServes() { return serves; }
    public void setServes(String serves) { this.serves = serves; }

    public Integer getCaloriesInt() { return caloriesInt; }
    public void setCaloriesInt(Integer caloriesInt) { this.caloriesInt = caloriesInt; }
}
