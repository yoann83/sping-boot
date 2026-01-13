package com.operis.project.adapter.config;

import com.operis.project.core.port.in.CreateProjectUseCase;
import com.operis.project.core.port.out.ProjectRepository;
import com.operis.project.core.usecase.CreateProjectUseCaseAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectAdapterConfig {

    @Bean
    public CreateProjectUseCase createProjectUseCase(ProjectRepository projectRepository) {
        return new CreateProjectUseCaseAdapter(projectRepository);
    }
}
