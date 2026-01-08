package com.operis.project.adapter.in.rest;

import com.operis.project.adapter.in.rest.dto.ProjectDto;
import com.operis.project.core.domain.CreateProjectCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.operis.project.adapter.in.rest.payload.CreateProjectPayload;
import com.operis.project.core.domain.Project;
import com.operis.project.core.port.in.CreateProjectUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private CreateProjectUseCase createProjectUseCase;

    public ResponseEntity<ProjectDto> createProject(@Valid CreateProjectPayload payload) {

        // Etape 1 : Arrivée du JSON (Payload) -> Le controller le reçoit propre grâce à @Valid

        // Etape 2 : Transformation en objet Java Pur (Command) -> Le "Point d'entrée"
        String connectedUser = "admin"; // Simulé pour l'instant
        CreateProjectCommand command = new CreateProjectCommand(
                payload.name(),
                payload.description(),
                connectedUser
        );

        // Etape 3 : Appel du Guichet (UseCase) -> On passe l'objet pour lancer le travail métier
        // Le UseCase nous rend l'objet "Project" qui a été créé et validé par le Core (safe)
        Project project = createProjectUseCase.createProject(command);

        // Etape 4 : Transformation pour la sortie (DTO)
        // On ne rend pas l'objet métier brut, on le met dans data transfert object (DTO) pour le client
        ProjectDto responseDto = new ProjectDto(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getOwner(),
                project.getMembers()
        );

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
