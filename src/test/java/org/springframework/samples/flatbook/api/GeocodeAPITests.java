package org.springframework.samples.flatbook.api;

import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@Log
class GeocodeAPITests {

    private static final String	GEOCODE_ENDPOINT	= "https://maps.googleapis.com/maps/api/geocode/json";

    private static final String	API_KEY				= "AIzaSyBNGjohXXlwq4qcQE66tjVEnfXa5WqM-4c";

    @Test
    void getGeocodeData() {
        given()
            .queryParam("address", "Sevilla, Spain")
            .queryParam("key", API_KEY)
            .request().log().all()
            .response().log().all()
        .when()
            .get(GEOCODE_ENDPOINT)
        .then()
            .statusCode(200)
            .assertThat()
                .body("results.geometry.location.lat", hasItem(37.38909f))
                .body("results.geometry.location.lng", hasItem(-5.984459f))
                .body("status", equalTo("OK"))
            .and()
                .time(lessThan(10L), TimeUnit.SECONDS);

    }

    @Test
    void getGeocodeDataZeroResults() {
        given()
            .queryParam("address", "This test will return zero results")
            .queryParam("key", API_KEY)
            .request().log().all()
            .response().log().all()
            .when()
            .get(GEOCODE_ENDPOINT)
        .then()
            .statusCode(200)
            .assertThat()
                .body("results", empty())
                .body("status", equalTo("ZERO_RESULTS"))
            .and()
                .time(lessThan(10L), TimeUnit.SECONDS);

    }
}
