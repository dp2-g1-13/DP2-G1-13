package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class UH22PerformanceTest extends Simulation {

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

	object Home { 
		val home = exec(http("Home")
				.get("/")
				.headers(headers_0))
		.pause(4)
	}

object AdminLogin { 
		val adminLogin = exec(http("AdminLogin")
			.get("/login")
			.headers(headers_0)
			.resources(http("AdminLogin")
			.get("/login")
			.headers(headers_2))
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(10)
		.exec(http("AdminLogged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "admin")
			.formParam("password", "admin")
			.formParam("_csrf", "${stoken}")
			.resources(http("Logged")
			.get("/")
			.headers(headers_0))
			.check(status.is(302)))
		.pause(16)
	}
	
	object UserLogin { 
		val userLogin = exec(http("UserLogin")
			.get("/login")
			.headers(headers_0)
			.resources(http("UserLogin")
			.get("/login")
			.headers(headers_2))
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(10)
		.exec(http("UserLogged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "rbordessa0")
			.formParam("password", "Is-Dp2-G1-13")
			.formParam("_csrf", "${stoken}")
			.resources(http("Logged")
			.get("/")
			.headers(headers_0))
			.check(status.is(302)))
		.pause(16)
	}
	
	object UserPage {
		val userPage = exec(http("UserPage")
			.get("/users/vcloyv")
			.headers(headers_0))
		.pause(2)
	}
	
	object NewReport {
		val newReport = exec(http("NewReport")
			.get("/reports/vcloyv/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(7)
		.exec(http("NewReport")
			.post("/reports/vcloyv/new")
			.headers(headers_3)
			.formParam("reason", "ReportReport")
			.formParam("creationDate", "23/05/2020")
			.formParam("sender", "rbordessa0")
			.formParam("receiver", "vcloyv")
			.formParam("_csrf", "${stoken}")
			.resources(http("UserPage")
			.get("/users/vcloyv")
			.headers(headers_0))
			.check(status.is(302)))
		.pause(22)
	}
	
	object NewReportForbidden {
		val newReportForbidden = exec(http("NewReportForbidden")
			.get("/reports/vcloyv/new")
			.headers(headers_0)
			.check(status.is(403)))
		.pause(7)
	}
	
	val newReport = scenario("NewReport")
		.exec(
		Home.home,
		UserLogin.userLogin,
		UserPage.userPage,
		NewReport.newReport)
		
	val newReportForbidden = scenario("NewReportForbidden")
		.exec(
		Home.home,
		AdminLogin.adminLogin,
		UserPage.userPage,
		NewReportForbidden.newReportForbidden)

	setUp(newReport.inject(atOnceUsers(10)),
		newReportForbidden.inject(atOnceUsers(10)))
		.protocols(httpProtocol)
}