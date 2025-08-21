package com.securin.recipes.repo;

import com.securin.recipes.domain.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query(value = """
      SELECT * FROM recipes
      WHERE (:title IS NULL OR lower(title) LIKE lower(CONCAT('%', :title, '%')))
        AND (:cuisine IS NULL OR cuisine = :cuisine)
        AND (:minRating IS NULL OR rating >= :minRating)
        AND (:maxRating IS NULL OR rating <= :maxRating)
        AND (:minTotal IS NULL OR total_time >= :minTotal)
        AND (:maxTotal IS NULL OR total_time <= :maxTotal)
        AND (:calOp IS NULL OR (
             (:calOp = '='  AND calories_int = :calVal) OR
             (:calOp = '<'  AND calories_int < :calVal) OR
             (:calOp = '>'  AND calories_int > :calVal) OR
             (:calOp = '<=' AND calories_int <= :calVal) OR
             (:calOp = '>=' AND calories_int >= :calVal)
        ))
      """,
      countQuery = """
      SELECT count(*) FROM recipes
      WHERE (:title IS NULL OR lower(title) LIKE lower(CONCAT('%', :title, '%')))
        AND (:cuisine IS NULL OR cuisine = :cuisine)
        AND (:minRating IS NULL OR rating >= :minRating)
        AND (:maxRating IS NULL OR rating <= :maxRating)
        AND (:minTotal IS NULL OR total_time >= :minTotal)
        AND (:maxTotal IS NULL OR total_time <= :maxTotal)
        AND (:calOp IS NULL OR 1=1)
      """,
      nativeQuery = true)
    Page<Recipe> search(
      @Param("title") String title,
      @Param("cuisine") String cuisine,
      @Param("minRating") Float minRating,
      @Param("maxRating") Float maxRating,
      @Param("minTotal") Integer minTotal,
      @Param("maxTotal") Integer maxTotal,
      @Param("calOp") String calOp,
      @Param("calVal") Integer calVal,
      Pageable pageable
    );
}
