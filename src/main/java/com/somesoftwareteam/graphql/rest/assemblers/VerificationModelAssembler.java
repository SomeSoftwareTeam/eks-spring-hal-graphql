package com.somesoftwareteam.graphql.rest.assemblers;

import com.somesoftwareteam.graphql.datasources.mysql.entities.Verification;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class VerificationModelAssembler implements RepresentationModelAssembler<Verification, EntityModel<Verification>> {

    private final RepositoryEntityLinks repositoryEntityLinks;

    public VerificationModelAssembler(RepositoryEntityLinks repositoryEntityLinks) {
        this.repositoryEntityLinks = repositoryEntityLinks;
    }

    @NonNull
    @Override
    public EntityModel<Verification> toModel(@NonNull Verification verification) {
        return EntityModel.of(verification,
                repositoryEntityLinks.linkToItemResource(Verification.class, verification.getId()).withSelfRel(),
                repositoryEntityLinks.linkToCollectionResource(Verification.class).withRel("verifications"));
    }
}