package com.somesoftwareteam.graphql.rest.controllers;

import com.somesoftwareteam.graphql.datasources.mysql.acl.MyAclService;
import com.somesoftwareteam.graphql.rest.assemblers.PropertyModelAssembler;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Property;
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
public class PropertyController {

    private final MyAclService myAclService;
    private final EntityCreator entityCreator;
    private final PropertyModelAssembler assembler;

    PropertyController(MyAclService aclManager, EntityCreator entityCreator, PropertyModelAssembler assembler) {
        this.myAclService = aclManager;
        this.entityCreator = entityCreator;
        this.assembler = assembler;
    }

    @PostMapping("/properties")
    @PreAuthorize("hasAuthority('SCOPE_write:properties')")
    public ResponseEntity<?> newFixture(@RequestBody Property property) {
        Property newProperty = entityCreator.setOwnerAndPersistEntity(property);
        myAclService.createAccessControlList(Property.class, newProperty.getId());
        EntityModel<Property> model = assembler.toModel(property);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }
}
