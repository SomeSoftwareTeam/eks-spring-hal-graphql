package com.somesoftwareteam.graphql.resolvers;

import com.fasterxml.jackson.databind.JsonNode;
import com.somesoftwareteam.graphql.acl.MyAclService;
import com.somesoftwareteam.graphql.entities.Verification;
import com.somesoftwareteam.graphql.repositories.EntityCreator;
import com.somesoftwareteam.graphql.repositories.VerificationRepository;
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

@Service
@GraphQLApi
@PreAuthorize("hasAuthority('SCOPE_read:verifications')")
public class VerificationResolver {

    private final MyAclService myAclService;
    private final EntityCreator entityCreator;
    private final VerificationRepository repository;

    public VerificationResolver(MyAclService myAclService, EntityCreator entityCreator,
                                VerificationRepository repository) {
        this.myAclService = myAclService;
        this.entityCreator = entityCreator;
        this.repository = repository;
    }

    @GraphQLQuery(name = "Verification", description = "Get verification by primary id")
    public Verification verification(@GraphQLId @GraphQLNonNull Long id) {
        return repository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    @GraphQLQuery(name = "allVerifications", description = "Get verification records")
    public List<Verification> allVerifications(Integer page, Integer perPage, String sortField, String sortOrder,
                                            List<VerificationFilter> filters) {
        // TODO: implement filter
        if (Objects.isNull(page)) page = 0;
        if (Objects.isNull(perPage)) perPage = 10;
        if (Objects.isNull(sortField)) sortField = "id";
        if (Objects.isNull(sortOrder)) sortOrder = "ASC";
        Sort sort = sortOrder.equals("ASC") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        return repository.findAll(PageRequest.of(page, perPage, sort)).getContent();
    }

    @GraphQLQuery(name = "_allVerificationsMeta", description = "Get verification records metadata")
    public ListMetadata allVerificationsMeta(Integer page, Integer perPage, VerificationFilter filter) {
        // TODO: implement filter
        if (Objects.isNull(page)) page = 0;
        if (Objects.isNull(perPage)) perPage = 10;
        Long count = repository.findAll(PageRequest.of(page, perPage)).getTotalElements();
        return new ListMetadata(count);
    }

    @GraphQLMutation(name = "createVerification", description = "Create a new verification record")
    public Verification createVerification(@GraphQLNonNull String name, JsonNode attributes) {
        Verification verification = new Verification(name, attributes);
        Verification newVerification = entityCreator.persistEntity(verification);
        myAclService.createOrUpdateAccessControlListToIncludeCurrentSecurityIdentity(newVerification);
        return newVerification;
    }

    @GraphQLMutation(name = "updateVerification", description = "Update a verification record")
    public Verification updateVerification(@GraphQLId @GraphQLNonNull Long id, String name, JsonNode attributes) {
        Verification verification = repository.findById(id).orElseThrow(ResourceNotFoundException::new);
        verification.setAttributes(attributes);
        verification.setName(name);
        repository.save(verification);
        return verification;
    }

    @GraphQLMutation(name = "deleteVerification", description = "Delete a verification record")
    public Verification deleteVerification(@GraphQLId @GraphQLNonNull Long id) {
        Verification verification = repository.findById(id).orElseThrow(ResourceNotFoundException::new);
        repository.deleteById(id);
        return verification;
    }

}
