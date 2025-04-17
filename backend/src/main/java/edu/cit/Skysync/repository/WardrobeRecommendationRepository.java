package edu.cit.Skysync.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.cit.Skysync.entity.WardrobeRecommendationEntity;

public interface WardrobeRecommendationRepository extends JpaRepository<WardrobeRecommendationEntity, Long> {
    List<WardrobeRecommendationEntity> findByWeatherCode(int weatherCode);
}
