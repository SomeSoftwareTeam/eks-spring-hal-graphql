package com.somesoftwareteam.graphql.rest.assemblers;

import com.somesoftwareteam.graphql.datasources.mysql.entities.Document;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;


@Component
public class DocumentModelAssembler implements RepresentationModelAssembler<Document, EntityModel<Document>> {

    private final RepositoryEntityLinks repositoryEntityLinks;

    public DocumentModelAssembler(RepositoryEntityLinks repositoryEntityLinks) {
        this.repositoryEntityLinks = repositoryEntityLinks;
    }

    @NonNull
    @Override
    public EntityModel<Document> toModel(@NonNull Document document) {
        return EntityModel.of(document,
                repositoryEntityLinks.linkToItemResource(Document.class, document.getId()).withSelfRel(),
                repositoryEntityLinks.linkToCollectionResource(Document.class).withRel("documents"));
    }
}