package com.somesoftwareteam.graphql.rest.assemblers;

import com.somesoftwareteam.graphql.datasources.mysql.acl.Entry;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AclModelAssembler implements
        RepresentationModelAssembler<Entry, EntityModel<Entry>> {

    private final EntityLinks entityLinks;

    public AclModelAssembler(EntityLinks entityLinks) {
        this.entityLinks = entityLinks;
    }

    @NonNull
    @Override
    public EntityModel<Entry> toModel(@NonNull Entry accessControlEntry) {
        List<Link> links = new ArrayList<>();
        return EntityModel.of(accessControlEntry, links);
    }
}