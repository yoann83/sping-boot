package com.operis.project.adapter.out.persistence;

import com.operis.project.core.domain.Project;
import com.operis.project.core.port.out.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MysqlProjectRepositoryAdapter implements ProjectRepository {

    private final JPAProjectRepository jpaProjectRepository;

    @Override
    public void create(Project project) {
        // Implémentation de la persistance du projet (ex: sauvegarde en base de données)
        ProjectEntity projectEntity = new ProjectEntity();
        jpaProjectRepository.save(projectEntity);
    }
}
