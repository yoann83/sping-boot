package com.operis.project.adapter.in.rest.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CreateProjectPayload(
        // Swagger documentation for the 'name' field
        @Schema(
                description = "Nom du projet",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "Nouveau Projet"
        )
        @NotBlank String name,

        @Schema(
                description = "Description du projet",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                example = "Ceci est un projet important."
        )
        String description
    ) {
}
