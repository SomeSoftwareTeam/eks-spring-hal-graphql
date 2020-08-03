package com.somesoftwareteam.graphql;

import com.somesoftwareteam.graphql.datasources.mysql.entities.Fixture;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Property;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Verification;
import com.somesoftwareteam.graphql.utility.IntegrationTestBase;
import com.vladmihalcea.hibernate.type.json.internal.JacksonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThat;

public class EntityCreatorShould extends IntegrationTestBase {

    @Test
    @WithMockUser("google|12345")
    public void createNewPropertyWithCorrectOwner() {
        Property property = new Property("TestName", null, JacksonUtil.toJsonNode("{}"));
        entityCreator.setOwnerAndPersistEntity(property);
        myAclService.createAccessControlList(Property.class, property.getId());
        assertThat(property.getId()).isNotZero();
        assertThat(property.getOwner()).isEqualTo("google|12345");
    }

    @Test
    @WithMockUser("google|12345")
    public void createNewFixtureWithCorrectOwner() {
        Property property = new Property("TestName", null, JacksonUtil.toJsonNode("{}"));
        entityCreator.setOwnerAndPersistEntity(property);
        Fixture fixture = new Fixture("TestName", null, JacksonUtil.toJsonNode("{}"), property);
        entityCreator.setOwnerAndPersistEntity(fixture);
        myAclService.createAccessControlList(Fixture.class, fixture.getId());
        assertThat(fixture.getId()).isNotZero();
        assertThat(fixture.getOwner()).isEqualTo("google|12345");
    }

    @Test
    @WithMockUser("google|12345")
    public void createNewVerificationWithCorrectOwner() {
        Property property = new Property("TestName", null, JacksonUtil.toJsonNode("{}"));
        entityCreator.setOwnerAndPersistEntity(property);
        Verification verification = new Verification("TestName", null, JacksonUtil.toJsonNode("{}"), property);
        entityCreator.setOwnerAndPersistEntity(verification);
        myAclService.createAccessControlList(Verification.class, verification.getId());
        assertThat(verification.getId()).isNotZero();
        assertThat(verification.getOwner()).isEqualTo("google|12345");
    }
}
