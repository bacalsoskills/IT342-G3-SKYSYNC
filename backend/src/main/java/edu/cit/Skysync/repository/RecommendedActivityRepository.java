package edu.cit.Skysync.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.cit.Skysync.entity.RecommendedActivityEntity;

@Repository
public interface RecommendedActivityRepository extends JpaRepository<RecommendedActivityEntity, Long> {
    List<RecommendedActivityEntity> findByWeatherConditionIgnoreCase(String weatherCondition);
}
