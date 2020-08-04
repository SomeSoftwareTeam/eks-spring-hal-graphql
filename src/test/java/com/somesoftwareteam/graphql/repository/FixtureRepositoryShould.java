package com.somesoftwareteam.graphql.repository;

import com.somesoftwareteam.graphql.datasources.mysql.entities.Fixture;
import com.somesoftwareteam.graphql.datasources.mysql.repositories.FixtureRepository;
import com.somesoftwareteam.graphql.utility.IntegrationTestBase;
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
public class FixtureRepositoryShould extends IntegrationTestBase {

    @Autowired
    private FixtureRepository repository;

    @Test
    @WithMockUser(username = "google|12345", authorities = {"SCOPE_read:fixtures"})
    public void findAllForOwner() {
        Fixture fixture = fixtureBuilder.createNewFixtureWithDefaults().useOwner("google|12345").persist().build();
        accessControlListBuilder.configureAccessControlList("google|12345", Fixture.class, fixture.getId());
        Page<Fixture> resultFromFindAll = repository.findAll(PageRequest.of(0, 10));
        assertThat(resultFromFindAll.getContent().size()).isGreaterThan(0);
    }

    @Test
    @WithMockUser(username = "google|54321", authorities = {"SCOPE_read:fixtures"})
    public void findNoneForNonOwner() {
        Fixture fixture = fixtureBuilder.createNewFixtureWithDefaults().useOwner("google|12345").persist().build();
        accessControlListBuilder
                .configureAccessControlList("google|12345", Fixture.class, fixture.getId())
                .addSecurityId("google|54321");
        Page<Fixture> resultFromFindAll = repository.findAll(PageRequest.of(0, 10));
        assertThat(resultFromFindAll.getContent().size()).isEqualTo(0);
    }

    @Test
    @WithMockUser(username = "google|12345", authorities = {"SCOPE_read:fixtures"})
    public void findByIdForOwner() {
        Fixture fixture = fixtureBuilder.createNewFixtureWithDefaults().useOwner("google|12345").persist().build();
        accessControlListBuilder.configureAccessControlList("google|12345", Fixture.class, fixture.getId());
        Fixture resultFromFindById =
                repository.findById(fixture.getId()).orElseThrow(ResourceNotFoundException::new);
        assertThat(resultFromFindById.getId()).isEqualTo(fixture.getId());
    }

    @Test
    @WithMockUser(username = "google|54321", authorities = {"SCOPE_read:fixtures"})
    public void notGetFixtureByIdForNonOwner() {
        Fixture fixture = fixtureBuilder.createNewFixtureWithDefaults().useOwner("google|12345").persist().build();
        accessControlListBuilder
                .configureAccessControlList("google|12345", Fixture.class, fixture.getId())
                .addSecurityId("google|54321");
        assertThrows(AccessDeniedException.class, () -> repository.findById(fixture.getId()));
    }
}
