package org.example.g4_localzero.repository;

import org.example.g4_localzero.model.EcoAction;
import org.example.g4_localzero.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EcoActionRepository extends JpaRepository<EcoAction, Long> {
    List<EcoAction> findByUserOrderByCreatedAtDesc(User user);
}
