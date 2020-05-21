package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class UH2PerformanceTest extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.disableFollowRedirect
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es,en;q=0.9")
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

	val headers_7 = Map(
		"Content-Type" -> "multipart/form-data; boundary=----WebKitFormBoundaryPBBPCnTe5JBFhj1S",
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(4)
	}
	
	object Login {
		val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_2")
			.get("/login")
			.headers(headers_2)))
		.pause(6)
	}
	
	object LoggedHost {
		val loggedHost = exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "rbordessa0")
			.formParam("password", "Is-Dp2-G1-13")
			.formParam("_csrf", "67bd16ca-81f9-4518-bf68-27aa1c16cc0c")
			.resources(http("request_4")
			.get("/")
			.headers(headers_0))
			.check(status.is(302)))
		.pause(9)
	
	}
	
	object MyFlats {
		val myFlats = exec(http("MyFlats")
			.get("/flats/list")
			.headers(headers_0))
		.pause(7)
	}
	
	object NewFlat {
		val newFlat = exec(http("NewFlat")
			.get("/flats/new")
			.headers(headers_0))
		.pause(34)
	}
	
	object CreatedFlat {
		val createdFlat = exec(http("CreatedFlat")
			.post("/flats/new")
			.headers(headers_7)
			.body(RawFileBody("dp2/uh2performancetest/0007_request.dat"))
			.resources(http("request_8")
			.get("/flats/46")
			.headers(headers_0))
			.check(status.is(302)))
		.pause(141)
	}
	
	object LoggedTenant {
		val loggedTenant = exec(http("LoggedTenant")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "rdunleavy0")
			.formParam("password", "Is-Dp2-G1-13")
			.formParam("_csrf", "b0ded5c8-09e8-44ec-87d2-e971b41ab947")
			.resources(http("request_15")
			.get("/")
			.headers(headers_0))
			.check(status.is(302)))
		.pause(43)	
	}
	
	object NewFlatForbidden {
		val newFlatForbidden = exec(http("NewFlatForbidden")
			.get("/flats/new")
			.headers(headers_0)
			.check(status.is(403)))
		.pause(28)
	}
	
	val createFlatByHostScn = scenario("CreateFlatByHost")
		.exec(
		Home.home,
		Login.login,
		LoggedHost.loggedHost,
		MyFlats.myFlats,
		NewFlat.newFlat,
		CreatedFlat.createdFlat)
		
	val createFlatByTenantScn = scenario("CreateFlatByTenant")
		.exec(
		Home.home,
		Login.login,
		LoggedTenant.loggedTenant,
		NewFlatForbidden.newFlatForbidden)

	setUp(createFlatByHostScn.inject(atOnceUsers(1))).protocols(httpProtocol)
	setUp(createFlatByTenantScn.inject(atOnceUsers(1))).protocols(httpProtocol)
}