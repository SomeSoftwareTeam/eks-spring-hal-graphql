package com.somesoftwareteam.graphql.resolvers;

import com.somesoftwareteam.graphql.acl.MyAclService;
import com.somesoftwareteam.graphql.repositories.EntityCreator;
import com.somesoftwareteam.graphql.repositories.VerificationRepository;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@GraphQLApi
@PreAuthorize("hasAuthority('SCOPE_read:verifications')")
public class VerificationResolver {

    private final EntityCreator entityCreator;
    private final MyAclService myAclService;
    private final VerificationRepository repository;

    public VerificationResolver(EntityCreator entityCreator,
                                MyAclService myAclService,
                                VerificationRepository repository) {
        this.entityCreator = entityCreator;
        this.myAclService = myAclService;
        this.repository = repository;
    }

//    @GraphQLQuery(name = "getVerification", description = "Query to get verification of asset ownership")
//    public Verification getVerification(Long id) {
//        return repository.findById(id).orElseThrow(ResourceNotFoundException::new);
//    }
//
//    @GraphQLMutation(name = "createVerification", description = "Mutation to create a new verification record")
//    public Verification createVerification(Verification verification) {
//        Verification newVerification = entityCreator.persistEntity(verification);
//        myAclService.createOrUpdateAccessControlListToIncludeCurrentSecurityIdentity(newVerification);
//        return newVerification;
//    }
}
