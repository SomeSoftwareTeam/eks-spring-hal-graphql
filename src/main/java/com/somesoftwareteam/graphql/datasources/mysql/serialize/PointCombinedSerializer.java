package com.somesoftwareteam.graphql.datasources.mysql.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

/**
 * https://www.baeldung.com/spring-boot-jsoncomponent
 */
@JsonComponent
public class PointCombinedSerializer {

    private static GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 3857);

    public static class PointJsonSerializer extends JsonSerializer<Point> {

        @Override
        public void serialize(Point point, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("latitude");
            jsonGenerator.writeNumber(point.getCoordinate().getY());
            jsonGenerator.writeFieldName("longitude");
            jsonGenerator.writeNumber(point.getCoordinate().getX());
            jsonGenerator.writeFieldName("srid");
            jsonGenerator.writeNumber(point.getSRID());
            jsonGenerator.writeEndObject();
        }
    }

    public static class PointJsonDeserializer extends JsonDeserializer<Point> {

        @Override
        public Point deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException {
            ObjectNode node = jsonParser.getCodec().readTree(jsonParser);
            Coordinate coordinate = new Coordinate(node.get("longitude").asDouble(), node.get("latitude").asDouble());
            return geometryFactory.createPoint(coordinate);
        }
    }
}