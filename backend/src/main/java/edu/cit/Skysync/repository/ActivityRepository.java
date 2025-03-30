package edu.cit.Skysync.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.cit.Skysync.entity.ActivityEntity;
import edu.cit.Skysync.entity.UserEntity;

public interface ActivityRepository extends JpaRepository<ActivityEntity, Long> {
    List<ActivityEntity> findByUser(UserEntity user);
}