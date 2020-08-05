package com.somesoftwareteam.graphql.repository;

import com.somesoftwareteam.graphql.datasources.mysql.entities.Verification;
import com.somesoftwareteam.graphql.datasources.mysql.repositories.VerificationRepository;
import com.somesoftwareteam.graphql.utility.IntegrationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Help from:
 * https://www.baeldung.com/spring-boot-testing
 * https://www.baeldung.com/spring-jpa-test-in-memory-database
 * https://vladmihalcea.com/jpa-persist-and-merge/
 * https://github.com/eugenp/tutorials/blob/master/spring-security-modules/spring-security-acl/src/test/java/com/baeldung/acl/SpringACLIntegrationTest.java
 */
public class VerificationRepositoryShould extends IntegrationTestBase {

    @Autowired
    VerificationRepository repository;

    @BeforeEach
    public void before() {
        myAclService.createNewSecurityIdentityIfNecessary("google|12345");
        myAclService.createNewSecurityIdentityIfNecessary("google|54321");
    }

    @Test
    @WithMockUser(username = "google|12345", authorities = {"SCOPE_read:verifications"})
    public void findAllForOwner() {
        Verification verification = verificationBuilder
                .createNewVerificationWithDefaults().useOwner("google|12345").persist().build();
        accessControlListBuilder
                .configureAccessControlList("google|12345", Verification.class, verification.getId());
        Page<Verification> resultFromFindAll = repository.findAll(PageRequest.of(0, 10));
        assertThat(resultFromFindAll.getContent().size()).isGreaterThan(0);
    }

    @Test
    @WithMockUser(username = "google|54321", authorities = {"SCOPE_read:verifications"})
    public void findNoneForNonOwner() {
        Verification verification = verificationBuilder
                .createNewVerificationWithDefaults().useOwner("google|12345").persist().build();
        accessControlListBuilder
                .configureAccessControlList("google|12345", Verification.class, verification.getId());
        Page<Verification> resultFromFindAll = repository.findAll(PageRequest.of(0, 10));
        assertThat(resultFromFindAll.getContent().size()).isEqualTo(0);
    }

    @Test
    @WithMockUser(username = "google|12345", authorities = {"SCOPE_read:verifications"})
    public void getVerificationByIdForOwner() {
        Verification verification = verificationBuilder
                .createNewVerificationWithDefaults().useOwner("google|12345").persist().build();
        accessControlListBuilder
                .configureAccessControlList("google|12345", Verification.class, verification.getId());
        Verification resultFromGetById = repository
                .findById(verification.getId()).orElseThrow(ResourceNotFoundException::new);
        assertThat(resultFromGetById.getId()).isEqualTo(verification.getId());
    }

    @Test
    @WithMockUser(username = "google|54321", authorities = {"SCOPE_read:verifications"})
    public void notGetFixtureByIdForNonOwner() {
        Verification verification = verificationBuilder
                .createNewVerificationWithDefaults().useOwner("google|12345").persist().build();
        accessControlListBuilder
                .configureAccessControlList("google|12345", Verification.class, verification.getId());
        assertThrows(AccessDeniedException.class, () -> repository.findById(verification.getId()));
    }
}
