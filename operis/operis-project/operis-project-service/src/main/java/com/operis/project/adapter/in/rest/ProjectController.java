package com.operis.project.adapter.in.rest;

import com.operis.project.adapter.in.rest.dto.ProjectDto;
import com.operis.project.adapter.in.rest.error.ApiError;
import com.operis.project.adapter.infrastructure.jwt.JWTConnectedUserResolver;
import com.operis.project.core.domain.CreateProjectCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.operis.project.adapter.in.rest.payload.CreateProjectPayload;
import com.operis.project.core.domain.Project;
import com.operis.project.core.port.in.CreateProjectUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/projects")
@Tag(name = "Gesion de projet", description = "APIs pour la gestion de projets")
public class ProjectController {

    private final CreateProjectUseCase createProjectUseCase;
    private final JWTConnectedUserResolver jwtConnectedUserResolver;

    @PostMapping
    @Operation(summary = "Créer un nouveau projet", responses = {
            // Responses documentées pour Swagger/OpenAPI
            @ApiResponse(
                    responseCode = "201",
                    description = "Projet créé avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requête invalide",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Non autorisé",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erreur serveur",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
            )
    })
    public ResponseEntity<ProjectDto> createProject(
            @Valid @RequestBody CreateProjectPayload payload,
            @Parameter(description = "Payload pour créer un projet", hidden = true)
            @RequestHeader("Authorization") String authorizationHeader) {

        // Etape 1 : Arrivée du JSON (Payload) -> Le controller le reçoit propre grâce à @Valid

        // Etape 2 : Transformation en objet Java Pur (Command) -> Le "Point d'entrée"

        String connectedUser = jwtConnectedUserResolver.extractConnectedUserEmail(authorizationHeader); // Simulé pour l'instant

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
