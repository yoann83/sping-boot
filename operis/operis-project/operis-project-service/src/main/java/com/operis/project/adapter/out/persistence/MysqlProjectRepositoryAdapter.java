package com.operis.project.adapter.out.persistence;

import com.operis.project.core.domain.Project;
import com.operis.project.core.port.out.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MysqlProjectRepositoryAdapter implements ProjectRepository {

    private final JPAProjectRepository jpaProjectRepository;
    private final ProjectEntityMapper mapper;

    @Override
    public void create(Project project) {
        // transforme le Métier -> Entity
        ProjectEntity projectEntity = mapper.toEntity(project);

        // sauvegarde et récupèration du résultat (ID généré inclus)
        ProjectEntity savedEntity = jpaProjectRepository.save(projectEntity);

        // met à jour l'objet métier avec l'ID pour le renvoyer au controller
        project.setId(savedEntity.getId());
    }
}