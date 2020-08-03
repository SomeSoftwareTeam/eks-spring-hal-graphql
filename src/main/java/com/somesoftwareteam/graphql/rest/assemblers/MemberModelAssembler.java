package com.somesoftwareteam.graphql.rest.assemblers;

import com.somesoftwareteam.graphql.datasources.auth0.Member;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Item;
import org.geolatte.geom.M;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.core.ControllerEntityLinks;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class MemberModelAssembler implements RepresentationModelAssembler<Member, EntityModel<Member>> {

    private final EntityLinks entityLinks;

    public MemberModelAssembler(EntityLinks entityLinks) {
        this.entityLinks = entityLinks;
    }

    @NonNull
    @Override
    public EntityModel<Member> toModel(@NonNull Member member) {
        List<Link> links = new ArrayList<>();
        return EntityModel.of(member, links);
    }
}