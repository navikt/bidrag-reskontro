package no.nav.bidrag.reskontro.controller

import io.kotest.matchers.shouldBe
import no.nav.bidrag.domain.ident.PersonIdent
import no.nav.bidrag.reskontro.SpringTestRunner
import no.nav.bidrag.reskontro.model.HentPersonResponse
import org.junit.jupiter.api.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

class ReskontroControllerTest : SpringTestRunner() {

    @Test
    fun `Skal hente persondata`() {
        val httpEntity = HttpEntity(PersonIdent("22496818540"))
        stubUtils.stubBidragPersonResponse(HentPersonResponse("22496818540", "Navn Navnesen", "213213213"))

        val response = httpHeaderTestRestTemplate.exchange("${rootUri()}/person", HttpMethod.POST, httpEntity, HentPersonResponse::class.java)

        response.statusCode shouldBe HttpStatus.OK
    }
}
