package com.somesoftwareteam.graphql;

import com.somesoftwareteam.graphql.entities.Fixture;
import com.somesoftwareteam.graphql.entities.Property;
import com.somesoftwareteam.graphql.entities.Verification;
import com.vladmihalcea.hibernate.type.json.internal.JacksonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

public class EntityCreatorTests extends IntegrationTestBase {

    @Test
    @WithMockUser("google|12345")
    public void entityCreator_CreatesNewPropertyWithCorrectOwner() {
        Property property = new Property("TestName", null, JacksonUtil.toJsonNode("{}"));
        entityCreator.persistEntity(property);
        myAclService.createOrUpdateAccessControlListToIncludeCurrentSecurityIdentity(property);
        assertThat(property.getId()).isNotZero();
        assertThat(property.getOwner()).isEqualTo("google|12345");
    }

    @Test
    @WithMockUser("google|12345")
    public void entityCreator_CreatesNewFixtureWithCorrectOwner() {
        Fixture fixture = new Fixture("TestName", null, JacksonUtil.toJsonNode("{}"));
        entityCreator.persistEntity(fixture);
        myAclService.createOrUpdateAccessControlListToIncludeCurrentSecurityIdentity(fixture);
        assertThat(fixture.getId()).isNotZero();
        assertThat(fixture.getOwner()).isEqualTo("google|12345");
    }

    @Test
    @WithMockUser("google|12345")
    public void entityCreator_CreatesNewVerificationWithCorrectOwner() {
        Verification verification = new Verification("TestName", null, JacksonUtil.toJsonNode("{}"));
        entityCreator.persistEntity(verification);
        myAclService.createOrUpdateAccessControlListToIncludeCurrentSecurityIdentity(verification);
        assertThat(verification.getId()).isNotZero();
        assertThat(verification.getOwner()).isEqualTo("google|12345");
    }
}
