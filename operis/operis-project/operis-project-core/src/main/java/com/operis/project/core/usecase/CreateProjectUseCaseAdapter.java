package com.operis.project.core.usecase;

import com.operis.project.core.domain.CreateProjectCommand;
import com.operis.project.core.domain.Project;
import com.operis.project.core.port.in.CreateProjectUseCase;
import com.operis.project.core.port.out.ProjectRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateProjectUseCaseAdapter implements CreateProjectUseCase {

    private  final ProjectRepository projectRepository;

    @Override
    public Project createProject(CreateProjectCommand command) {
        Project projectToCreate = new Project(
                command.name(),
                command.description(),
                command.Owner()
        );

        projectRepository.create(projectToCreate);


        return projectToCreate;
    }
}
