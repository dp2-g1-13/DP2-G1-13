package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class UH1PerformanceTest extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*js.*""", """.*.ico""", """.*.png""", """.*.jpg""", """.*.jpeg"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

	val headers_0 = Map("Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Origin" -> "http://www.dp2.com",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_3 = Map("Accept" -> "image/webp,*/*")

	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
			.pause(9)
	}

	object Register {
		val register = exec(http("Register")
			.get("/users/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(25)
		.feed(csv("data_registerUser.csv"))
		.exec(http("Register")
			.post("/users/new")
			.headers(headers_2)
			.formParam("username", "${username}")
			.formParam("authority", "${authority}")
			.formParam("password", "Is-Dp2-G1-13")
			.formParam("confirmPassword", "Is-Dp2-G1-13")
			.formParam("firstName", "${firstName}")
			.formParam("lastName", "${lastName}")
			.formParam("dni", "${dni}")
			.formParam("email", "${email}")
			.formParam("phoneNumber", "${phoneNumber}")
			.formParam("saveType", "NEW")
			.formParam("_csrf", "${stoken}")
			.resources(http("Register")
				.get("/favicon.ico")
				.headers(headers_3)))
			.pause(15)
	}

	object NotRegisteredErrors {
		val notRegisteredErrors = exec(http("NotRegisteredErrors")
			.get("/users/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(25)
		.exec(http("NotRegisteredErrors")
			.post("/users/new")
			.headers(headers_2)
			.formParam("username", "perftestingwrong")
			.formParam("authority", "HOST")
			.formParam("password", "dsfdfsdfsfsdf")
			.formParam("confirmPassword", "sdfsdfsdfdsfdsfsfsd")
			.formParam("firstName", "Performance")
			.formParam("lastName", "Testing Wrong")
			.formParam("dni", "464841ASDSA5")
			.formParam("email", "perftestingwrong@dp2.com")
			.formParam("phoneNumber", "")
			.formParam("saveType", "NEW")
			.formParam("_csrf", "${stoken}"))
			.pause(36)
	}

	val registeredScn = scenario("Registered").exec(Home.home,
													Register.register)

	val notRegisteredScn = scenario("NotRegistered").exec(Home.home,
													NotRegisteredErrors.notRegisteredErrors)
	setUp(
		registeredScn.inject(atOnceUsers(15)),
		notRegisteredScn.inject(atOnceUsers(15))
	).protocols(httpProtocol)
}