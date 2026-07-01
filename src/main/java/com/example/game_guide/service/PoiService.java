package com.example.game_guide.service;

import com.example.game_guide.dto.GuideRequestDto;
import com.example.game_guide.dto.PoiResponseDto;
import com.example.game_guide.dto.RatingRequestDto;
import com.example.game_guide.model.Guide;
import com.example.game_guide.model.PointOfInterest;
import com.example.game_guide.model.Rating;
import com.example.game_guide.repository.GuideRepository;
import com.example.game_guide.repository.PoiRepository;
import com.example.game_guide.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PoiService {

    private final PoiRepository poiRepository;
    private final RatingRepository ratingRepository;
    private final GuideRepository guideRepository;

    public List<PoiResponseDto> findNearbyPois(Double userX, Double userY, Double radius,
                                               String category, Double minRating,
                                               String sortBy, Integer limit) {
        List<PointOfInterest> pois = poiRepository.findAll();

        List<PointOfInterest> filtered = pois.stream()
                .filter(p -> p.getDistance(userX, userY) <= radius)
                .filter(p -> category == null || p.getCategory().getName().equalsIgnoreCase(category))
                .filter(p -> minRating == null || p.getAvgRating() >= minRating)
                .collect(Collectors.toList());

        // Сортировка
        if ("rating".equalsIgnoreCase(sortBy)) {
            filtered.sort(Comparator.comparing(PointOfInterest::getAvgRating).reversed());
        } else if ("name".equalsIgnoreCase(sortBy)) {
            filtered.sort(Comparator.comparing(PointOfInterest::getName));
        } else {
            filtered.sort(Comparator.comparingDouble(p -> p.getDistance(userX, userY)));
        }

        if (limit == null) limit = 10;
        int finalLimit = Math.min(limit, filtered.size());
        filtered = filtered.subList(0, finalLimit);

        return filtered.stream()
                .map(p -> new PoiResponseDto(
                        p.getId(),
                        p.getName(),
                        p.getCategory().getName(),
                        p.getCoordX(),
                        p.getCoordY(),
                        p.getAvgRating(),
                        p.getDistance(userX, userY)
                ))
                .collect(Collectors.toList());
    }

    public PointOfInterest getPoiById(Long id) {
        return poiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Точка интереса не найдена"));
    }

    @Transactional
    public void addRating(Long poiId, RatingRequestDto request) {
        PointOfInterest poi = getPoiById(poiId);
        Rating rating = new Rating();
        rating.setValue(request.getValue());
        rating.setPoi(poi);
        ratingRepository.save(rating);

        Double newAvg = ratingRepository.calculateAverageRating(poiId);
        poi.setAvgRating(newAvg != null ? newAvg : 0.0);
        poiRepository.save(poi);
    }

    public Double getAverageRating(Long poiId) {
        return ratingRepository.calculateAverageRating(poiId);
    }

    @Transactional
    public Guide addGuide(Long poiId, GuideRequestDto request) {
        PointOfInterest poi = getPoiById(poiId);
        Guide guide = new Guide();
        guide.setContent(request.getContent());
        guide.setPoi(poi);
        return guideRepository.save(guide);
    }

    public List<Guide> getGuidesByPoiId(Long poiId) {
        getPoiById(poiId);
        return guideRepository.findAllByPoiIdOrderByCreatedAtDesc(poiId);
    }
}
