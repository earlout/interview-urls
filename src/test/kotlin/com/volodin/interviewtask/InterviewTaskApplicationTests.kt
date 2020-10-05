package com.volodin.interviewtask

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import kotlin.concurrent.thread

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InterviewTaskApplicationTests {
	companion object {
		// test may fail if some connection problems occur or server is unavailable
		private val TEST_URLS_EXPECTED_RESULTS = mapOf(
				"https://google.com" to true,
				"http://www.google.cz" to true,
				"http://www.adsdasads.cz" to false,
				"c.com" to false,
				"http://seznam.cz" to true,
				"1234" to false
		)
	}

	@Autowired
	private lateinit var webTestClient: WebTestClient

	@Test
	fun test() {
		val t1 = thread {
			testCheckUrlsEndpoint()
		}
		val t2 = thread {
			testCheckUrlsEndpoint()
		}
		val t3 = thread {
			testCheckUrlsEndpoint()
		}
		t1.join()
		t2.join()
		t3.join()
	}

	private fun testCheckUrlsEndpoint() {
		webTestClient.method(HttpMethod.POST)
				.uri("/api/checkUrls")
				.accept(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(TEST_URLS_EXPECTED_RESULTS.keys))
				.exchange()
				.expectStatus().isOk
				.expectBodyList(Pair::class.java)
				.hasSize(TEST_URLS_EXPECTED_RESULTS.size)
				.returnResult()
				.responseBody!!.forEach {
					assertEquals(TEST_URLS_EXPECTED_RESULTS[it.first], it.second)
				}
	}
}
