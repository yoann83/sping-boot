package com.operis.project.core.usecase;

import com.operis.project.core.domain.CreateProjectCommand;
import com.operis.project.core.domain.Project;
import com.operis.project.core.port.out.ProjectRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateProjectUseCaseAdapterTest {

    // mock is a fake implementation that we can use to verify interactions
    @Mock
    private ProjectRepository projectRepository;

    // injection of mocks into the class we want to test
    @InjectMocks
    private CreateProjectUseCaseAdapter createProjectUseCase;

    @Test
    void createProjectShouldSucceedGivenValidParams() {
        // GIVEN (scenario)
        CreateProjectCommand commande = new CreateProjectCommand(
                "Operis",
                "This is a new project.",
                "yoann.test@gmail.com"
        );

        // WHEN (action)
        createProjectUseCase.createProject(commande);

        // THEN (test)
        // Mockito.times(1) verifies that the create method was called exactly once
        // default verification is times(1)
        // Mockito.verify(projectRepository, Mockito.times(1)).create(actualProject);

        // ArgumentCaptor captures the argument passed to the create method
        ArgumentCaptor<Project> projectArgumentCaptor = ArgumentCaptor.forClass(Project.class);
        verify(projectRepository).create(projectArgumentCaptor.capture());
        Project projectCaptured = projectArgumentCaptor.getValue();

        assertThat(projectCaptured.getId()).isNull();
        assertThat(projectCaptured.getName()).isEqualTo("Operis");
        assertThat(projectCaptured.getDescription()).isEqualTo("This is a new project.");
        assertThat(projectCaptured.getOwner()).isEqualTo("yoann.test@gmail.com");
        assertThat(projectCaptured.getMembers()).containsExactlyInAnyOrder("yoann.test@gmail.com");

    }

    @ParameterizedTest
    @CsvSource({
            "' ', yoann.test@gmail.com, Project name cannot be null or empty.",
            ", yoann.test@gmail.com, Project name cannot be null or empty.",
            "Operis, ' ', Project owner cannot be null or empty.",
            "Operis, , Project owner cannot be null or empty."
    })
    void createProjectShouldThrowExceptionGivenInvalidParams(String name, String owner, String expectedMessage) {
        // GIVEN (scenario)
        CreateProjectCommand commande = new CreateProjectCommand(
                name,
                "This is a new project.",
                owner
        );

        // WHEN (action)
        IllegalArgumentException actualException = assertThrows(IllegalArgumentException.class, () -> {
            createProjectUseCase.createProject(commande);
        });

        // THEN (test)
        assertThat(actualException.getMessage()).isEqualTo(expectedMessage);


    }
}