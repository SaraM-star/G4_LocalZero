package org.example.g4_localzero.service;

import org.example.g4_localzero.model.Initiative;
import org.example.g4_localzero.model.User;
import org.example.g4_localzero.repository.InitiativeRepository;
import org.example.g4_localzero.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InitiativeService {

    private final InitiativeRepository initiativeRepository;
    private final UserRepository userRepository;

    public InitiativeService(InitiativeRepository initiativeRepository, UserRepository userRepository) {
        this.initiativeRepository = initiativeRepository;
        this.userRepository = userRepository;
    }

    public List<Initiative> getVisibleInitiatives(User user) {
        return initiativeRepository.findAll()
                .stream()
                .filter(initiative ->
                        "Public".equalsIgnoreCase(initiative.getVisibility())
                                || initiative.getNeighborhood() == null
                                || initiative.getNeighborhood().equals(user.getLocation())
                )
                .toList();
    }

    public Initiative createInitiative(Initiative initiative, User sessionUser) {
        User creator = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isCommunityOrganizer = creator.getRoles()
                .stream()
                .anyMatch(role -> role.name().equals("COMMUNITY_ORGANIZER"));

        initiative.setCreator(creator);
        initiative.setNeighborhood(creator.getLocation());

        if (!isCommunityOrganizer) {
            initiative.setVisibility("Neighborhood");
        }

        initiative.getParticipants().add(creator);

        return initiativeRepository.save(initiative);
    }

    public Initiative joinInitiative(Long id, User sessionUser) {
        Initiative initiative = initiativeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Initiative not found"));

        User user = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        initiative.getParticipants().add(user);

        return initiativeRepository.save(initiative);
    }

    public Initiative updateInitiative(Long id, Initiative updated, User sessionUser) {
        Initiative initiative = initiativeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Initiative not found"));

        if (initiative.getCreator() == null || !initiative.getCreator().getId().equals(sessionUser.getId())) {
            throw new RuntimeException("Only the creator can edit this initiative");
        }

        initiative.setTitle(updated.getTitle());
        initiative.setDescription(updated.getDescription());
        initiative.setLocation(updated.getLocation());
        initiative.setDuration(updated.getDuration());
        initiative.setCategory(updated.getCategory());
        initiative.setVisibility(updated.getVisibility());

        return initiativeRepository.save(initiative);
    }

    public void deleteInitiative(Long id, User sessionUser) {
        Initiative initiative = initiativeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Initiative not found"));

        if (initiative.getCreator() == null || !initiative.getCreator().getId().equals(sessionUser.getId())) {
            throw new RuntimeException("Only the creator can delete this initiative");
        }

        initiativeRepository.delete(initiative);
    }
}