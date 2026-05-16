package org.example.g4_localzero.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Initiative {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 1000)
    private String description;

    private String location;
    private String category;
    private String visibility;
    private String duration;
    private String neighborhood;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    @JsonIgnoreProperties({"password", "roles"})
    private User creator;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "initiative_participants",
            joinColumns = @JoinColumn(name = "initiative_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnoreProperties({"password", "roles"})
    private Set<User> participants = new HashSet<>();


    public Initiative(){}

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title=title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description=description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location=location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category=category;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility=visibility;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration=duration;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator=creator;
    }

    public Set<User> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<User> participants) {
        this.participants=participants;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }
}