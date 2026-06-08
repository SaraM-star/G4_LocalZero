package org.example.g4_localzero.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.example.g4_localzero.enums.ForumType;

import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Forum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String neighborhood;

    @Enumerated(EnumType.STRING)
    private ForumType forumType;

    @ManyToOne
    @JsonIgnore
    private Forum parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Forum> children = new ArrayList<>();

    @OneToMany(mappedBy = "forum")
    @JsonIgnore
    private List<Initiative> initiatives = new ArrayList<>();

    public Forum() {
    }

    public Forum(String name, String neighborhood, ForumType forumType) {
        this.name = name;
        this.neighborhood = neighborhood;
        this.forumType = forumType;
    }

    public void addChild(Forum child) {
        children.add(child);
        child.setParent(this);
    }

    public void removeChild(Forum child) {
        children.remove(child);
        child.setParent(null);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public ForumType getForumType() {
        return forumType;
    }

    public Forum getParent() {
        return parent;
    }

    public List<Forum> getChildren() {
        return children;
    }

    public List<Initiative> getInitiatives() {
        return initiatives;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public void setForumType(ForumType forumType) {
        this.forumType = forumType;
    }

    public void setParent(Forum parent) {
        this.parent = parent;
    }

    public void setChildren(List<Forum> children) {
        this.children = children;
    }

    public void setInitiatives(List<Initiative> initiatives) {
        this.initiatives = initiatives;
    }
}