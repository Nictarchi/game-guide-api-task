package com.example.game_guide.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "point_of_interest")
public class PointOfInterest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "coord_x", nullable = false)
    private Double coordX;

    @Column(name = "coord_y", nullable = false)
    private Double coordY;

    @Column(name = "avg_rating", columnDefinition = "DECIMAL(3,2) DEFAULT 0.00")
    private Double avgRating = 0.0;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "poi", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Rating> ratings;

    @OneToMany(mappedBy = "poi", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Guide> guides;

    public Double getDistance(Double userX, Double userY) {
        return Math.sqrt(Math.pow(this.coordX - userX, 2) + Math.pow(this.coordY - userY, 2));
    }
}