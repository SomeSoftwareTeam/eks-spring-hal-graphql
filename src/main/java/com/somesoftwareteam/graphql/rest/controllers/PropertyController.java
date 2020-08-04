package com.somesoftwareteam.graphql.rest.controllers;

import com.somesoftwareteam.graphql.datasources.mysql.acl.Entry;
import com.somesoftwareteam.graphql.datasources.mysql.acl.MyAclService;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Property;
import com.somesoftwareteam.graphql.datasources.mysql.repositories.EntityCreator;
import com.somesoftwareteam.graphql.rest.assemblers.AclModelAssembler;
import com.somesoftwareteam.graphql.rest.assemblers.PropertyModelAssembler;
import org.modelmapper.ModelMapper;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

/**
 * https://spring.io/guides/tutorials/rest/
 */
@RepositoryRestController
@PreAuthorize("hasAuthority('SCOPE_read:properties')")
public class PropertyController {

    private final MyAclService myAclService;
    private final EntityCreator entityCreator;
    private final ModelMapper modelMapper;
    private final PropertyModelAssembler assembler;
    private final AclModelAssembler aclModelAssembler;

    public PropertyController(MyAclService myAclService, EntityCreator entityCreator, ModelMapper modelMapper,
                              PropertyModelAssembler assembler, AclModelAssembler aclModelAssembler) {
        this.myAclService = myAclService;
        this.entityCreator = entityCreator;
        this.modelMapper = modelMapper;
        this.assembler = assembler;
        this.aclModelAssembler = aclModelAssembler;
    }

    @PostMapping("/properties")
    @PreAuthorize("hasAuthority('SCOPE_write:properties')")
    public ResponseEntity<?> newFixture(@RequestBody Property property) {
        Property newProperty = entityCreator.setOwnerAndPersistEntity(property);
        myAclService.createAccessControlList(Property.class, newProperty.getId());
        EntityModel<Property> model = assembler.toModel(property);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @GetMapping("/properties/{propertyId}/accesscontrolentriesd")
    public ResponseEntity<?> getAcl(@PathVariable Long propertyId) {
        Acl acl = myAclService.getAcl(Property.class, propertyId);
        List<EntityModel<Entry>> models =
                acl.getEntries().stream().map(this::convertToEntityModel).collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(models));
    }

//    @PostMapping("/properties/{propertyId}/acl")
//    public void something(@PathVariable Long propertyId, @RequestBody Member member) {
//        myAclService.createReadPermissionAccessControlEntry(propertyId, member.getName());
//    }

    private EntityModel<Entry> convertToEntityModel(AccessControlEntry entry) {
        Entry dto = modelMapper.map(entry, Entry.class);
        dto.setSid(entry.getSid().toString());
        dto.setPermission(entry.getPermission().getPattern());
        return aclModelAssembler.toModel(dto);
    }
}
