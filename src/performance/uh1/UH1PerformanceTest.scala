package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class UH1PerformanceTest extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png""", """.*.jpg""", """.*.jpeg"""), WhiteList())
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

	object RegisterForm {
		val registerForm = exec(http("RegisterForm")
			.get("/users/new")
			.headers(headers_0))
			.pause(25)
	}

	object RegisteredAndLogged {
		val registeredAndLogged = exec(http("Registered&Logged")
			.post("/users/new")
			.headers(headers_2)
			.formParam("username", "perftestinghost")
			.formParam("authority", "HOST")
			.formParam("password", "Is-Dp2-G1-13")
			.formParam("confirmPassword", "Is-Dp2-G1-13")
			.formParam("firstName", "Performance")
			.formParam("lastName", "Testing")
			.formParam("dni", "33366699W")
			.formParam("email", "performancetesting@dp2.com")
			.formParam("phoneNumber", "333555777")
			.formParam("saveType", "NEW")
			.formParam("_csrf", "a684da09-7d84-4e41-a0b7-b6c42706275a")
			.resources(http("request_3")
				.get("/favicon.ico")
				.headers(headers_3)))
			.pause(15)
	}

	object NotRegisteredErrors {
		val notRegisteredErrors = exec(http("NotRegisteredErrors")
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
			.formParam("_csrf", "6c11f47b-34f0-4b42-9e11-3f836fad8aea"))
			.pause(36)
	}

	val registeredScn = scenario("Registered").exec(Home.home,
		RegisterForm.registerForm,
		RegisteredAndLogged.registeredAndLogged)

	val notRegisteredScn = scenario("NotRegistered").exec(Home.home,
		RegisterForm.registerForm,
		NotRegisteredErrors.notRegisteredErrors)

	setUp(
		registeredScn.inject(atOnceUsers(1)),
		notRegisteredScn.inject(atOnceUsers(1))
	).protocols(httpProtocol)
}