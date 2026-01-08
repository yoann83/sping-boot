package com.operis.project.adapter.in.rest.payload;

import jakarta.validation.constraints.NotBlank;

public record CreateProjectPayload(
        @NotBlank String name,
        String description
    ) {
}
