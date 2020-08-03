package com.somesoftwareteam.graphql.rest.controllers;

import com.somesoftwareteam.graphql.datasources.mysql.acl.MyAclService;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Item;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Item;
import com.somesoftwareteam.graphql.datasources.mysql.repositories.EntityCreator;
import com.somesoftwareteam.graphql.rest.assemblers.ItemModelAssembler;
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
public class ItemController {

    private final MyAclService myAclService;
    private final EntityCreator entityCreator;
    private final ItemModelAssembler itemModelAssembler;

    ItemController(MyAclService aclManager, EntityCreator entityCreator, ItemModelAssembler itemModelAssembler) {
        this.myAclService = aclManager;
        this.entityCreator = entityCreator;
        this.itemModelAssembler = itemModelAssembler;
    }

    @PostMapping("/items")
    @PreAuthorize("hasAuthority('SCOPE_write:items')")
    public ResponseEntity<?> newItem(@RequestBody Item item) {
        Item newItem = entityCreator.setOwnerAndPersistEntity(item);
        myAclService.createAccessControlList(Item.class, newItem.getId());
        EntityModel<Item> model = itemModelAssembler.toModel(item);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }
}
