package com.operis.project.core.domain;


import lombok.Data;

import java.util.Arrays;
import java.util.List;

// garde-fou pour appliquer les règles métiers liées aux projets
@Data
public class Project {
    private String id;
    private String name;
    private String description;
    private String owner;
    private List<String> members;

    public Project(String name, String description, String owner) {
        if(name == null || name.isBlank()) {
            throw new IllegalArgumentException("Project name cannot be null or empty.");
        }

        if(owner == null || owner.isBlank()) {
            throw new IllegalArgumentException("Project owner cannot be null or empty.");
        }

        this.name = name;
        this.description = description;
        this.owner = owner;
        this.members = Arrays.asList(owner);
    }
}
