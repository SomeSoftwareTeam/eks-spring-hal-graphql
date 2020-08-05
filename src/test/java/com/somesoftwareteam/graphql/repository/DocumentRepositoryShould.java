package com.somesoftwareteam.graphql.repository;

import com.somesoftwareteam.graphql.datasources.mysql.acl.MyAclService;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Document;
import com.somesoftwareteam.graphql.datasources.mysql.repositories.DocumentRepository;
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
public class DocumentRepositoryShould extends IntegrationTestBase {

    @Autowired
    private DocumentRepository repository;

    @BeforeEach
    public void before() {
        myAclService.createNewSecurityIdentityIfNecessary("google|12345");
        myAclService.createNewSecurityIdentityIfNecessary("google|54321");
    }

    @Test
    @WithMockUser(username = "google|12345", authorities = {"SCOPE_read:documents"})
    public void findAllForOwner() {
        Document document = documentBuilder.createNewDocumentWithDefaults().useOwner("google|12345").persist().build();
        accessControlListBuilder.configureAccessControlList("google|12345", Document.class, document.getId());
        Page<Document> resultFromFindAll = repository.findAll(PageRequest.of(0, 10));
        assertThat(resultFromFindAll.getContent().size()).isGreaterThan(0);
    }

    @Test
    @WithMockUser(username = "google|54321", authorities = {"SCOPE_read:documents"})
    public void findNoneForNonOwner() {
        Document document = documentBuilder.createNewDocumentWithDefaults().useOwner("google|12345").persist().build();
        accessControlListBuilder.configureAccessControlList("google|12345", Document.class, document.getId());
        Page<Document> resultFromFindAll = repository.findAll(PageRequest.of(0, 10));
        assertThat(resultFromFindAll.getContent().size()).isEqualTo(0);
    }

    @Test
    @WithMockUser(username = "google|12345", authorities = {"SCOPE_read:documents"})
    public void findByIdForOwner() {
        Document document = documentBuilder.createNewDocumentWithDefaults().useOwner("google|12345").persist().build();
        accessControlListBuilder.configureAccessControlList("google|12345", Document.class, document.getId());
        Document resultFromFindById = repository.findById(document.getId()).orElseThrow(ResourceNotFoundException::new);
        assertThat(resultFromFindById.getId()).isEqualTo(document.getId());
    }

    @Test
    @WithMockUser(username = "google|54321", authorities = {"SCOPE_read:documents"})
    public void notGetDocumentByIdForNonOwner() {
        Document document = documentBuilder.createNewDocumentWithDefaults().useOwner("google|12345").persist().build();
        accessControlListBuilder.configureAccessControlList("google|12345", Document.class, document.getId());
        assertThrows(AccessDeniedException.class, () -> repository.findById(document.getId()));
    }
}
