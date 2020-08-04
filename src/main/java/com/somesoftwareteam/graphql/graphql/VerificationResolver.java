package com.somesoftwareteam.graphql.graphql;

import com.somesoftwareteam.graphql.datasources.mysql.acl.MyAclService;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Verification;
import com.somesoftwareteam.graphql.datasources.mysql.repositories.EntityCreator;
import com.somesoftwareteam.graphql.datasources.mysql.repositories.PropertyRepository;
import com.somesoftwareteam.graphql.datasources.mysql.repositories.VerificationRepository;
import com.somesoftwareteam.graphql.datasources.mysql.specification.SpecificationBuilder;
import com.somesoftwareteam.graphql.security.AuthenticationFacade;
import io.leangen.graphql.annotations.GraphQLId;
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

@Service
@GraphQLApi
@PreAuthorize("hasAuthority('SCOPE_read:verifications')")
public class VerificationResolver {

    private final AuthenticationFacade authenticationFacade;
    private final EntityCreator entityCreator;
    private final MyAclService myAclService;
    private final PropertyRepository propertyRepository;
    private final SpecificationBuilder<Verification> specificationBuilder;
    private final VerificationRepository verificationRepository;

    public VerificationResolver(AuthenticationFacade authenticationFacade, EntityCreator entityCreator,
                                MyAclService myAclService, PropertyRepository propertyRepository,
                                SpecificationBuilder<Verification> specificationBuilder,
                                VerificationRepository verificationRepository) {
        this.authenticationFacade = authenticationFacade;
        this.entityCreator = entityCreator;
        this.myAclService = myAclService;
        this.propertyRepository = propertyRepository;
        this.specificationBuilder = specificationBuilder;
        this.verificationRepository = verificationRepository;
    }

    @GraphQLQuery(name = "Verification", description = "Get verification by primary id")
    public Verification verification(@GraphQLId @GraphQLNonNull Long id) {
        return verificationRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    @GraphQLQuery(name = "allVerifications", description = "Get verification records")
    public List<Verification> allVerifications(Integer page, Integer perPage, String sortField, String sortOrder,
                                               VerificationFilter filter) {
        Specification<Verification> spec = specificationBuilder.createSpecFromFilter(filter);
        Sort sort = createSort(sortOrder, sortField);
        PageRequest pageRequest = createPageRequest(page, perPage, sort);
        return verificationRepository.findAll(spec, pageRequest).getContent();
    }

    @GraphQLQuery(name = "_allVerificationsMeta", description = "Get verification records metadata")
    public ListMetadata allVerificationsMeta(Integer page, Integer perPage, VerificationFilter filter) {
        Specification<Verification> spec = specificationBuilder.createSpecFromFilter(filter);
        PageRequest pageRequest = createPageRequest(page, perPage);
        Long count = verificationRepository.findAll(spec, pageRequest).getTotalElements();
        return new ListMetadata(count);
    }

//    @PreAuthorize("hasAuthority('SCOPE_write:verifications')")
//    @GraphQLMutation(name = "createVerification", description = "Create a new verification record")
//    public Verification createVerification(@GraphQLId @GraphQLNonNull Long propertyId, @GraphQLNonNull String name,
//                                           JsonNode attributes) {
//        String owner = authenticationFacade.getCurrentPrincipalName();
//        Property property = propertyRepository.findById(propertyId).orElseThrow(ResourceNotFoundException::new);
//        Verification verification = new Verification(name, owner, attributes, property);
//        entityCreator.setOwnerAndPersistEntity(verification);
//        myAclService.createAccessControlList(Verification.class, verification.getId());
//        return verification;
//    }
//
//    @PreAuthorize("hasAuthority('SCOPE_write:verifications')")
//    @GraphQLMutation(name = "updateVerification", description = "Update a verification record")
//    public Verification updateVerification(@GraphQLId @GraphQLNonNull Long id, String name, JsonNode attributes) {
//        Verification verification = verificationRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
//        verification.setAttributes(attributes);
//        verification.setName(name);
//        verificationRepository.save(verification);
//        return verification;
//    }
//
//    @PreAuthorize("hasAuthority('SCOPE_write:verifications')")
//    @GraphQLMutation(name = "deleteVerification", description = "Delete a verification record")
//    public Verification deleteVerification(@GraphQLId @GraphQLNonNull Long id) {
//        Verification verification = verificationRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
//        verificationRepository.deleteById(id);
//        return verification;
//    }
}
