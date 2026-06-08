package org.example.g4_localzero.service;

import org.example.g4_localzero.enums.ForumType;
import org.example.g4_localzero.model.Forum;
import org.example.g4_localzero.model.User;
import org.springframework.stereotype.Component;

@Component
public class ForumAccessPolicy {

    public boolean canViewForum(User user, Forum forum) {
        if (user == null || forum == null) {
            return false;
        }

        if (forum.getForumType() == ForumType.ROOT) {
            return true;
        }

        if (forum.getForumType() == ForumType.GLOBAL) {
            return true;
        }

        if (forum.getForumType() == ForumType.NEIGHBORHOOD) {
            return sameNeighborhood(user, forum);
        }

        return false;
    }

    public boolean canPostInForum(User user, Forum forum) {
        return canViewForum(user, forum);
    }

    private boolean sameNeighborhood(User user, Forum forum) {
        if (user.getLocation() == null || forum.getNeighborhood() == null) {
            return false;
        }

        return user.getLocation().equalsIgnoreCase(forum.getNeighborhood());
    }
}