package org.example.g4_localzero.service;

import org.example.g4_localzero.enums.ForumType;
import org.example.g4_localzero.enums.UserRole;
import org.example.g4_localzero.model.Forum;
import org.example.g4_localzero.model.Initiative;
import org.example.g4_localzero.model.User;
import org.example.g4_localzero.repository.ForumRepository;
import org.example.g4_localzero.repository.InitiativeRepository;
import org.example.g4_localzero.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InitiativeService {

    private final InitiativeRepository initiativeRepository;
    private final UserRepository userRepository;
    private final ForumRepository forumRepository;
    private final ForumAccessPolicy forumAccessPolicy;

    public InitiativeService(InitiativeRepository initiativeRepository,
                             UserRepository userRepository,
                             ForumRepository forumRepository,
                             ForumAccessPolicy forumAccessPolicy) {
        this.initiativeRepository = initiativeRepository;
        this.userRepository = userRepository;
        this.forumRepository = forumRepository;
        this.forumAccessPolicy = forumAccessPolicy;
    }

    public List<Initiative> getVisibleInitiatives(User user) {
        List<Initiative> allInitiatives = initiativeRepository.findAll();
        List<Initiative> visibleInitiatives = new ArrayList<>();

        boolean isOrganizer = user.getRoles() != null && user.getRoles().contains(UserRole.COMMUNITY_ORGANIZER);

        for (Initiative initiative : allInitiatives) {

            if (initiative.getForum() != null && forumAccessPolicy.canViewForum(user, initiative.getForum())){

            boolean isMyNeighbourhood = initiative.getNeighborhood() != null && initiative.getNeighborhood().equalsIgnoreCase(user.getLocation());


            if (isOrganizer || isMyNeighbourhood){
                visibleInitiatives.add(initiative);
            }
            }
        }

        return visibleInitiatives;
    }

    public Initiative createInitiative(Initiative initiative, User sessionUser) {
        User creator = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        initiative.setCreator(creator);

        placeInitiativeInCorrectForum(initiative, creator);

        initiative.getParticipants().add(creator);

        return initiativeRepository.save(initiative);
    }

    public Initiative joinInitiative(Long id, User sessionUser) {
        Initiative initiative = initiativeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Initiative not found"));

        User user = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (initiative.getForum() == null
                || !forumAccessPolicy.canViewForum(user, initiative.getForum())) {
            throw new RuntimeException("You are not allowed to join this initiative");
        }

        initiative.getParticipants().add(user);

        return initiativeRepository.save(initiative);
    }

    public Initiative leaveInitiative(Long id, User sessionUser){
        Initiative initiative = initiativeRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Initiative not found"));

        User user = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        initiative.getParticipants().remove(user);
        return initiativeRepository.save(initiative);
    }

    public Initiative updateInitiative(Long id, Initiative updated, User sessionUser) {
        Initiative initiative = initiativeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Initiative not found"));

        User creator = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (initiative.getCreator() == null
                || !initiative.getCreator().getId().equals(sessionUser.getId())) {
            throw new RuntimeException("Only the creator can edit this initiative");
        }

        initiative.setTitle(updated.getTitle());
        initiative.setDescription(updated.getDescription());
        initiative.setLocation(updated.getLocation());
        initiative.setDuration(updated.getDuration());
        initiative.setCategory(updated.getCategory());
        initiative.setVisibility(updated.getVisibility());

        placeInitiativeInCorrectForum(initiative, creator);

        return initiativeRepository.save(initiative);
    }

    public void deleteInitiative(Long id, User sessionUser) {
        Initiative initiative = initiativeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Initiative not found"));

        if (initiative.getCreator() == null
                || !initiative.getCreator().getId().equals(sessionUser.getId())) {
            throw new RuntimeException("Only the creator can delete this initiative");
        }

        initiativeRepository.delete(initiative);
    }

    private void placeInitiativeInCorrectForum(Initiative initiative, User creator) {
        if ("Public".equalsIgnoreCase(initiative.getVisibility())) {
            Forum globalForum = forumRepository.findByForumType(ForumType.GLOBAL)
                    .orElseThrow(() -> new RuntimeException("Global forum not found"));

            initiative.setVisibility("Public");
            initiative.setNeighborhood(null);
            initiative.setForum(globalForum);
        } else {
            Forum neighborhoodForum = forumRepository
                    .findByNeighborhoodIgnoreCase(creator.getLocation())
                    .orElseThrow(() -> new RuntimeException("Neighborhood forum not found"));

            initiative.setVisibility("Neighborhood");
            initiative.setNeighborhood(creator.getLocation());
            initiative.setForum(neighborhoodForum);
        }
    }
}