package com.example.game_guide.repository;

import com.example.game_guide.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("SELECT AVG(r.value) FROM Rating r WHERE r.poi.id = :poiId")
    Double calculateAverageRating(@Param("poiId") Long poiId);

    List<Rating> findAllByPoiId(Long poiId);
}
