package com.ricardo.GymPass.domain.repository;

import com.ricardo.GymPass.domain.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
    Optional<Membership> findByExternalId(String externalId);
    
    List<Membership> findByUserExternalId(String externalId);
}