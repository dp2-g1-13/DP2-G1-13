package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class UH19PerformanceTest extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.disableFollowRedirect
		.inferHtmlResources(BlackList(""".*.ico""", """.*.js""", """.*.png""", """.*js.*""", """.*.css"""), WhiteList())
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
	
	object PrepareRequest {
		val prepareRequest = exec(http("PrepareRequest")
			.get("/performance/requests/prepare?hostId=rbordessa0&status=ACCEPTED")
			.headers(headers_0)
			.check(jsonPath("$..flatId").ofType[Int].saveAs("flatId"),
			jsonPath("$..advertisementId").ofType[Int].saveAs("advertisementId"),
			jsonPath("$..tenantId").ofType[String].saveAs("tenantId"),
			jsonPath("$..requestId").ofType[String].saveAs("requestId")))
		.pause(17)
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
  
	object TenantLogin { 
		val tenantLogin = exec(http("TenantLogin")
			.get("/login")
			.headers(headers_0)
			.resources(http("TenantLogin")
			.get("/login")
			.headers(headers_2))
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(10)
		.exec(http("UserLogged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "${tenantId}")
			.formParam("password", "Is-Dp2-G1-13")
			.formParam("_csrf", "${stoken}")
			.resources(http("Logged")
			.get("/")
			.headers(headers_0))
			.check(status.is(302)))
		.pause(16)
	}
	
	object MyFlats { 
		val myFlats = exec(http("MyFlats")
			.get("/flats/list")
			.headers(headers_0))
		.pause(24)
	}
	
	object RequestList { 
		val requestList = exec(http("RequestList")
			.get("/flats/"+"${flatId}"+"/requests/list")
			.headers(headers_0))
		.pause(24)
	}
	
	object FinalizeRequest { 
		val finalizeRequest = exec(http("FinalizeRequest")
			.get("/flats/"+"${flatId}"+"/requests/"+"${requestId}"+"/conclude")
			.headers(headers_0)
			.check(status.is(302)))
		.pause(24)
	}

	object FinalizeRequestFail { 
		val finalizeRequestFail = exec(http("FinalizeRequestFail")
			.get("/flats/0/requests/"+"${requestId}"+"/conclude")
			.headers(headers_0)
			.check(status.is(403)))
		.pause(24)
	}
	
	val finalizeRequest = scenario("FinalizeRequest")
		.exec(
		PrepareRequest.prepareRequest,
		Home.home,
		HostLogin.hostLogin,
		MyFlats.myFlats,
		RequestList.requestList,
		FinalizeRequest.finalizeRequest)
		
	val finalizeRequestFail = scenario("FinalizeRequestFail")
		.exec(
		PrepareRequest.prepareRequest,
		Home.home,
		TenantLogin.tenantLogin,
		FinalizeRequestFail.finalizeRequestFail)

	setUp(finalizeRequest.inject(atOnceUsers(10)),
		finalizeRequestFail.inject(atOnceUsers(10)))
		.protocols(httpProtocol)
}