package com.operis.project.adapter.out.persistence;

import com.operis.project.core.domain.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// componentModel = "spring" permet de l'injecter avec @Autowired ou le constructeur
@Mapper(componentModel = "spring")
public interface ProjectEntityMapper {

    // MapStruct fait le code : entity.setName(project.getName()), etc.
    // ID qui le gère
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ownerEmail", source = "owner") // Si les noms diffèrent, on précise
    ProjectEntity toEntity(Project project);

    // pour la lecture
    @Mapping(target = "owner", source = "ownerEmail")
    Project toDomain(ProjectEntity entity);
}