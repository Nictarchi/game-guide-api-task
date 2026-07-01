package com.example.game_guide.repository;

import com.example.game_guide.model.PointOfInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PoiRepository extends JpaRepository<PointOfInterest, Long> {

    @Query("SELECT p FROM PointOfInterest p WHERE " +
            "p.category.name = :category AND " +
            "p.avgRating >= :minRating")
    List<PointOfInterest> findByCategoryAndMinRating(
            @Param("category") String category,
            @Param("minRating") Double minRating
    );

    @Query("SELECT p FROM PointOfInterest p WHERE p.category.name = :category")
    List<PointOfInterest> findByCategory(@Param("category") String category);

    @Query("SELECT p FROM PointOfInterest p WHERE p.avgRating >= :minRating")
    List<PointOfInterest> findByMinRating(@Param("minRating") Double minRating);
}
