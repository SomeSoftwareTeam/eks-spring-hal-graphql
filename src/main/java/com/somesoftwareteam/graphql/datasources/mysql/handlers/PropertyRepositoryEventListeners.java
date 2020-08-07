package com.somesoftwareteam.graphql.datasources.mysql.handlers;

import com.google.maps.model.PlacesSearchResult;
import com.somesoftwareteam.graphql.datasources.google.GooglePlacesWrapper;
import com.somesoftwareteam.graphql.datasources.mysql.entities.Property;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import javax.validation.Validator;

/**
 * https://docs.spring.io/spring-data/rest/docs/current/reference/html/#events
 */
@Component
@RepositoryEventHandler
public class PropertyRepositoryEventListeners {

    private final GeometryFactory geometryFactory;
    private final GooglePlacesWrapper googlePlacesWrapper;
    private final Logger logger;

    public PropertyRepositoryEventListeners(GeometryFactory geometryFactory, GooglePlacesWrapper googlePlacesWrapper,
                                            Validator validator) {
        this.geometryFactory = geometryFactory;
        this.googlePlacesWrapper = googlePlacesWrapper;
        this.logger = LoggerFactory.getLogger("PropertyRepositoryEventListeners");
    }

    @HandleBeforeCreate
    public void handlePropertySave(Property property) {
        attemptPropertyGeocode(property);
    }

    private void attemptPropertyGeocode(Property property) {
        try {
            PlacesSearchResult result = googlePlacesWrapper.getGooglePlaceCandidates(property.getAddress());
            property.setAddress(result.formattedAddress);
            property.setAddressFormatted(true);
            property.setLocation(geometryFactory.createPoint(
                    new Coordinate(result.geometry.location.lng, result.geometry.location.lat)));
        } catch (Exception e) {
            logger.error("Exception while geocoding", e);
        }
    }
}