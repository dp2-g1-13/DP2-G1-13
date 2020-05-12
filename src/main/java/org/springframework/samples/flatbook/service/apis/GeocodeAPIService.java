
package org.springframework.samples.flatbook.service.apis;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.samples.flatbook.model.pojos.GeocodeResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeocodeAPIService {

	private static final String	GEOCODE_ENDPOINT	= "https://maps.googleapis.com/maps/api/geocode/json";

	private static final String	API_KEY				= "AIzaSyBNGjohXXlwq4qcQE66tjVEnfXa5WqM-4c";


	public GeocodeResponse getGeocodeData(final String address) throws UnsupportedEncodingException {

		URI uri = URI.create(GeocodeAPIService.GEOCODE_ENDPOINT + "?address=" + URLEncoder.encode(address, StandardCharsets.UTF_8.toString()) + "&key=" + GeocodeAPIService.API_KEY);
		RestTemplate rest = new RestTemplate();
		return rest.getForObject(uri, GeocodeResponse.class);
	}
}
