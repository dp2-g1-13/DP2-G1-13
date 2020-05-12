package org.springframework.samples.flatbook.api;

import io.restassured.http.ContentType;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@Log
public class MailjetAPITests {

    private static final String		MAILJET_ENDPOINT					= "https://api.mailjet.com/v3.1/send";

    private static final String		PUBLIC_KEY							= "329ef9afd307ddbed66c48435651c32c";

    private static final String		SECRET_KEY							= "eacb3b578fe8fed88b7bfd1d6ad3234b";

    @Test
    void sendMessage() {
        String body = "{\"Messages\":"
            + "[{\"HTMLPart\":\"<h3>Welcome to FlatBook, Test. </h3><br>Thanks for using our service, enjoy it pisha!"
            + "<br>Your username: a<br>Your password: b<br>Don't share it!\","
            + "\"TextPart\":\"Welcome to FlatBook, Test. Thanks for using our service, enjoy it pisha!"
            + " Your username: a Your password: b Don't share it!\","
            + "\"From\":{\"Email\":\"flatbookus@gmail.com\","
            + "\"Name\":\"FlatBook\"},"
            + "\"To\":[{\"Email\":\"ahyatth_b88m@buxod.com\","
            + "\"Name\":\"a\"}],"
            + "\"Subject\":\"Welcome to FlatBook!\"}]}";

        given()
            .auth()
                .preemptive()
                .basic(PUBLIC_KEY, SECRET_KEY)
            .request().contentType(ContentType.JSON).log().all()
            .response().log().all()
        .with()
            .body(body)
        .when()
            .post(MAILJET_ENDPOINT)
        .then()
            .statusCode(200)
            .assertThat()
                .body("Messages.Status", hasItem("success"))
                .body("Messages.Errors", hasItem(nullValue()))
            .and()
                .time(lessThan(10L), TimeUnit.SECONDS);
    }

    @Test
    void sendMessageBadRequest() {
        String body = "{\"Messages\":"
            + "[{\"HTMLPart\":\"<h3>Welcome to FlatBook, Test. </h3><br>Thanks for using our service, enjoy it pisha!"
            + "<br>Your username: a<br>Your password: b<br>Don't share it!\","
            + "\"TextPart\":\"Welcome to FlatBook, Test. Thanks for using our service, enjoy it pisha!"
            + " Your username: a Your password: b Don't share it!\","
            + "\"From\":{\"Name\":\"FlatBook\"},"
            + "\"To\":[{\"Name\":\"a\"}],"
            + "\"Subject\":\"Welcome to FlatBook!\"}]}";

        given()
            .auth()
                .preemptive()
                .basic(PUBLIC_KEY, SECRET_KEY)
            .request().contentType(ContentType.JSON).log().all()
            .response().log().all()
        .with()
            .body(body)
        .when()
            .post(MAILJET_ENDPOINT)
        .then()
            .statusCode(400)
            .assertThat()
                .body("Messages.Status", hasItem("error"))
                .body("Messages.Errors.StatusCode", hasItem(hasItem(400)))
            .and()
                .time(lessThan(10L), TimeUnit.SECONDS);
    }
}
