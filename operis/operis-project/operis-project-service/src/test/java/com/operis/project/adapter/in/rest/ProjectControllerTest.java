package com.operis.project.adapter.in.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.operis.project.adapter.in.rest.payload.CreateProjectPayload;
import com.operis.project.adapter.infrastructure.jwt.JWTConnectedUserResolver;
import com.operis.project.core.domain.Project;
import com.operis.project.core.port.in.CreateProjectUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType; // Important pour le contentType
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath; // Pour vérifier le JSON
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateProjectUseCase createProjectUseCase;

    @MockitoBean
    private JWTConnectedUserResolver jwtConnectedUserResolver;

    @Test
    void createProjectShouldReturnCREATED() throws Exception {
        // GIVEN
        CreateProjectPayload payload = new CreateProjectPayload(
                "Operis Project",
                "Project management tool"
        );

        // Simulation de l'utilisateur connecté
        Mockito.when(jwtConnectedUserResolver.extractConnectedUserEmail(any()))
                .thenReturn("yoann.test@gmail.com");

        // Simulation du UseCase (Core)
        Project createdProject = new Project(
                "Operis Project",
                "Project management tool",
                "yoann.test@gmail.com"
        );
        Mockito.when(createProjectUseCase.createProject(any()))
                .thenReturn(createdProject);

        // WHEN & THEN
        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON) // constante MediaType
                        .header("Authorization", "Bearer dummy-token") // Le token n'est pas vérifié dans ce test
                        .content(objectMapper.writeValueAsString(payload)) // Sérialisation JSON
                )
                // 1. Vérifie le code HTTP 201
                .andExpect(status().isCreated())

                // 2. Vérifie le contenu du JSON retourné
                // "$" représente la racine du JSON
                .andExpect(jsonPath("$.name").value("Operis Project"))
                .andExpect(jsonPath("$.owner").value("yoann.test@gmail.com"));

        // 3. Vérifie que le UseCase a bien été appelé
        Mockito.verify(createProjectUseCase).createProject(any());
    }
}