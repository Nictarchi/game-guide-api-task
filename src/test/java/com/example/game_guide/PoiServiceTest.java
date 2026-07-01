package com.example.game_guide;

import com.example.game_guide.model.PointOfInterest;
import com.example.game_guide.model.Category;
import com.example.game_guide.repository.PoiRepository;
import com.example.game_guide.repository.RatingRepository;
import com.example.game_guide.repository.GuideRepository;
import com.example.game_guide.service.PoiService;
import com.example.game_guide.dto.PoiResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PoiServiceTest {

    @Mock
    private PoiRepository poiRepository;

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private GuideRepository guideRepository;

    @InjectMocks
    private PoiService poiService;

    private List<PointOfInterest> testPois;

    @BeforeEach
    void setUp() {
        Category category = new Category();
        category.setName("Квестовая");

        PointOfInterest poi1 = new PointOfInterest();
        poi1.setId(1L);
        poi1.setName("Заброшенный лес");
        poi1.setCoordX(100.0);
        poi1.setCoordY(200.0);
        poi1.setAvgRating(8.5);
        poi1.setCategory(category);

        PointOfInterest poi2 = new PointOfInterest();
        poi2.setId(2L);
        poi2.setName("Тёмная пещера");
        poi2.setCoordX(150.0);
        poi2.setCoordY(180.0);
        poi2.setAvgRating(7.0);
        poi2.setCategory(category);

        testPois = Arrays.asList(poi1, poi2);
    }

    @Test
    void testFindNearbyPois() {
        when(poiRepository.findAll()).thenReturn(testPois);

        List<PoiResponseDto> result = poiService.findNearbyPois(
                110.0, 190.0, 100.0, null, null, null, 10);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Заброшенный лес", result.get(0).getName());
    }

    @Test
    void testFilterByCategory() {
        when(poiRepository.findAll()).thenReturn(testPois);

        List<PoiResponseDto> result = poiService.findNearbyPois(
                110.0, 190.0, 100.0, "Квестовая", null, null, 10);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testFilterByMinRating() {
        when(poiRepository.findAll()).thenReturn(testPois);

        List<PoiResponseDto> result = poiService.findNearbyPois(
                110.0, 190.0, 100.0, null, 8.0, null, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Заброшенный лес", result.get(0).getName());
    }

    @Test
    void testGetPoiByIdNotFound() {
        when(poiRepository.findById(999L)).thenReturn(java.util.Optional.empty());

        assertThrows(RuntimeException.class, () -> poiService.getPoiById(999L));
    }
}
