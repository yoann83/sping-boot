package com.operis.project.adapter.in.rest;

// TEST AVEC MockMvc
import com.fasterxml.jackson.databind.ObjectMapper;
import com.operis.project.adapter.in.rest.dto.ProjectDto;
import com.operis.project.adapter.in.rest.error.ApiError;
import com.operis.project.adapter.in.rest.payload.CreateProjectPayload;
import com.operis.project.adapter.infrastructure.jwt.JWTConnectedUserResolver;
import com.operis.project.adapter.out.persistence.JPAProjectRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Integration Test
@SpringBootTest // Lancement du contexte complet qui inclut la couche Web et la couche Core
@AutoConfigureMockMvc // Configuration de MockMvc qui permet de faire des appels HTTP simulés
@ActiveProfiles("test") // Utilisation du profil "application-test.yml" pour charger des configurations spécifiques (base de données en mémoire h2)
class ProjectControllerIntegrationTest {
    public static final String BEARER_TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.rHcug6UKnHr6qfoFbTl5opvm49jfQ5Wluvv6KJBsEi4";

    // Pour faire des appels HTTP simulés
    @Autowired
    private MockMvc mockMvc;

    // Pour la sérialisation/désérialisation JSON
    @Autowired
    private ObjectMapper objectMapper;

    // Pour vérifier l'état de la base de données après un appel HTTP
    @Autowired
    private JPAProjectRepository jpaProjectRepository;

    // Mock du resolver de l'utilisateur connecté
    @MockitoBean
    private JWTConnectedUserResolver jwtConnectedUserResolver;

    @Test
    void createProjectShouldWordsSuccessFullyGivenParams() throws Exception {
        // GIVEN
        CreateProjectPayload payload = new CreateProjectPayload("Operis Project", "Project management tool");

        // Simulation de l'utilisateur connecté
        Mockito.when(jwtConnectedUserResolver.extractConnectedUserEmail(any()))
                .thenReturn("yoann.test@gmail.com");

        // WHEN
        MockHttpServletResponse response =  mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON) // constante MediaType
                        .header("Authorization", BEARER_TOKEN) // Le token n'est pas vérifié dans ce test
                        .content(objectMapper.writeValueAsString(payload)) // Sérialisation JSON
                )
                // 1. Vérifie le code HTTP 201
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();
        // THEN
            // depuis le controller on récupère le ProjectDto retourné
        String responseStr = response.getContentAsString();
        ProjectDto createdProject = objectMapper.readValue(responseStr, ProjectDto.class);

        // Vérification dans la base de données
        assertNotNull(createdProject.id());
        assertEquals("Operis Project", createdProject.name());
        assertEquals("Project management tool", createdProject.description());
        assertEquals("yoann.test@gmail.com", createdProject.owner());

        // AND
            // Vérification que le projet a bien été persisté en base de données
        jpaProjectRepository.findById(createdProject.id())
                .ifPresentOrElse(
                        project -> {
                            assertEquals("Operis Project", project.getName());
                            assertEquals("Project management tool", project.getDescription());
                        },
                        () -> fail("Project not found in database")
                );
    }

    @Test
    void createProjectShoulBAD_REQUESTGivenEmptyProjectName() throws Exception {
        // GIVEN
        CreateProjectPayload payload = new CreateProjectPayload(" ", "Project management tool");

        // Simulation de l'utilisateur connecté
        Mockito.when(jwtConnectedUserResolver.extractConnectedUserEmail(any()))
                .thenReturn("yoann.test@gmail.com");

        // WHEN
        MockHttpServletResponse response = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON) // constante MediaType
                        .header("Authorization", BEARER_TOKEN) // Le token n'est pas vérifié dans ce test
                        .content(objectMapper.writeValueAsString(payload)) // Sérialisation JSON
                )
                // THEN
                    // 1. Vérifie le code HTTP 201
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();
        // AND
        String responseStr = response.getContentAsString();
        var apiError = objectMapper.readValue(responseStr, ApiError.class);

        // Vérifie le String "BAD_REQUEST" dans ton JSON
        assertEquals(HttpStatus.BAD_REQUEST.name(), apiError.httpStatus());

        // Vérifie l'entier 400 sur la Réponse HTTP (l'enveloppe)
        assertEquals(HttpStatus.BAD_REQUEST.value(), apiError.statusCode());

        // Vérifie le message
        assertEquals("Validation failed for one or more arguments.", apiError.message());

    }
}