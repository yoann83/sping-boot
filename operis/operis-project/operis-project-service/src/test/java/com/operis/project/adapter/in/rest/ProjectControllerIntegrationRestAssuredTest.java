package com.operis.project.adapter.in.rest;

// TEST AVEC Rest Assured
import com.operis.project.adapter.in.rest.dto.ProjectDto;
import com.operis.project.adapter.in.rest.error.ApiError;
import com.operis.project.adapter.in.rest.payload.CreateProjectPayload;
import com.operis.project.adapter.infrastructure.jwt.JWTConnectedUserResolver;
import com.operis.project.adapter.out.persistence.JPAProjectRepository;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProjectControllerIntegrationRestAssuredTest {

    // Token fictif (le contenu n'importe pas car on mocke le resolver)
    private static final String BEARER_TOKEN = "Bearer token-ignore";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JPAProjectRepository jpaProjectRepository;

    // On mocke le composant de sécurité pour contourner la validation JWT réelle
    @MockitoBean
    private JWTConnectedUserResolver jwtConnectedUserResolver;

    /**
     * Configuration indispensable pour utiliser Rest Assured avec @WebMvcTest ou @SpringBootTest
     * sans lancer un vrai serveur HTTP.
     */
    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    void createProject_ShouldReturn201_WhenParamsAreValid() {
        // GIVEN
        CreateProjectPayload payload = new CreateProjectPayload("RestAssured Project", "Testing with style");

        Mockito.when(jwtConnectedUserResolver.extractConnectedUserEmail(any()))
                .thenReturn("yoann.test@gmail.com");

        // WHEN & THEN
        ProjectDto createdProject = given()
                .header("Authorization", BEARER_TOKEN)
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/api/projects")
                .then()
                .log().ifValidationFails() // Affiche les logs si ça plante (super utile !)
                .status(HttpStatus.CREATED)
                .extract()
                .as(ProjectDto.class); // Extraction directe en objet Java

        // ASSERTIONS
        assertNotNull(createdProject.id());
        assertEquals("RestAssured Project", createdProject.name());
        assertEquals("yoann.test@gmail.com", createdProject.owner());

        // Vérification optionnelle en base de données
        var entity = jpaProjectRepository.findById(createdProject.id()).orElseThrow();
        assertEquals("RestAssured Project", entity.getName());
    }

    @Test
    void createProject_ShouldReturn400_WhenNameIsEmpty() {
        // GIVEN
        CreateProjectPayload payload = new CreateProjectPayload(" ", "Description");

        Mockito.when(jwtConnectedUserResolver.extractConnectedUserEmail(any()))
                .thenReturn("yoann.test@gmail.com");

        // WHEN & THEN
        ApiError apiError = given()
                .header("Authorization", BEARER_TOKEN)
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/api/projects")
                .then()
                .status(HttpStatus.BAD_REQUEST)
                .extract()
                .as(ApiError.class);

        // ASSERTIONS
        assertEquals(HttpStatus.BAD_REQUEST.name(), apiError.httpStatus());
        assertEquals(HttpStatus.BAD_REQUEST.value(), apiError.statusCode());
        assertEquals("Validation failed for one or more arguments.", apiError.message());
    }
}