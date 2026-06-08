package org.example.g4_localzero.service;

import org.example.g4_localzero.enums.ForumType;
import org.example.g4_localzero.model.Forum;
import org.example.g4_localzero.repository.ForumRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ForumDataInitializer implements CommandLineRunner {

    private final ForumRepository forumRepository;

    public ForumDataInitializer(ForumRepository forumRepository) {
        this.forumRepository = forumRepository;
    }

    @Override
    public void run(String... args) {
        if (!forumRepository.findAll().isEmpty()) {
            return;
        }

        Forum root = new Forum("LocalZero Forum Root", null, ForumType.ROOT);

        Forum global = new Forum("Global Forum", null, ForumType.GLOBAL);
        Forum groupA = new Forum("Neighborhood A Forum", "Neighborhood A", ForumType.NEIGHBORHOOD);
        Forum groupB = new Forum("Neighborhood B Forum", "Neighborhood B", ForumType.NEIGHBORHOOD);
        Forum groupC = new Forum("Neighborhood C Forum", "Neighborhood C", ForumType.NEIGHBORHOOD);

        root.addChild(global);
        root.addChild(groupA);
        root.addChild(groupB);
        root.addChild(groupC);

        forumRepository.save(root);
    }
}