package com.somesoftwareteam.graphql;

import com.somesoftwareteam.graphql.entities.Fixture;
import com.vladmihalcea.hibernate.type.json.internal.JacksonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

public class EntityCreatorTests extends IntegrationTestBase {

    @Autowired
    EntityManager entityManger;

    @Test
    @WithMockUser("google|12345")
    public void entityCreator_CreatesNewFixtureWithCorrectOwner() {
        Fixture fixture = new Fixture("TestName", null, JacksonUtil.toJsonNode("{}"));
        entityCreator.persistEntity(fixture);
        myAclService.createOrUpdateAccessControlListToIncludeCurrentSecurityIdentity(fixture);
        assertThat(fixture.getId()).isNotZero();
        assertThat(fixture.getOwner()).isEqualTo("google|12345");
    }
}
