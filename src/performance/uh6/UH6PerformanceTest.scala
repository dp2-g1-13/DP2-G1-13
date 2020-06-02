package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class UH6PerformanceTest extends Simulation {

  val httpProtocol = http
    .baseUrl("http://www.dp2.com")
    .disableFollowRedirect
    .inferHtmlResources(BlackList(""".*.ico""", """.*.js""", """.*.png""", """.*js.*""", """.*.css"""), WhiteList())
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

  val headers_0 = Map("Upgrade-Insecure-Requests" -> "1")

  val headers_2 = Map(
    "Origin" -> "http://www.dp2.com",
    "Upgrade-Insecure-Requests" -> "1")

  val headers_6 = Map(
    "Content-Type" -> "multipart/form-data; boundary=---------------------------328267529938779412793754211156",
    "Origin" -> "http://www.dp2.com",
    "Upgrade-Insecure-Requests" -> "1")

  val headers_9 = Map("Accept" -> "image/webp,*/*")

  object Home {
    val home = exec(http("Home")
      .get("/")
      .headers(headers_0))
      .pause(9)
  }
  object HostLogin {
    val hostLogin = exec(http("HostLogin")
      .get("/login")
      .headers(headers_0)
      .check(css("input[name=_csrf]", "value").saveAs("stoken")))
      .pause(22)
      .exec(http("HostLogged")
        .post("/login")
        .headers(headers_2)
        .formParam("username", "rbordessa0")
        .formParam("password", "Is-Dp2-G1-13")
        .formParam("_csrf", "${stoken}")
        .resources(http("HostLogged")
          .get("/")
          .headers(headers_0))
        .check(status.is(302)))
      .pause(22)
  }

  object FindMyFlats {
    var findMyFlats = exec(http("FindMyFlats")
      .get("/flats/list")
      .headers(headers_0))
      .pause(16)
  }

  object NewFlat {
    var newFlat = exec(http("NewFlat")
      .get("/flats/new")
      .headers(headers_0)
      .check(css("input[name=_csrf]", "value").saveAs("stoken")))
      .pause(48)
      .feed(csv("data_createFlat.csv"))
      .exec(http("FlatCreated")
        .post("/flats/new")
        .headers(headers_6)
        .formParam("description", "${description}")
        .formParam("squareMeters", "${squareMeters}")
        .formParam("numberRooms", "${numberRooms}")
        .formParam("numberBaths", "${numberBaths}")
        .formParam("availableServices", "${availableServices}")
        .formParam("address.location", "Calle Sierpes")
        .formParam("address.city", "Sevilla")
        .formParam("address.postalCode", "41004")
        .formParam("address.country", "Spain")
        .formUpload("images", "dp2/uh2/images/pivotal-logo.png")
        .formUpload("images", "dp2/uh2/images/pivotal-logo2.png")
        .formUpload("images", "dp2/uh2/images/pivotal-logo3.png")
        .formUpload("images", "dp2/uh2/images/pivotal-logo4.png")
        .formUpload("images", "dp2/uh2/images/pivotal-logo5.png")
        .formUpload("images", "dp2/uh2/images/pivotal-logo6.png")
        .formParam("_csrf", "${stoken}")
        .check(
          status.is(302),
          header("Location").saveAs("createdFlat")
        ))
      .pause(20)
	  .exec(http("NewAdvertisement")
        .get("${createdFlat}")
        .headers(headers_0))
      .pause(20)
	  }
	  
      object NewAdvertisement {
	   val newAdvertisement = exec(http("NewAdvertisement")
         .get("${createdFlat}" + "/advertisements/new")
         .headers(headers_0)
         .check(css("input[name=_csrf]", "value").saveAs("stoken2")))
       .pause(35)
       .feed(csv("data_createAdvertisement.csv"))
       .exec(http("AdvertisementCreated")
        .post("${createdFlat}" + "/advertisements/new")
        .headers(headers_2)
        .formParam("title", "${title}")
        .formParam("description", "${description}")
        .formParam("requirements", "${requirements}")
        .formParam("pricePerMonth", "${pricePerMonth}")
        .formParam("_csrf", "${stoken2}")
        .check(
          status.is(302),
          header("Location").saveAs("createdAdvertisement")
        ))
      .pause(22)
      .exec(http("AdvertisementCreated")
        .get("${createdAdvertisement}")
        .headers(headers_0))
      .pause(22)
  }

	  object DeleteAdvertisement {
		   val deleteAdvertisement = exec(http("DeleteAdvertisement")
			 .get("${createdAdvertisement}" + "/delete")
			 .headers(headers_0)
			 .check(status.is(302)))
		   .pause(22)
	  }

  object TenantLogin {
    val tenantLogin = exec(http("TenantLogin")
      .get("/login")
      .headers(headers_0)
      .check(css("input[name=_csrf]", "value").saveAs("stoken")))
      .pause(22)
      .exec(http("TenantLogged")
        .post("/login")
        .headers(headers_2)
        .formParam("username", "rdunleavy0")
        .formParam("password", "Is-Dp2-G1-13")
        .formParam("_csrf", "${stoken}")
        .resources(http("TenantLogged")
          .get("/")
          .headers(headers_0))
        .check(status.is(302)))
      .pause(18)
  }

  object NewAdvertisementForbidden {
    var newAdvertisementForbidden = exec(http("NewAdvertisementForbidden")
      .get("/flats/46/advertisements/new")
      .headers(headers_0)
      .resources(http("NewAdvertisementForbidden")
        .get("/login")
        .headers(headers_9))
      .check(status.is(403)))
      .pause(17)
  }

  val deleteAdvertisement = scenario("DeleteAdvertisement")
    .exec(
      Home.home,
      HostLogin.hostLogin,
      FindMyFlats.findMyFlats,
      NewFlat.newFlat,
	  NewAdvertisement.newAdvertisement,
	  DeleteAdvertisement.deleteAdvertisement)

  val deleteAdvertisementForbidden = scenario("DeleteAdvertisementForbidden")
    .exec(
      Home.home,
      TenantLogin.tenantLogin,
      NewAdvertisementForbidden.newAdvertisementForbidden)

  setUp(
    deleteAdvertisement.inject(rampUsers(10) during (10 seconds)),
    deleteAdvertisementForbidden.inject(rampUsers(10) during (10 seconds))
  ).protocols(httpProtocol)



}
