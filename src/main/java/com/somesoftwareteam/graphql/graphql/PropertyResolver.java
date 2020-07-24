package com.somesoftwareteam.graphql.graphql;

import com.fasterxml.jackson.databind.JsonNode;
import com.somesoftwareteam.graphql.datasources.mysql.acl.MyAclService;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Property;
import com.somesoftwareteam.graphql.datasources.mysql.repositories.EntityCreator;
import com.somesoftwareteam.graphql.datasources.mysql.repositories.PropertyRepository;
import com.somesoftwareteam.graphql.datasources.mysql.specification.SpecificationBuilder;
import com.somesoftwareteam.graphql.security.AuthenticationFacade;
import io.leangen.graphql.annotations.GraphQLId;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLNonNull;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.somesoftwareteam.graphql.graphql.ResolverUtils.createPageRequest;
import static com.somesoftwareteam.graphql.graphql.ResolverUtils.createSort;

/**
 * https://github.com/marmelab/react-admin/tree/master/packages/ra-data-graphql-simple
 */
@Service
@GraphQLApi
@PreAuthorize("hasAuthority('SCOPE_read:properties')")
public class PropertyResolver {

    private final AuthenticationFacade authenticationFacade;
    private final EntityCreator entityCreator;
    private final MyAclService myAclService;
    private final SpecificationBuilder<Property> specificationBuilder;
    private final PropertyRepository propertyRepository;

    public PropertyResolver(AuthenticationFacade authenticationFacade, EntityCreator entityCreator,
                            MyAclService myAclService, SpecificationBuilder<Property> specificationBuilder,
                            PropertyRepository repository) {
        this.authenticationFacade = authenticationFacade;
        this.entityCreator = entityCreator;
        this.myAclService = myAclService;
        this.specificationBuilder = specificationBuilder;
        this.propertyRepository = repository;
    }

    @GraphQLQuery(name = "Property", description = "Get property by primary id")
    public Property property(@GraphQLId @GraphQLNonNull Long id) {
        return propertyRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    @GraphQLQuery(name = "allProperties", description = "Get property records")
    public List<Property> allProperties(Integer page, Integer perPage, String sortField, String sortOrder,
                                        PropertyFilter filter) {
        Specification<Property> spec = specificationBuilder.createSpecFromFilter(filter);
        Sort sort = createSort(sortOrder, sortField);
        PageRequest pageRequest = createPageRequest(page, perPage, sort);
        return propertyRepository.findAll(spec, pageRequest).getContent();
    }

    @GraphQLQuery(name = "_allPropertiesMeta", description = "Get property records metadata")
    public ListMetadata allPropertiesMeta(Integer page, Integer perPage, PropertyFilter filter) {
        Specification<Property> spec = specificationBuilder.createSpecFromFilter(filter);
        PageRequest pageRequest = createPageRequest(page, perPage);
        Long count = propertyRepository.findAll(spec, pageRequest).getTotalElements();
        return new ListMetadata(count);
    }

    @PreAuthorize("hasAuthority('SCOPE_write:properties')")
    @GraphQLMutation(name = "createProperty", description = "Create a new property record")
    public Property createProperty(@GraphQLNonNull String name, JsonNode attributes) {
        String owner = authenticationFacade.getCurrentPrincipalName();
        Property property = new Property(name, owner, attributes);
        Property newProperty = entityCreator.setOwnerAndPersistEntity(property);
        myAclService.createAccessControlList(Property.class, newProperty.getId());
        return newProperty;
    }

    @PreAuthorize("hasAuthority('SCOPE_write:properties')")
    @GraphQLMutation(name = "updateProperty", description = "Update a property record")
    public Property updateProperty(@GraphQLId @GraphQLNonNull Long id, String name, JsonNode attributes) {
        Property property = propertyRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        property.setAttributes(attributes);
        property.setName(name);
        propertyRepository.save(property);
        return property;
    }

    @PreAuthorize("hasAuthority('SCOPE_write:properties')")
    @GraphQLMutation(name = "deleteProperty", description = "Delete a property record")
    public Property deleteProperty(@GraphQLId @GraphQLNonNull Long id) {
        Property property = propertyRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        propertyRepository.deleteById(id);
        return property;
    }
}
