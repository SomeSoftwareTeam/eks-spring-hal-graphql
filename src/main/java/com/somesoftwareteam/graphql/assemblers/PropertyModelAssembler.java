package com.somesoftwareteam.graphql.assemblers;

import com.somesoftwareteam.graphql.entities.Property;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;


@Component
public class PropertyModelAssembler implements RepresentationModelAssembler<Property, EntityModel<Property>> {

    private final RepositoryEntityLinks repositoryEntityLinks;

    public PropertyModelAssembler(RepositoryEntityLinks repositoryEntityLinks) {
        this.repositoryEntityLinks = repositoryEntityLinks;
    }

    @NonNull
    @Override
    public EntityModel<Property> toModel(@NonNull Property property) {
        return EntityModel.of(property,
                repositoryEntityLinks.linkToItemResource(Property.class, property.getId()).withSelfRel(),
                repositoryEntityLinks.linkToCollectionResource(Property.class).withRel("properties"));
    }
}