package com.somesoftwareteam.graphql.assemblers;

import com.somesoftwareteam.graphql.entities.Property;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;


@Component
public class PropertyModelAssembler implements RepresentationModelAssembler<Property, EntityModel<Property>> {

    private final RepositoryEntityLinks repositoryEntityLinks;

    public PropertyModelAssembler(RepositoryEntityLinks repositoryEntityLinks) {
        this.repositoryEntityLinks = repositoryEntityLinks;
    }

    @NotNull
    @Override
    public EntityModel<Property> toModel(@NotNull Property property) {
        return EntityModel.of(property,
                repositoryEntityLinks.linkToItemResource(Property.class, property.getId()).withSelfRel(),
                repositoryEntityLinks.linkToCollectionResource(Property.class).withRel("properties"));
    }
}