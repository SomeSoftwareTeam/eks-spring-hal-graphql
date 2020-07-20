package com.somesoftwareteam.graphql.rest.controllers;

import com.somesoftwareteam.graphql.datasources.mysql.acl.MyAclService;
import com.somesoftwareteam.graphql.rest.assemblers.FixtureModelAssembler;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Fixture;
import com.somesoftwareteam.graphql.datasources.mysql.repositories.EntityCreator;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * https://spring.io/guides/tutorials/rest/
 */
@RepositoryRestController
public class FixtureController {

    private final MyAclService aclManager;
    private final EntityCreator entityCreator;
    private final FixtureModelAssembler fixtureModelAssembler;

    FixtureController(MyAclService aclManager, EntityCreator entityCreator,
                      FixtureModelAssembler fixtureModelAssembler) {
        this.aclManager = aclManager;
        this.entityCreator = entityCreator;
        this.fixtureModelAssembler = fixtureModelAssembler;
    }

    @PostMapping("/fixtures")
    @PreAuthorize("hasAuthority('SCOPE_write:fixtures')")
    public ResponseEntity<?> newFixture(@RequestBody Fixture fixture) {
        Fixture newFixture = entityCreator.persistEntity(fixture);
        aclManager.createOrUpdateAccessControlListToIncludeCurrentSecurityIdentity(newFixture);
        EntityModel<Fixture> model = fixtureModelAssembler.toModel(fixture);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }
}
