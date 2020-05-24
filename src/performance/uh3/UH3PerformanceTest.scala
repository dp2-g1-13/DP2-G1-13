package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class UH3PerformanceTest extends Simulation {

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
  object HostLoginScn1 {
    val hostLogin = exec(http("HostLoginScn1")
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

  object HostLoginScn2 {
    val hostLogin = exec(http("HostLoginScn2")
      .get("/login")
      .headers(headers_0)
      .check(css("input[name=_csrf]", "value").saveAs("stoken")))
      .pause(22)
      .exec(http("HostLogged")
        .post("/login")
        .headers(headers_2)
        .formParam("username", "rjurries7")
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

  object NewFlatForm {
    var newFlatForm = exec(http("NewFlatForm")
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
  }

  object NewFlatFormBadAddress {
    var newFlatForm = exec(http("NewFlatForm")
      .get("/flats/new")
      .headers(headers_0)
      .check(css("input[name=_csrf]", "value").saveAs("stoken")))
      .pause(48)
      .feed(csv("data_createFlat.csv"))
      .exec(http("FlatBadAddress")
        .post("/flats/new")
        .headers(headers_6)
        .formParam("description", "${description}")
        .formParam("squareMeters", "${squareMeters}")
        .formParam("numberRooms", "${numberRooms}")
        .formParam("numberBaths", "${numberBaths}")
        .formParam("availableServices", "${availableServices}")
        .formParam("address.address", "This address does not exist")
        .formParam("address.city", "This city does not exist")
        .formParam("address.postalCode", "12345")
        .formParam("address.country", "This country does not exist")
        .formUpload("images", "dp2/uh2/images/pivotal-logo.png")
        .formUpload("images", "dp2/uh2/images/pivotal-logo2.png")
        .formUpload("images", "dp2/uh2/images/pivotal-logo3.png")
        .formUpload("images", "dp2/uh2/images/pivotal-logo4.png")
        .formUpload("images", "dp2/uh2/images/pivotal-logo5.png")
        .formUpload("images", "dp2/uh2/images/pivotal-logo6.png")
        .formParam("_csrf", "${stoken}")
        .check(
          status.in(200 to 210)
        ))
      .pause(20)
  }

  val createFlatExistingAddress = scenario("CreateFlatExistingAddress")
    .exec(
      Home.home,
      HostLoginScn1.hostLogin,
      FindMyFlats.findMyFlats,
      NewFlatForm.newFlatForm)

  val createFlatBadAddress = scenario("CreateFlatBadAddress")
    .exec(
      Home.home,
      HostLoginScn2.hostLogin,
      FindMyFlats.findMyFlats,
      NewFlatFormBadAddress.newFlatForm)

  setUp(
    createFlatExistingAddress.inject(rampUsers(10) during (10 seconds)),
    createFlatBadAddress.inject(rampUsers(10) during (10 seconds))
  ).protocols(httpProtocol)
}
