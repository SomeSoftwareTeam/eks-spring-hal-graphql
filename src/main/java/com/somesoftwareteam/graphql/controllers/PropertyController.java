package com.somesoftwareteam.graphql.controllers;

import com.somesoftwareteam.graphql.acl.MyAclService;
import com.somesoftwareteam.graphql.assemblers.PropertyModelAssembler;
import com.somesoftwareteam.graphql.entities.Property;
import com.somesoftwareteam.graphql.repositories.EntityCreator;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * https://spring.io/guides/tutorials/rest/
 */
@RepositoryRestController
public class PropertyController {

    private final MyAclService aclManager;
    private final EntityCreator entityCreator;
    private final PropertyModelAssembler assembler;

    PropertyController(MyAclService aclManager, EntityCreator entityCreator, PropertyModelAssembler assembler) {
        this.aclManager = aclManager;
        this.entityCreator = entityCreator;
        this.assembler = assembler;
    }

    @PostMapping("/properties")
    @PreAuthorize("hasAuthority('SCOPE_write:properties')")
    public ResponseEntity<?> newFixture(@RequestBody Property property) {
        Property newProperty = entityCreator.persistEntity(property);
        aclManager.createOrUpdateAccessControlListToIncludeCurrentSecurityIdentity(newProperty);
        EntityModel<Property> model = assembler.toModel(property);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }
}
