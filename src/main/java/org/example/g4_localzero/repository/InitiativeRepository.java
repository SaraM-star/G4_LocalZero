package org.example.g4_localzero.repository;

import org.example.g4_localzero.model.Forum;
import org.example.g4_localzero.model.Initiative;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InitiativeRepository extends JpaRepository<Initiative, Long> {

    List<Initiative> findByForum(Forum forum);
}