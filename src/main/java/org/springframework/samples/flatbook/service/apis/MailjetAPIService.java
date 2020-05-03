package org.springframework.samples.flatbook.service.apis;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MailjetAPIService {

    private static final String		MAILJET_ENDPOINT					= "https://api.mailjet.com/v3.1/send";

    private static final String		PUBLIC_KEY							= "329ef9afd307ddbed66c48435651c32c";

    private static final String		SECRET_KEY							= "eacb3b578fe8fed88b7bfd1d6ad3234b";

    public void sendSimpleMessage(final String name, final String email, final String username, final String password) {
        String body = "{\"Messages\":"
            + "[{\"HTMLPart\":\"<h3>Welcome to FlatBook, " + name + ".</h3><br>Thanks for using our service, enjoy it pisha!"
            + "<br>Your username: "+ username +"<br>Your password: "+ password +"<br>Dont share it!\","
            + "\"TextPart\":\"Welcome to FlatBook, " + name + ". Thanks for using our service, enjoy it pisha!"
            + " Your username: "+ username +" Your password: "+ password +" Dont share it!\","
            + "\"From\":{\"Email\":\"flatbookus@gmail.com\","
            + "\"Name\":\"FlatBook\"},"
            + "\"To\":[{\"Email\":\"" + email + "\","
            + "\"Name\":\"" + name + "\"}],"
            + "\"Subject\":\"Welcome to FlatBook!\"}]}";

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.setBasicAuth(PUBLIC_KEY, SECRET_KEY);

        HttpEntity<String> request = new HttpEntity<>(body, header);
        new RestTemplate().exchange(MAILJET_ENDPOINT, HttpMethod.POST, request, String.class);
    }
}
