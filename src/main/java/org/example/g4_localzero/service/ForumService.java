package org.example.g4_localzero.service;

import org.example.g4_localzero.enums.ForumType;
import org.example.g4_localzero.model.Forum;
import org.example.g4_localzero.enums.UserRole;
import org.example.g4_localzero.model.Initiative;
import org.example.g4_localzero.model.User;
import org.example.g4_localzero.repository.ForumRepository;
import org.example.g4_localzero.repository.InitiativeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ForumService {

    private final ForumRepository forumRepository;
    private final InitiativeRepository initiativeRepository;
    private final ForumAccessPolicy forumAccessPolicy;

    public ForumService(ForumRepository forumRepository,
                        InitiativeRepository initiativeRepository,
                        ForumAccessPolicy forumAccessPolicy) {
        this.forumRepository = forumRepository;
        this.initiativeRepository = initiativeRepository;
        this.forumAccessPolicy = forumAccessPolicy;
    }

    public List<Forum> getVisibleForums(User user) {
        List<Forum> allForums = forumRepository.findAll();
        List<Forum> visibleForums = new ArrayList<>();

        boolean isOrganizer = user.getRoles() != null && user.getRoles().contains(UserRole.COMMUNITY_ORGANIZER);

        for (Forum forum : allForums) {
            if (forum.getForumType() != ForumType.ROOT
                    && forumAccessPolicy.canViewForum(user, forum)) {
                boolean isGlobal = forum.getForumType() == ForumType.GLOBAL
                        || (forum.getName() != null && forum.getName().toLowerCase().contains("global"));

               if (isGlobal && !isOrganizer){
                   continue;
               }

               visibleForums.add(forum);
            }
        }

        return visibleForums;
    }

    public List<Initiative> getInitiativesForForum(Long forumId, User user) {
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new RuntimeException("Forum not found"));

        if (!forumAccessPolicy.canViewForum(user, forum)) {
            throw new RuntimeException("You are not allowed to view this forum");
        }

        boolean isOrganizer = user.getRoles() != null && user.getRoles().contains(UserRole.COMMUNITY_ORGANIZER);
        boolean isGlobal = forum.getForumType() == ForumType.GLOBAL
        || (forum.getName() != null && forum.getName().toLowerCase().contains("global"));

        if (isGlobal && !isOrganizer) {
            throw new RuntimeException("Access denied: Residents cannot view global initiatives");
        }


        return initiativeRepository.findByForum(forum);
    }
    }
