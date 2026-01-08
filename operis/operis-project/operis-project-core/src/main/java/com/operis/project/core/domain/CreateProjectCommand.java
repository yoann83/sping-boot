package com.operis.project.core.domain;

// enveloppe les informations nécessaires à la création d'un projet
public record CreateProjectCommand(String name, String description, String Owner) {
}
