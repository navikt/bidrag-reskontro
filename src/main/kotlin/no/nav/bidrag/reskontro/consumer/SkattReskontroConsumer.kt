package no.nav.bidrag.reskontro.consumer

import no.nav.bidrag.commons.web.client.AbstractRestClient
import no.nav.bidrag.domain.ident.PersonIdent
import no.nav.bidrag.domain.tid.FomDato
import no.nav.bidrag.domain.tid.TomDato
import no.nav.bidrag.reskontro.SECURE_LOGGER
import no.nav.bidrag.reskontro.dto.Input
import no.nav.bidrag.reskontro.dto.Output
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestOperations
import java.net.URI

@Service
class SkattReskontroConsumer(
    @Value("\${SKATT_URL}") private val skattUrl: String,
    @Qualifier("maskinporten") private val restTemplate: RestOperations
) : AbstractRestClient(restTemplate, "bidrag-reskontro") {

    companion object {
        const val BIDRAGSSAK_PATH = "/BisysResk/bidragssak"
        const val TRANSAKSJONER_PATH = "/BisysResk/transaksjoner"
        const val INNKREVINGSSAK_PATH = "/BisysResk/innkrevingssak"
        const val ENDRE_RM_PATH = "/BisysResk/endrerm"
    }

    fun hentInnkrevningssakerPåBidragssak(bidragssaksnummer: Long): ResponseEntity<Output> {
        SECURE_LOGGER.info("Kaller hent bidragssak for sak: $bidragssaksnummer")
        return restTemplate.postForEntity(
            URI.create(skattUrl + BIDRAGSSAK_PATH),
            Input(aksjonskode = 1, bidragssaksnummer = bidragssaksnummer),
            Output::class.java
        )
    }

    fun hentInnkrevningssakerPåPerson(person: PersonIdent): ResponseEntity<Output> {
        SECURE_LOGGER.info("Kaller hent bidragssaker for person: ${person.verdi}")
        return restTemplate.postForEntity(
            URI.create(skattUrl + BIDRAGSSAK_PATH),
            Input(aksjonskode = 2, fodselsOrgnr = person.verdi),
            Output::class.java
        )
    }

    fun hentTransaksjonerPåBidragssak(
        bidragssaksnummer: Long,
        fomDato: FomDato,
        tomDato: TomDato,
        antallTransaksjoner: Int?
    ): ResponseEntity<Output> {
        SECURE_LOGGER.info("Kaller hent transaksjoner for sak: $bidragssaksnummer")
        return restTemplate.postForEntity(
            URI.create(skattUrl + TRANSAKSJONER_PATH),
            Input(
                aksjonskode = 3,
                bidragssaksnummer = bidragssaksnummer,
                datoFom = fomDato.toString(),
                datoTom = tomDato.toString(),
                maxAntallTransaksjoner = antallTransaksjoner
            ),
            Output::class.java
        )

    }

    fun hentTransaksjonerPåPerson(
        person: PersonIdent,
        fomDato: FomDato,
        tomDato: TomDato,
        antall: Int?
    ): ResponseEntity<Output> {
        SECURE_LOGGER.info("Kaller hent transaksjoner for person: ${person.verdi}")
        return restTemplate.postForEntity(
            URI.create(skattUrl + TRANSAKSJONER_PATH),
            Input(
                aksjonskode = 4,
                fodselsOrgnr = person.verdi,
                datoFom = fomDato.toString(),
                datoTom = tomDato.toString(),
                maxAntallTransaksjoner = antall
            ),
            Output::class.java
        )
    }

    fun hentTransaksjonerPåTransaksjonsId(
        transaksjonsid: Long,
        fomDato: FomDato,
        tomDato: TomDato
    ): ResponseEntity<Output> {
        SECURE_LOGGER.info("Kaller hent transaksjoner for transaksjonsId: $transaksjonsid")
        return restTemplate.postForEntity(
            URI.create(skattUrl + TRANSAKSJONER_PATH),
            Input(
                aksjonskode = 5,
                transaksjonsId = transaksjonsid,
                datoFom = fomDato.toString(),
                datoTom = tomDato.toString()
            ),
            Output::class.java
        )

    }

    fun hentInformasjonOmInnkrevingssaken(person: PersonIdent): ResponseEntity<Output> {
        SECURE_LOGGER.info("Kaller hent informasjonOmInnkrevingssaken for person: ${person.verdi}")
        return restTemplate.postForEntity(
            URI.create(skattUrl + INNKREVINGSSAK_PATH),
            Input(aksjonskode = 6, fodselsOrgnr = person.verdi),
            Output::class.java
        )
    }

    fun endreRmForSak(
        bidragssaksnummer: Long,
        barn: PersonIdent,
        nyRm: PersonIdent
    ): ResponseEntity<Output> {
        SECURE_LOGGER.info("Kaller endre RM for sak. NyRM: ${nyRm.verdi} i sak $bidragssaksnummer med barn: ${barn.verdi}")
        return restTemplate.exchange(
            URI.create(skattUrl + ENDRE_RM_PATH),
            HttpMethod.PATCH,
            HttpEntity<Input>(
                Input(
                    aksjonskode = 8,
                    bidragssaksnummer = bidragssaksnummer,
                    fodselsnrGjelder = barn.verdi,
                    fodselsnrNy = nyRm.verdi
                ),
            ),
            Output::class.java
        )
    }
}