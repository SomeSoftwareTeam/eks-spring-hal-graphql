package com.somesoftwareteam.graphql;

import com.somesoftwareteam.graphql.entities.Fixture;
import com.somesoftwareteam.graphql.repositories.FixtureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Help from:
 * https://www.baeldung.com/spring-boot-testing
 * https://www.baeldung.com/spring-jpa-test-in-memory-database
 * https://vladmihalcea.com/jpa-persist-and-merge/
 * https://github.com/eugenp/tutorials/blob/master/spring-security-modules/spring-security-acl/src/test/java/com/baeldung/acl/SpringACLIntegrationTest.java
 */
public class FixtureRepositoryTests extends com.somesoftwareteam.graphql.IntegrationTestBase {

    @Autowired
    private FixtureRepository repository;

    @BeforeEach
    public void before() {
        myAclService.createNewSecurityIdentityIfNecessary("google|12345");
        myAclService.createNewSecurityIdentityIfNecessary("google|54321");
    }

    @Test
    @Transactional
    @WithMockUser(username = "google|12345", authorities = {"SCOPE_read:fixtures"})
    public void repository_FindsAllForOwner() {
        createTestFixtureWithAccessControlListForUser("google|12345");
        Page<Fixture> resultFromFindAll = repository.findAll(PageRequest.of(0, 10));
        assertThat(resultFromFindAll.getContent().size()).isGreaterThan(0);
    }

    @Test
    @Transactional
    @WithMockUser(username = "google|54321", authorities = {"SCOPE_read:fixtures"})
    public void repository_FindsNoneForNonOwner() {
        createTestFixtureWithAccessControlListForUser("google|12345");
        Page<Fixture> resultFromFindAll = repository.findAll(PageRequest.of(0, 10));
        assertThat(resultFromFindAll.getContent().size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @WithMockUser(username = "google|12345", authorities = {"SCOPE_read:fixtures"})
    public void repository_FindsByIdForOwner() {
        Fixture fixture = createTestFixtureWithAccessControlListForUser("google|12345");
        Fixture resultFromFindById =
                repository.findById(fixture.getId()).orElseThrow(ResourceNotFoundException::new);
        assertThat(resultFromFindById.getId()).isEqualTo(fixture.getId());
    }

    @Test
    @Transactional
    @WithMockUser(username = "google|54321", authorities = {"SCOPE_read:fixtures"})
    public void repository_DoesNotGetFixtureByIdForNonOwner() {
        Fixture fixture = createTestFixtureWithAccessControlListForUser("google|12345");
        assertThrows(AccessDeniedException.class, () -> repository.findById(fixture.getId()));
    }
}
