package com.edifika.reservation.infrastructure.persistence.jpa.repositories;

import com.edifika.reservation.domain.model.entities.CommonArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommonAreaRepository extends JpaRepository<CommonArea, Long> {
    Optional<CommonArea> findByName(String name);
}

