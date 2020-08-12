package com.somesoftwareteam.graphql.repository;

import com.somesoftwareteam.graphql.datasources.mysql.entities.Property;
import com.somesoftwareteam.graphql.datasources.mysql.repositories.PropertyRepository;
import com.somesoftwareteam.graphql.security.AuthenticationFacade;
import com.somesoftwareteam.graphql.utility.IntegrationTestBase;
import com.somesoftwareteam.graphql.utility.WithMockAuthentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * https://www.baeldung.com/spring-boot-testing
 * https://vladmihalcea.com/jpa-persist-and-merge/
 * https://github.com/eugenp/tutorials/blob/master/spring-security-modules/spring-security-acl/src/test/java/com/baeldung/acl/SpringACLIntegrationTest.java
 * https://docs.spring.io/spring-security/site/docs/4.0.x/reference/htmlsingle/#test-method-withsecuritycontext
 */
public class PropertyRepositoryShould extends IntegrationTestBase {

    @Autowired
    private PropertyRepository repository;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @BeforeEach
    public void before() {
        myAclService.createNewSecurityIdentityIfNecessary("google|12345");
        myAclService.createNewSecurityIdentityIfNecessary("google|54321");
    }

    @Test
    @WithMockAuthentication(groups = {"Trenor Laners"}, authorities = {"SCOPE_read:properties"})
    public void findAllForGroupMember() {
        Property property = propertyBuilder.createNewEntityWithDefaults().persist().build();
        Page<Property> resultFromFindAll = repository.findAll(PageRequest.of(0, 10));
        assertThat(resultFromFindAll.getContent().size()).isGreaterThan(0);
    }

//    @Test
//    @WithMockAuthentication(groups = {}, authorities = {"SCOPE_read:properties"})
//    public void notFindAllForNonGroupMember() {
//        Property property = propertyBuilder.createNewPropertyWithDefaults().persist().build();
//        Page<Property> resultFromFindAll = repository.findAll(PageRequest.of(0, 10));
//        assertThat(resultFromFindAll.getContent().size()).isEqualTo(0);
//    }

    @Test
    @WithMockAuthentication(groups = {"Trenor Laners"}, authorities = {"SCOPE_read:properties"}, name = "google|12345")
    public void findByIdForGroupMember() {
        Property property = propertyBuilder.createNewEntityWithDefaults().persist().build();
        Property resultFromFindById = repository.findById(property.getId()).orElseThrow(ResourceNotFoundException::new);
        assertThat(resultFromFindById.getId()).isEqualTo(property.getId());
    }

//    @Test
//    @WithMockAuthentication(groups = {}, authorities = {"SCOPE_read:properties"})
//    public void notFindByIdForNonGroupMember() {
//        Property property = propertyBuilder.createNewPropertyWithDefaults().persist().build();
//        Optional<Property> resultFromFindById = repository.findById(property.getId());
//        assertThat(resultFromFindById.isEmpty()).isTrue();
//    }
}
