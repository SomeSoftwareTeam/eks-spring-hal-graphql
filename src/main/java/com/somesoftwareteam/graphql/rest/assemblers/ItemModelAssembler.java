package com.somesoftwareteam.graphql.rest.assemblers;

import com.somesoftwareteam.graphql.datasources.mysql.entities.Fixture;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Item;
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
public class ItemModelAssembler implements RepresentationModelAssembler<Item, EntityModel<Item>> {

    private final RepositoryEntityLinks repositoryEntityLinks;

    public ItemModelAssembler(RepositoryEntityLinks repositoryEntityLinks) {
        this.repositoryEntityLinks = repositoryEntityLinks;
    }

    @NonNull
    @Override
    public EntityModel<Item> toModel(@NonNull Item item) {

        List<Link> links = new ArrayList<>();
        links.add(repositoryEntityLinks.linkToItemResource(Item.class, item.getId()).withSelfRel());
        links.add(repositoryEntityLinks.linkToCollectionResource(Item.class).withRel("items"));

        if (!Objects.isNull(item.getProperty()))
            links.add(repositoryEntityLinks.linkToItemResource(Property.class, item.getProperty().getId()));

        return EntityModel.of(item, links);
    }
}