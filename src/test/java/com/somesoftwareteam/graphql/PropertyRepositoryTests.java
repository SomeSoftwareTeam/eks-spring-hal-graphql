package com.somesoftwareteam.graphql;

import com.somesoftwareteam.graphql.entities.Property;
import com.somesoftwareteam.graphql.repositories.PropertyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Help from:
 * https://www.baeldung.com/spring-boot-testing
 * https://www.baeldung.com/spring-jpa-test-in-memory-database
 * https://vladmihalcea.com/jpa-persist-and-merge/
 * https://github.com/eugenp/tutorials/blob/master/spring-security-modules/spring-security-acl/src/test/java/com/baeldung/acl/SpringACLIntegrationTest.java
 */
public class PropertyRepositoryTests extends IntegrationTestBase {

    @Autowired
    private PropertyRepository repository;

    @BeforeEach
    public void before() {
        myAclService.createNewSecurityIdentityIfNecessary("google|12345");
        myAclService.createNewSecurityIdentityIfNecessary("google|54321");
    }

    @Test
    @Transactional
    @WithMockUser(username = "google|12345", authorities = {"SCOPE_read:properties"})
    public void repository_FindsAllForOwner() {
        createTestPropertyWithAccessControlListForUser("google|12345");
        List<Property> resultFromFindAll = repository.findAll();
        assertThat(resultFromFindAll.size()).isGreaterThan(0);
    }

    @Test
    @Transactional
    @WithMockUser(username = "google|54321", authorities = {"SCOPE_read:properties"})
    public void repository_FindsNoneForNonOwner() {
        createTestPropertyWithAccessControlListForUser("google|12345");
        List<Property> resultFromFindAll = repository.findAll();
        assertThat(resultFromFindAll.size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @WithMockUser(username = "google|12345", authorities = {"SCOPE_read:properties"})
    public void repository_FindsByIdForOwner() {
        Property fixture = createTestPropertyWithAccessControlListForUser("google|12345");
        Property resultFromFindById = repository.findById(fixture.getId()).orElseThrow(ResourceNotFoundException::new);
        assertThat(resultFromFindById.getId()).isEqualTo(fixture.getId());
    }

    @Test
    @Transactional
    @WithMockUser(username = "google|54321", authorities = {"SCOPE_read:properties"})
    public void repository_DoesNotGetByIdForNonOwner() {
        Property property = createTestPropertyWithAccessControlListForUser("google|12345");
        assertThrows(AccessDeniedException.class, () -> repository.findById(property.getId()));
    }
}
