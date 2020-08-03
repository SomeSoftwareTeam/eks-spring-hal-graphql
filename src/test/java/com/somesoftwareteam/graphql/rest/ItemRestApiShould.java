package com.somesoftwareteam.graphql.rest;

import com.somesoftwareteam.graphql.datasources.mysql.entities.Item;
import com.somesoftwareteam.graphql.utility.HalResourceBase;
import com.vladmihalcea.hibernate.type.json.internal.JacksonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.config.HypermediaWebTestClientConfigurer;

/**
 * https://docs.spring.io/spring/docs/current/spring-framework-reference/pdf/testing-webtestclient.pdf
 * https://docs.spring.io/spring-hateoas/docs/current/reference/html/#client.web-test-client
 */
class ItemRestApiShould extends HalResourceBase<Item> {

    @Test
    public void createReadUpdateDelete(@Autowired HypermediaWebTestClientConfigurer configurer) {
        Item item = new Item("TestEntity", null, JacksonUtil.toJsonNode("{}"));
        createReadUpdateDelete(item, "items");
    }
}
