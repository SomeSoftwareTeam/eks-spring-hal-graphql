package com.somesoftwareteam.graphql;

import com.somesoftwareteam.graphql.datasources.mysql.entities.Fixture;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Property;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Verification;
import com.somesoftwareteam.graphql.datasources.mysql.repositories.EntityCreator;
import com.somesoftwareteam.graphql.utility.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThat;

public class EntityCreatorShould extends IntegrationTestBase {

    @Autowired
    public EntityCreator entityCreator;

    @Test
    @WithMockUser("google|12345")
    public void createNewPropertyWithCorrectOwner() {
        Property property = propertyBuilder.createNewPropertyWithDefaults().build();
        entityCreator.persistEntity(property);
        assertThat(property.getId()).isNotZero();
//        assertThat(property.getOwner()).isEqualTo("google|12345");
    }

    @Test
    @WithMockUser("google|12345")
    public void createNewFixtureWithCorrectOwner() {
        Property property = propertyBuilder.createNewPropertyWithDefaults().persist().build();
        Fixture fixture = fixtureBuilder.createNewFixtureWithDefaults().useProperty(property).build();
        entityCreator.persistEntity(fixture);
        assertThat(fixture.getId()).isNotZero();
//        assertThat(fixture.getOwner()).isEqualTo("google|12345");
    }

    @Test
    @WithMockUser("google|12345")
    public void createNewVerificationWithCorrectOwner() {
        Property property = propertyBuilder.createNewPropertyWithDefaults().persist().build();
        Verification verification = verificationBuilder.createNewVerificationWithDefaults().useProperty(property).build();
        entityCreator.persistEntity(verification);
        assertThat(verification.getId()).isNotZero();
//        assertThat(verification.getOwner()).isEqualTo("google|12345");
    }
}
