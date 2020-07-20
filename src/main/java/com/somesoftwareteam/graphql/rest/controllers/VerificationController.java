package com.somesoftwareteam.graphql.rest.controllers;

import com.somesoftwareteam.graphql.datasources.mysql.acl.MyAclService;
import com.somesoftwareteam.graphql.rest.assemblers.VerificationModelAssembler;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Verification;
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
public class VerificationController {

    private final MyAclService aclManager;
    private final EntityCreator entityCreator;
    private final VerificationModelAssembler verificationModelAssembler;

    VerificationController(MyAclService aclManager, EntityCreator entityCreator,
                           VerificationModelAssembler verificationModelAssembler) {
        this.aclManager = aclManager;
        this.entityCreator = entityCreator;
        this.verificationModelAssembler = verificationModelAssembler;
    }

    @PostMapping("/verifications")
    @PreAuthorize("hasAuthority('SCOPE_write:verifications')")
    public ResponseEntity<?> newVerification(@RequestBody Verification verification) {
        Verification newVerification = entityCreator.persistEntity(verification);
        aclManager.createOrUpdateAccessControlListToIncludeCurrentSecurityIdentity(newVerification);
        EntityModel<Verification> model = verificationModelAssembler.toModel(newVerification);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }
}
