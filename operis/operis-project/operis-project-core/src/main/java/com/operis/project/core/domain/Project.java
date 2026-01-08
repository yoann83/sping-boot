package com.operis.project.core.domain;


import lombok.Data;

import java.util.List;

// garde-fou pour appliquer les règles métiers liées aux projets
@Data
public class Project {
    private String id;
    private String name;
    private String description;
    private String owner;
    private List<String> members;
}
