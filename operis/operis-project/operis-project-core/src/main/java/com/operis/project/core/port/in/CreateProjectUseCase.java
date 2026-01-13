package com.operis.project.core.port.in;

import com.operis.project.core.domain.CreateProjectCommand;
import com.operis.project.core.domain.Project;

// code métier pour la création d'un projet
public interface CreateProjectUseCase {
    Project createProject(CreateProjectCommand command);
}
