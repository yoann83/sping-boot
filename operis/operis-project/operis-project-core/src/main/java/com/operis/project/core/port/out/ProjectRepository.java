package com.operis.project.core.port.out;

import com.operis.project.core.domain.Project;

public interface ProjectRepository {
    void create(Project project);
}
