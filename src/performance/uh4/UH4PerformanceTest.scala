package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class UH4PerformanceTest extends Simulation {

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
        .resources(http("request_3")
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

  object NewFlatNewAdvertisement {
    var newFlatNewAdvertisement = exec(http("NewFlatForm")
      .get("/flats/new")
      .headers(headers_0)
      .check(css("input[name=_csrf]", "value").saveAs("stoken")))
      .pause(48)
      .exec(http("FlatCreated")
        .post("/flats/new")
        .headers(headers_6)
        .formParam("description", "This is a sample description with more than 30 characters")
        .formParam("squareMeters", "90")
        .formParam("numberRooms", "2")
        .formParam("numberBaths", "2")
        .formParam("availableServices", "Wifi")
        .formParam("address.address", "Calle Sierpes")
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
          header("Location").saveAs("nextPageLocation")
        ))
      .pause(20)
      .exec(http("FlatCreatedRedirect")
        .get("${nextPageLocation}")
        .headers(headers_0))
      .pause(20)
      .exec(http("NewAdvertisementForm")
        .get("${nextPageLocation}" + "/advertisements/new")
        .headers(headers_0)
        .check(css("input[name=_csrf]", "value").saveAs("stoken2")))
      .pause(35)
      .exec(http("AdvertisementCreated")
        .post("${nextPageLocation}" + "/advertisements/new")
        .headers(headers_2)
        .formParam("title", "New Advertisement")
        .formParam("description", "Advertisement description")
        .formParam("requirements", "There are no requirements for this flat")
        .formParam("pricePerMonth", "250.90")
        .formParam("_csrf", "${stoken2}")
        .check(
          status.is(302),
          header("Location").saveAs("nextPageLocation2")
        ))
      .pause(22)
      .exec(http("request_2")
        .get("${nextPageLocation2}")
        .headers(headers_0))
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
        .resources(http("request_14")
          .get("/")
          .headers(headers_0))
        .check(status.is(302)))
      .pause(18)
  }

  object NewAdvertisementForbidden {
    var newAdvertisementForbidden = exec(http("NewAdvertisementForbidden")
      .get("/flats/46/advertisements/new")
      .headers(headers_0)
      .resources(http("request_11")
        .get("/login")
        .headers(headers_9))
      .check(status.is(403)))
      .pause(17)
  }

  val createAdvertisementByHostScn = scenario("CreateAdvertisementByHost")
    .exec(
      Home.home,
      HostLogin.hostLogin,
      FindMyFlats.findMyFlats,
      NewFlatNewAdvertisement.newFlatNewAdvertisement)

  val createAdvertisementByTenantScn = scenario("CreateAdvertisementByTenant")
    .exec(
      Home.home,
      TenantLogin.tenantLogin,
      NewAdvertisementForbidden.newAdvertisementForbidden)

  setUp(
    createAdvertisementByHostScn.inject(rampUsers(10) during (10 seconds)),
    createAdvertisementByTenantScn.inject(rampUsers(10) during (10 seconds))
  ).protocols(httpProtocol)



}
