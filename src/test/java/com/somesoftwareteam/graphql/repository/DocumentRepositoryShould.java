package com.somesoftwareteam.graphql.repository;

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


}
