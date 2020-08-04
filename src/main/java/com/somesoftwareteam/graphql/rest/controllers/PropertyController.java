package com.somesoftwareteam.graphql.rest.controllers;

import com.google.maps.errors.ApiException;
import com.google.maps.model.PlacesSearchResult;
import com.somesoftwareteam.graphql.datasources.google.GooglePlacesWrapper;
import com.somesoftwareteam.graphql.datasources.mysql.acl.Entry;
import com.somesoftwareteam.graphql.datasources.mysql.acl.MyAclService;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Property;
import com.somesoftwareteam.graphql.datasources.mysql.repositories.EntityCreator;
import com.somesoftwareteam.graphql.rest.assemblers.AclModelAssembler;
import com.somesoftwareteam.graphql.rest.assemblers.PropertyModelAssembler;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
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

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * https://spring.io/guides/tutorials/rest/
 */
@RepositoryRestController
@PreAuthorize("hasAuthority('SCOPE_read:properties')")
public class PropertyController {

    private final AclModelAssembler aclModelAssembler;
    private final EntityCreator entityCreator;
    private final GeometryFactory geometryFactory;
    private final GooglePlacesWrapper googlePlacesWrapper;
    private final ModelMapper modelMapper;
    private final MyAclService myAclService;
    private final PropertyModelAssembler assembler;

    public PropertyController(AclModelAssembler aclModelAssembler, EntityCreator entityCreator,
                              GeometryFactory geometryFactory, GooglePlacesWrapper googlePlacesWrapper,
                              ModelMapper modelMapper, MyAclService myAclService, PropertyModelAssembler assembler) {
        this.aclModelAssembler = aclModelAssembler;
        this.entityCreator = entityCreator;
        this.geometryFactory = geometryFactory;
        this.googlePlacesWrapper = googlePlacesWrapper;
        this.modelMapper = modelMapper;
        this.myAclService = myAclService;
        this.assembler = assembler;
    }

    @PostMapping("/properties")
    @PreAuthorize("hasAuthority('SCOPE_write:properties')")
    public ResponseEntity<?> newFixture(@RequestBody Property property)
            throws InterruptedException, ApiException, IOException {
        attemptPropertyGeocode(property);
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

    private void attemptPropertyGeocode(Property property) throws InterruptedException, ApiException, IOException {
        PlacesSearchResult result = googlePlacesWrapper.getGooglePlaceCandidates(property.getAddress());
        property.setAddress(result.formattedAddress);
        property.setAddressFormatted(true);
        property.setLocation(
                geometryFactory.createPoint(
                        new Coordinate(result.geometry.location.lng, result.geometry.location.lat)));
    }

    private EntityModel<Entry> convertToEntityModel(AccessControlEntry entry) {
        Entry dto = modelMapper.map(entry, Entry.class);
        dto.setSid(entry.getSid().toString());
        dto.setPermission(entry.getPermission().getPattern());
        return aclModelAssembler.toModel(dto);
    }
}
