package com.ricardo.GymPass.domain.repository;

import com.ricardo.GymPass.domain.entity.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GymRepository extends JpaRepository<Gym, Long> {
    Optional<Gym> findFirstByActiveTrue();
}
