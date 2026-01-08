package com.operis.project.adapter.in.rest.dto;

import java.util.List;

public record ProjectDto(
        String id,
        String name,
        String description,
        String owner,
        List<String> members
) {
}