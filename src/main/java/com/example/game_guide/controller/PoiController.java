package com.example.game_guide.controller;

import com.example.game_guide.dto.GuideRequestDto;
import com.example.game_guide.dto.PoiResponseDto;
import com.example.game_guide.dto.RatingRequestDto;
import com.example.game_guide.model.PointOfInterest;
import com.example.game_guide.model.Guide;
import com.example.game_guide.service.PoiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pois")
@RequiredArgsConstructor
public class PoiController {

    private final PoiService poiService;

    @GetMapping("/nearby")
    public ResponseEntity<List<PoiResponseDto>> getNearbyPois(
            @RequestParam Double x,
            @RequestParam Double y,
            @RequestParam Double radius,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) Integer limit) {

        List<PoiResponseDto> pois = poiService.findNearbyPois(
                x, y, radius, category, minRating, sortBy, limit);
        return ResponseEntity.ok(pois);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPoiInfo(@PathVariable Long id) {
        PointOfInterest poi = poiService.getPoiById(id);
        Double avgRating = poiService.getAverageRating(id);

        Map<String, Object> response = new HashMap<>();
        response.put("id", poi.getId());
        response.put("name", poi.getName());
        response.put("category", poi.getCategory().getName());
        response.put("coordX", poi.getCoordX());
        response.put("coordY", poi.getCoordY());
        response.put("avgRating", avgRating != null ? avgRating : 0.0);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/rating")
    public ResponseEntity<Map<String, Object>> getAverageRating(@PathVariable Long id) {
        Double avgRating = poiService.getAverageRating(id);
        Map<String, Object> response = new HashMap<>();
        response.put("poiId", id);
        response.put("avgRating", avgRating != null ? avgRating : 0.0);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/rating")
    public ResponseEntity<Map<String, Object>> addRating(
            @PathVariable Long id,
            @Valid @RequestBody RatingRequestDto request) {
        poiService.addRating(id, request);
        Double newAvg = poiService.getAverageRating(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Оценка успешно добавлена");
        response.put("newAvgRating", newAvg != null ? newAvg : 0.0);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/guides")
    public ResponseEntity<List<Guide>> getGuides(@PathVariable Long id) {
        List<Guide> guides = poiService.getGuidesByPoiId(id);
        return ResponseEntity.ok(guides);
    }

    @PostMapping("/{id}/guides")
    public ResponseEntity<Map<String, Object>> addGuide(
            @PathVariable Long id,
            @Valid @RequestBody GuideRequestDto request) {
        Guide saved = poiService.addGuide(id, request);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Гайд успешно добавлен");
        response.put("guideId", saved.getId());
        return ResponseEntity.ok(response);
    }
}
