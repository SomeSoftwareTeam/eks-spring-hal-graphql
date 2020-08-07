package com.somesoftwareteam.graphql.rest.assemblers;

import com.somesoftwareteam.graphql.datasources.auth0.Group;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GroupModelAssembler implements RepresentationModelAssembler<Group, EntityModel<Group>> {

    private final EntityLinks entityLinks;

    public GroupModelAssembler(EntityLinks entityLinks) {
        this.entityLinks = entityLinks;
    }

    @NonNull
    @Override
    public EntityModel<Group> toModel(@NonNull Group group) {
        List<Link> links = new ArrayList<>();
        return EntityModel.of(group, links);
    }
}