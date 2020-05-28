package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class UH21PerformanceTest extends Simulation {

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

	object HostLogin { 
		val hostLogin = exec(http("HostLogin")
			.get("/login")
			.headers(headers_0)
			.resources(http("HostLogin")
			.get("/login")
			.headers(headers_2))
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(10)
		.exec(http("HostLogged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "rbordessa0")
			.formParam("password", "Is-Dp2-G1-13")
			.formParam("_csrf", "${stoken}")
			.resources(http("HostLogged")
			.get("/")
			.headers(headers_0))
			.check(status.is(302)))
		.pause(16)
	}
	
	object TenantLogin { 
		val tenantLogin = exec(http("TenantLogin")
			.get("/login")
			.headers(headers_0)
			.resources(http("TenantLogin")
			.get("/login")
			.headers(headers_2))
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(10)
		.exec(http("TenantLogged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "rdunleavy0")
			.formParam("password", "Is-Dp2-G1-13")
			.formParam("_csrf", "${stoken}")
			.resources(http("TenantLogged")
			.get("/")
			.headers(headers_0))
			.check(status.is(302)))
		.pause(16)
	}
	
	object UserPage {
		val userPage = exec(http("UserPage")
			.get("/users/rdunleavy0")
			.headers(headers_0))
		.pause(2)
	}
	
	object TaskList {
		val taskList = exec(http("TaskList")
			.get("/tasks/list")
			.headers(headers_0))
		.pause(7)
	}
	
	object TaskListForbidden {
		val taskListForbidden = exec(http("TaskListForbidden")
			.get("/tasks/list")
			.headers(headers_0)
			.check(status.is(403)))
		.pause(7)
	}
	
	val taskList = scenario("TaskList")
		.exec(
		Home.home,
		TenantLogin.tenantLogin,
		UserPage.userPage,
		TaskList.taskList)
		
	val taskListForbidden = scenario("TaskListForbidden")
		.exec(
		Home.home,
		HostLogin.hostLogin,
		TaskListForbidden.taskListForbidden)

	setUp(taskList.inject(atOnceUsers(10)),
		taskListForbidden.inject(atOnceUsers(10)))
		.protocols(httpProtocol)
}