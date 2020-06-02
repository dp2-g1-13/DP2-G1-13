package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class UH12PerformanceTest extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*js.*""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9,en;q=0.8")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

	val headers_0 = Map(
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(15)
	}
	
	object PrepareFlat {
		val prepareFlat = exec(http("PrepareFlat")
			.get("/performance/flats/prepare?hostId=rbordessa0")
			.headers(headers_0)
			.check(jsonPath("$..flatId").ofType[Int].saveAs("flatId"),
			jsonPath("$..tenantId").ofType[String].saveAs("tenantId")))
		.pause(17)
	}
	
	object Login {
		val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.resources(http("Login")
			.get("/login")
			.headers(headers_2))
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(26)
		.exec(http("LoggedIn")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "${tenantId}")
			.formParam("password", "Is-Dp2-G1-13")
			.formParam("_csrf", "${stoken}"))
		.pause(14)
	}

	object MyUserPage {
		val myUserPage = exec(http("MyUserPage")
			.get("/users/rdunleavy0")
			.headers(headers_0))
		.pause(13)
	}
	

	object MyFlat {
		val myFlat = exec(http("MyFlat")
			.get("/flats/"+"${flatId}")
			.headers(headers_0))
		.pause(17)
	}

	object NewReview {
		val newReview = exec(http("NewReview")
			.get("/reviews/new?flatId="+"${flatId}")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(8)
		.feed(csv("data_createReview.csv"))
		.exec(http("NewReview")
			.post("/reviews/new?flatId="+"${flatId}")
			.headers(headers_3)
			.formParam("description", "${description}")
			.formParam("rate", "${rate}")
			.formParam("creationDate", "23/05/2020")
			.formParam("reviewed", "${flatId}")
			.formParam("creator", "${tenantId}")
			.formParam("type", "FLAT_REVIEW")
			.formParam("_csrf", "${stoken}"))
		.pause(10)
	}

	object NewReviewFail {
		val newReviewFail = exec(http("NewReviewFail")
			.get("/reviews/new?flatId=2")
			.headers(headers_0)
			.resources(http("NewReviewFail")
			.get("/resources/images/error.png")
			.headers(headers_2))
			.check(status.is(200)))
	}

	val positiveScn = scenario("Positive").exec(Home.home,
									  PrepareFlat.prepareFlat,
									  Login.login,
									  MyUserPage.myUserPage,
									  MyFlat.myFlat,
									  NewReview.newReview)
	val cantCreateReviewScn = scenario("Negative").exec(Home.home,
									  PrepareFlat.prepareFlat,
									  Login.login,
									  MyUserPage.myUserPage,
									  MyFlat.myFlat,
									  NewReviewFail.newReviewFail)

	setUp(
		positiveScn.inject(rampUsers(10) during (10 seconds)),
		cantCreateReviewScn.inject(rampUsers(10) during (10 seconds))
	).protocols(httpProtocol)
}