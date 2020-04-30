package org.springframework.samples.flatbook.web.apis;

import org.springframework.samples.flatbook.model.pojos.GeocodeResponse;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GeocodeAPI {

    private static final String		GEOCODE_ENDPOINT				= "https://maps.googleapis.com/maps/api/geocode/json";

    private static final String		API_KEY							= "AIzaSyBNGjohXXlwq4qcQE66tjVEnfXa5WqM-4c";

    private GeocodeAPI() {
        throw new UnsupportedOperationException();
    }

    public static GeocodeResponse getGeocodeData(String address) throws UnsupportedEncodingException {


        URI uri = URI.create(GEOCODE_ENDPOINT + "?address=" + URLEncoder.encode(address, StandardCharsets.UTF_8.toString()) + "&key=" + API_KEY);
        RestTemplate rest = new RestTemplate();
        return rest.getForObject(uri, GeocodeResponse.class);
    }
}
