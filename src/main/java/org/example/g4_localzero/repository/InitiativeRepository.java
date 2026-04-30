package org.example.g4_localzero.repository;

import org.example.g4_localzero.model.Initiative;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InitiativeRepository extends JpaRepository<Initiative, Long> {
}