package com.somesoftwareteam.graphql.rest.assemblers;

import com.somesoftwareteam.graphql.datasources.mysql.entities.Document;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Property;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class DocumentModelAssembler implements RepresentationModelAssembler<Document, EntityModel<Document>> {

    private final RepositoryEntityLinks repositoryEntityLinks;

    public DocumentModelAssembler(RepositoryEntityLinks repositoryEntityLinks) {
        this.repositoryEntityLinks = repositoryEntityLinks;
    }

    @NonNull
    @Override
    public EntityModel<Document> toModel(@NonNull Document document) {

        List<Link> links = new ArrayList<>();
        links.add(repositoryEntityLinks.linkToItemResource(Document.class, document.getId()).withSelfRel());
        links.add(repositoryEntityLinks.linkToCollectionResource(Document.class).withRel("documents"));

        if (!Objects.isNull(document.getProperty()))
            links.add(repositoryEntityLinks.linkToItemResource(Property.class, document.getProperty().getId()));

        return EntityModel.of(document, links);
    }
}