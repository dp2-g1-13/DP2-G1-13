package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class UH16PerformanceTest extends Simulation {

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
	
	object PrepareAdvertisement {
		val prepareAdvertisement = exec(http("PrepareAdvertisement")
			.get("/performance/advertisements/prepare?hostId=rbordessa0")
			.headers(headers_0)
			.check(jsonPath("$..flatId").ofType[Int].saveAs("flatId"),
			jsonPath("$..advertisementId").ofType[Int].saveAs("advertisementId"),
			jsonPath("$..tenantId").ofType[String].saveAs("tenantId")))
		.pause(17)
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
			.formParam("username", "phucksk")
			.formParam("password", "Is-Dp2-G1-13")
			.formParam("_csrf", "${stoken}")
			.resources(http("Logged")
			.get("/")
			.headers(headers_0))
			.check(status.is(302)))
		.pause(16)
	}
	
	object SearchFlats { 
		val searchFlats = exec(http("SearchFlats")
			.get("/advertisements?city=Sevilla%2C+Spain&postalCode=")
			.headers(headers_0))
		.pause(24)
	}
	
	object Advertisement { 
		val advertisement = exec(http("Advertisement")
			.get("/advertisements/"+"${advertisementId}")
			.headers(headers_0))
		.pause(24)
	}
	
	object NewRequest { 
		val newRequest = exec(http("NewRequest")
			.get("/flats/"+"${flatId}"+"/requests/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(24)
		.feed(csv("data_createRequest.csv"))
		.exec(http("NewRequest")
			.post("/flats/"+"${flatId}"+"/requests/new")
			.headers(headers_3)
			.formParam("description", "${description}")
			.formParam("startDate", "28/05/2020")
			.formParam("finishDate", "${finishDate}")
			.formParam("_csrf", "${stoken}"))
	}

	object NewRequestFail { 
		val newRequestFail = exec(http("NewRequestFail")
			.get("/flats/0/requests/new")
			.headers(headers_0))
		.pause(24)
	}
	
	val newRequest = scenario("NewRequest")
		.exec(
		PrepareAdvertisement.prepareAdvertisement,
		Home.home,
		UserLogin.userLogin,
		SearchFlats.searchFlats,
		Advertisement.advertisement,
		NewRequest.newRequest)
		
	val newRequestFail = scenario("NewRequestFail")
		.exec(
		PrepareAdvertisement.prepareAdvertisement,
		Home.home,
		UserLogin.userLogin,
		SearchFlats.searchFlats,
		Advertisement.advertisement,
		NewRequestFail.newRequestFail)

	setUp(newRequest.inject(atOnceUsers(10)),
		newRequestFail.inject(atOnceUsers(10)))
		.protocols(httpProtocol)
}