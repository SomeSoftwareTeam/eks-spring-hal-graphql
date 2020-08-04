package com.somesoftwareteam.graphql.utility;

import com.somesoftwareteam.graphql.datasources.mysql.acl.MyAclService;
import com.somesoftwareteam.graphql.datasources.mysql.entities.*;
import com.somesoftwareteam.graphql.datasources.mysql.repositories.EntityCreator;
import com.somesoftwareteam.graphql.security.AuthenticationFacade;
import com.vladmihalcea.hibernate.type.json.internal.JacksonUtil;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;
import org.testcontainers.containers.MySQLContainer;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * https://vladmihalcea.com/jpa-persist-and-merge/
 * https://www.baeldung.com/spring-boot-testing
 * https://www.testcontainers.org/test_framework_integration/junit_5/
 * https://www.baeldung.com/spring-boot-testcontainers-integration-test
 * https://github.com/eugenp/tutorials/blob/master/spring-security-modules/spring-security-acl/src/test/java/com/baeldung/acl/SpringACLIntegrationTest.java
 */
@SpringBootTest
public class IntegrationTestBase {

    @ClassRule
    public static final MySQLContainer mySQLContainer = MyTestContainer.getInstance();

    @Autowired
    public AuthenticationFacade authenticationFacade;

    @Autowired
    public EntityCreator entityCreator;

    @Autowired
    public EntityManager entityManager;

    // TODO: autowire
    public GeometryFactory geometryFactory = new GeometryFactory();

    @Autowired
    public MyAclService myAclService;

    public Document createTestDocumentWithAccessControlListForUser(String username) {
        Document document = new Document("name", username, "url", "description", JacksonUtil.toJsonNode("{}"));
        entityManager.persist(document);
        configureAccessControlList(username, Document.class, document.getId());
        return document;
    }

    public Property createTestPropertyWithAccessControlListForUser(String username) {
        Point location = geometryFactory.createPoint(new Coordinate(0, 0, 0));
        Property property = new Property("some property", username, JacksonUtil.toJsonNode("{}"));
        entityManager.persist(property);
        configureAccessControlList(username, Property.class, property.getId());
        return property;
    }

    public Item createTestItemWithAccessControlListForUser(String username) {
        Item item = new Item("TestEntity", username, JacksonUtil.toJsonNode(""));
        entityManager.persist(item);
        configureAccessControlList(username, Item.class, item.getId());
        return item;
    }

    public Fixture createTestFixtureWithAccessControlListForUser(String username) {
        Property property = createTestPropertyWithAccessControlListForUser(username);
        Fixture fixture = new Fixture("TestEntity", username, JacksonUtil.toJsonNode("{}"), property);
        entityManager.persist(fixture);
        configureAccessControlList(username, Fixture.class, fixture.getId());
        return fixture;
    }

    public Verification createTestVerificationWithAccessControlListForUser(String username) {
        Property property = createTestPropertyWithAccessControlListForUser(username);
        Verification verification = new Verification("TestEntity", username, JacksonUtil.toJsonNode(""), property);
        entityManager.persist(verification);
        configureAccessControlList(username, Verification.class, verification.getId());
        return verification;
    }

    private void configureAccessControlList(String username, Class<?> entityClass, Long entityId) {

        myAclService.createNewSecurityIdentityIfNecessary(username);

        // Prepare the information we'd like in our access control entry (ACE)
        ObjectIdentity oi = new ObjectIdentityImpl(entityClass, entityId);
        Sid sid = new PrincipalSid(username);

        // Create or update the relevant ACL
        MutableAcl acl;
        try {
            acl = (MutableAcl) myAclService.readAclById(oi);
        } catch (NotFoundException nfe) {
            acl = myAclService.createAcl(oi);
        }

        // Now grant some permissions via an access control entry (ACE)
        acl.insertAce(acl.getEntries().size(), BasePermission.READ, sid, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.WRITE, sid, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.DELETE, sid, true);
        myAclService.updateAcl(acl);
    }

    @Test
    public void context_Loads() {
        String mySqlUrl = mySQLContainer.getJdbcUrl();
        assertThat(mySQLContainer.isRunning());
        assertThat(entityManager).isNotNull();
    }
}
