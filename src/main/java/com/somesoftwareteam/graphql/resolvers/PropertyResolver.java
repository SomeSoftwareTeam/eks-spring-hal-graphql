package com.somesoftwareteam.graphql.resolvers;

import com.fasterxml.jackson.databind.JsonNode;
import com.somesoftwareteam.graphql.acl.MyAclService;
import com.somesoftwareteam.graphql.entities.Property;
import com.somesoftwareteam.graphql.repositories.EntityCreator;
import com.somesoftwareteam.graphql.repositories.PropertyRepository;
import com.somesoftwareteam.graphql.security.AuthenticationFacade;
import io.leangen.graphql.annotations.GraphQLId;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLNonNull;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * https://github.com/marmelab/react-admin/tree/master/packages/ra-data-graphql-simple
 */
@Service
@GraphQLApi
@PreAuthorize("hasAuthority('SCOPE_read:properties')")
public class PropertyResolver {

    private final MyAclService myAclService;
    private final AuthenticationFacade authenticationFacade;
    private final EntityCreator entityCreator;
    private final PropertyRepository repository;

    public PropertyResolver(AuthenticationFacade authenticationFacade, EntityCreator entityCreator,
                            MyAclService myAclService, PropertyRepository repository) {
        this.authenticationFacade = authenticationFacade;
        this.entityCreator = entityCreator;
        this.myAclService = myAclService;
        this.repository = repository;
    }

    @GraphQLQuery(name = "Property", description = "Get property by primary id")
    public Property property(@GraphQLId @GraphQLNonNull Long id) {
        return repository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    @GraphQLQuery(name = "allProperties", description = "Get property records")
    public List<Property> allProperties(Integer page, Integer perPage, String sortField, String sortOrder,
                                        List<PropertyFilter> filters) {
        // TODO: implement filter
        if (Objects.isNull(page)) page = 0;
        if (Objects.isNull(perPage)) perPage = 10;
        if (Objects.isNull(sortField)) sortField = "id";
        if (Objects.isNull(sortOrder)) sortOrder = "ASC";
        Sort sort = sortOrder.equals("ASC") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        return repository.findAll(PageRequest.of(page, perPage, sort)).getContent();
    }

    @GraphQLQuery(name = "_allPropertiesMeta", description = "Get property records metadata")
    public ListMetadata allPropertiesMeta(Integer page, Integer perPage, PropertyFilter filter) {
        // TODO: implement filter
        if (Objects.isNull(page)) page = 0;
        if (Objects.isNull(perPage)) perPage = 10;
        Long count = repository.findAll(PageRequest.of(page, perPage)).getTotalElements();
        return new ListMetadata(count);
    }

    @GraphQLMutation(name = "createProperty", description = "Create a new property record")
    public Property createProperty(@GraphQLNonNull String name, JsonNode attributes) {
        String owner = authenticationFacade.getCurrentPrincipalName();
        Property property = new Property(name, owner, attributes);
        Property newProperty = entityCreator.persistEntity(property);
        myAclService.createOrUpdateAccessControlListToIncludeCurrentSecurityIdentity(newProperty);
        return newProperty;
    }

    @GraphQLMutation(name = "updateProperty", description = "Update a property record")
    public Property updateProperty(@GraphQLId @GraphQLNonNull Long id, String name, JsonNode attributes) {
        Property property = repository.findById(id).orElseThrow(ResourceNotFoundException::new);
        property.setAttributes(attributes);
        property.setName(name);
        repository.save(property);
        return property;
    }

    @GraphQLMutation(name = "deleteProperty", description = "Delete a property record")
    public Property deleteProperty(@GraphQLId @GraphQLNonNull Long id) {
        Property property = repository.findById(id).orElseThrow(ResourceNotFoundException::new);
        repository.deleteById(id);
        return property;
    }
}
