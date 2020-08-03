package com.somesoftwareteam.graphql;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * https://www.baeldung.com/spring-boot-jsoncomponent
 */
@JsonTest
public class PointCombinedJsonSerializerShould {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void deserializePointCorrectly() throws IOException {
        String json = "{\"latitude\":\"75.75\", \"longitude\":\"123.123\"}";
        Point point = objectMapper.readValue(json, Point.class);
        assertThat(point.getCoordinate().getX()).isEqualTo(123.123);
        assertThat(point.getCoordinate().getY()).isEqualTo(75.75);
    }

    @Test
    public void serializePointCorrectly() throws JsonProcessingException {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 3857);
        Point point = geometryFactory.createPoint(new Coordinate(123.123, 75.75));
        String s = objectMapper.writeValueAsString(point);
        assertThat(s).isEqualTo("{\"latitude\":75.75,\"longitude\":123.123,\"srid\":3857}");
    }
}