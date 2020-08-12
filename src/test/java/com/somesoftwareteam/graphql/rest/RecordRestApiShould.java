package com.somesoftwareteam.graphql.rest;

import com.somesoftwareteam.graphql.datasources.mysql.entities.Record;
import org.junit.jupiter.api.Test;

/**
 * https://docs.spring.io/spring/docs/current/spring-framework-reference/pdf/testing-webtestclient.pdf
 * https://docs.spring.io/spring-hateoas/docs/current/reference/html/#client.web-test-client
 */
class RecordRestApiShould extends RestApiResourceBase<Record> {

    @Test
    public void createReadUpdateDelete() {
        Record record = itemBuilder.createNewEntityWithDefaults().build();
        createReadUpdateDelete(record, "records");
    }
}
