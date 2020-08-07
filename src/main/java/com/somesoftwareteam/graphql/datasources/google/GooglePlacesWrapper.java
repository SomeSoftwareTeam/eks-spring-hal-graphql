package com.somesoftwareteam.graphql.datasources.google;

import com.google.maps.FindPlaceFromTextRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.FindPlaceFromText;
import com.google.maps.model.PlacesSearchResult;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * https://developers.google.com/places/web-service/search?hl=en_US
 * https://github.com/googlemaps/google-maps-services-java
 */
@Service
public class GooglePlacesWrapper {

    private final GeoApiContext geoApiContext;

    public GooglePlacesWrapper(GeoApiContext geoApiContext) {
        this.geoApiContext = geoApiContext;
    }

    public PlacesSearchResult getGooglePlaceCandidates(String input)
            throws IllegalArgumentException, IOException, InterruptedException, ApiException {

        FindPlaceFromText findPlaceFromText = PlacesApi
                .findPlaceFromText(geoApiContext, input, FindPlaceFromTextRequest.InputType.TEXT_QUERY)
                .fields(
                        FindPlaceFromTextRequest.FieldMask.FORMATTED_ADDRESS,
                        FindPlaceFromTextRequest.FieldMask.GEOMETRY)
                .await();

        if (findPlaceFromText.candidates.length == 0)
            throw new IllegalArgumentException("No candidates for place were found for: " + input);

        return findPlaceFromText.candidates[0];
    }
}
