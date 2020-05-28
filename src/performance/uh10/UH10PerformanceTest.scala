package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class UH10PerformanceTest extends Simulation {

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
			.formParam("username", "kwhatling1p")
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
	
	object AdvertisementWithTenants { 
		val advertisementWithTenants = exec(http("AdvertisementWithTenants")
			.get("/advertisements/31")
			.headers(headers_0))
		.pause(24)
	}
	
	object UserPage { 
		val userPage = exec(http("UserPage")
			.get("/users/ejuszczyk1o")
			.headers(headers_0))
		.pause(24)
	}
	
	object NotExistingAdvertisement { 
		val notExistingAdvertisement = exec(http("NotExistingAdvertisement")
			.get("/advertisements/0")
			.headers(headers_0))
		.pause(24)
	}
	
	val advertisementWithTenants = scenario("AdvertisementWithTenants")
		.exec(
		Home.home,
		UserLogin.userLogin,
		SearchFlats.searchFlats,
		AdvertisementWithTenants.advertisementWithTenants,
		UserPage.userPage)
		
	val notExistingAdvertisement = scenario("NotExistingAdvertisement")
		.exec(
		Home.home,
		UserLogin.userLogin,
		SearchFlats.searchFlats,
		NotExistingAdvertisement.notExistingAdvertisement)

	setUp(advertisementWithTenants.inject(atOnceUsers(10)),
		notExistingAdvertisement.inject(atOnceUsers(10)))
		.protocols(httpProtocol)
}