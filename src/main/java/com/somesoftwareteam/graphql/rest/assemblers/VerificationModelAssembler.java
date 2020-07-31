package com.somesoftwareteam.graphql.rest.assemblers;

import com.somesoftwareteam.graphql.datasources.mysql.entities.Property;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Verification;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class VerificationModelAssembler implements RepresentationModelAssembler<Verification, EntityModel<Verification>> {

    private final RepositoryEntityLinks repositoryEntityLinks;

    public VerificationModelAssembler(RepositoryEntityLinks repositoryEntityLinks) {
        this.repositoryEntityLinks = repositoryEntityLinks;
    }

    @NonNull
    @Override
    public EntityModel<Verification> toModel(@NonNull Verification verification) {

        List<Link> links = new ArrayList<>();
        links.add(repositoryEntityLinks.linkToItemResource(Verification.class, verification.getId()).withSelfRel());
        links.add(repositoryEntityLinks.linkToCollectionResource(Verification.class).withRel("verifications"));

        if (!Objects.isNull(verification.getProperty()))
            links.add(repositoryEntityLinks.linkToItemResource(Property.class, verification.getProperty().getId()));

        return EntityModel.of(verification, links);
    }
}