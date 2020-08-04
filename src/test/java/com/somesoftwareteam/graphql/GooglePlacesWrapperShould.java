package com.somesoftwareteam.graphql;

import com.google.maps.errors.ApiException;
import com.google.maps.model.PlacesSearchResult;
import com.somesoftwareteam.graphql.datasources.google.GooglePlacesWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class GooglePlacesWrapperShould {

    @Autowired
    GooglePlacesWrapper googlePlacesWrapper;

    @Test
    public void getFormattedAddress() throws InterruptedException, ApiException, IOException {
        String input = "135 trenor ln powells point nc";
        PlacesSearchResult result = googlePlacesWrapper.getGooglePlaceCandidates(input);
        assertThat(result.formattedAddress).isEqualTo("135 Trenor Ln, Powells Point, NC 27966, USA");
    }
}
