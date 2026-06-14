package org.example.g4_localzero.service;

import org.example.g4_localzero.enums.UserRole;
import org.example.g4_localzero.model.Initiative;
import org.example.g4_localzero.model.Message;
import org.example.g4_localzero.model.User;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationPolicy {

    public boolean isCommunityOrganizer(User user) {
        return user != null
                && user.getRoles() != null
                && user.getRoles().contains(UserRole.COMMUNITY_ORGANIZER);
    }

    public boolean isInitiativeOwner(User user, Initiative initiative) {
        return user != null
                && user.getId() != null
                && initiative != null
                && initiative.getCreator() != null
                && user.getId().equals(initiative.getCreator().getId());
    }

    public boolean canEditInitiative(User user, Initiative initiative) {
        return isInitiativeOwner(user, initiative);
    }

    public boolean canDeleteInitiative(User user, Initiative initiative) {
        return isInitiativeOwner(user, initiative)
                || isCommunityOrganizer(user);
    }

    public boolean isMessageSender(User user, Message message) {
        return user != null
                && user.getId() != null
                && message != null
                && message.getSender() != null
                && user.getId().equals(message.getSender().getId());
    }

    public boolean canEditMessage(User user, Message message) {
        return isMessageSender(user, message);
    }

    public boolean canDeleteMessage(User user, Message message) {
        return isMessageSender(user, message);
    }

    public boolean canManageUsers(User user) {
        return isCommunityOrganizer(user);
    }

    public boolean canDeactivateUser(User actingUser, User targetUser) {
        return isCommunityOrganizer(actingUser)
                && actingUser.getId() != null
                && targetUser != null
                && targetUser.getId() != null
                && !actingUser.getId().equals(targetUser.getId())
                && targetUser.isActive();
    }
}