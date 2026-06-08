package org.example.g4_localzero.repository;

import org.example.g4_localzero.enums.ForumType;
import org.example.g4_localzero.model.Forum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ForumRepository extends JpaRepository<Forum, Long> {

    Optional<Forum> findByForumType(ForumType forumType);

    Optional<Forum> findByNeighborhoodIgnoreCase(String neighborhood);
}