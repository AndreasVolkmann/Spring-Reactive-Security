package com.example.demo.config

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SecurityConfigurationTests(
    @Autowired private val webTestClient: WebTestClient
) {
    private val loginUri = "/login"
    private val defaultPassword = "password"
    private val sessionCookieName = "SESSION"

    @Test fun `login should return session`() {
        postLogin().setFormLogin()
            .exchange()
            .expectStatus().isOk
            .expectHeader().exists("Set-Cookie")
    }

    @Test fun `login with bad credentials should return 401`() {
        postLogin().setFormLogin("wrong")
            .exchange()
            .expectStatus().isUnauthorized
    }

    @Test fun `login without credentials should return 401`() {
        postLogin()
            .exchange()
            .expectStatus().isUnauthorized
    }

    @Test fun `logged out access to secure endpoint should return 401`() {
        getUser("invalid")
            .exchange()
            .expectStatus().isUnauthorized
    }

    @Test fun `logged in can access secured endpoint`() {
        val result = postLogin().setFormLogin().exchange().expectBody().returnResult()
        val sessionCookie = result.responseCookies.getFirst(sessionCookieName)!!.value
        getUser(sessionCookie)
            .exchange()
            .expectStatus().isOk
    }

    private fun postLogin() = webTestClient.post().uri(loginUri)

    private fun getUser(sessionCookie: String) =
        webTestClient.get().uri("/user").cookie(sessionCookieName, sessionCookie)

    private fun WebTestClient.RequestBodySpec.setFormLogin(password: String = defaultPassword) = this
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .bodyValue(parseLoginBody(password = password))

    private fun parseLoginBody(userName: String = "user", password: String = defaultPassword) =
        "username=$userName&password=$password"
}
