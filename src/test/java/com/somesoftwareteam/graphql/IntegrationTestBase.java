package com.somesoftwareteam.graphql;

import com.somesoftwareteam.graphql.acl.MyAclService;
import com.somesoftwareteam.graphql.entities.Fixture;
import com.somesoftwareteam.graphql.entities.Property;
import com.somesoftwareteam.graphql.entities.Verification;
import com.somesoftwareteam.graphql.repositories.EntityCreator;
import com.somesoftwareteam.graphql.security.AuthenticationFacade;
import com.vladmihalcea.hibernate.type.json.internal.JacksonUtil;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;
import org.springframework.test.context.junit4.SpringRunner;
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
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTestBase {

    @ClassRule
    public static final MySQLContainer mySQLContainer = MyTestContainer.getInstance();

    @Autowired
    AuthenticationFacade authenticationFacade;

    @Autowired
    EntityCreator entityCreator;

    @Autowired
    EntityManager entityManager;

    // TODO: autowire
    GeometryFactory geometryFactory = new GeometryFactory();

    @Autowired
    MyAclService myAclService;

    @LocalServerPort
    public int port;

    public Property createTestPropertyWithAccessControlListForUser(String username) {

        Point location = geometryFactory.createPoint(new Coordinate(0, 0, 0));
        Property property = new Property("some property", username, JacksonUtil.toJsonNode("{}"));
        entityManager.persist(property);

        // Prepare the information we'd like in our access control entry (ACE)
        ObjectIdentity oi = new ObjectIdentityImpl(Property.class, property.getId());
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

        return property;
    }

    public Fixture createTestFixtureWithAccessControlListForUser(String username) {

        Fixture fixture = new Fixture("TestEntity", username, JacksonUtil.toJsonNode("{}"));
        entityManager.persist(fixture);

        // Prepare the information we'd like in our access control entry (ACE)
        ObjectIdentity oi = new ObjectIdentityImpl(Fixture.class, fixture.getId());
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

        return fixture;
    }

    public Verification createTestVerificationWithAccessControlListForUser(String username) {

        Verification verification = new Verification("TestEntity", JacksonUtil.toJsonNode(""));
        entityManager.persist(verification);

        // Prepare the information we'd like in our access control entry (ACE)
        ObjectIdentity oi = new ObjectIdentityImpl(Verification.class, verification.getId());
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

        return verification;
    }

    @Test
    public void context_Loads() {
        String mySqlUrl = mySQLContainer.getJdbcUrl();
        assertThat(mySQLContainer.isRunning());
        assertThat(entityManager).isNotNull();
    }
}
