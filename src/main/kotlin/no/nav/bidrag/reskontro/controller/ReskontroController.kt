package no.nav.bidrag.reskontro.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import no.nav.bidrag.domain.ident.PersonIdent
import no.nav.bidrag.reskontro.dto.response.innkrevingssak.EndreRmForSak
import no.nav.bidrag.reskontro.dto.request.InnkrevingssakPåPersonRequest
import no.nav.bidrag.reskontro.dto.request.InnkrevingssakPåSaksnummerRequest
import no.nav.bidrag.reskontro.dto.response.innkrevingssak.Bidragssak
import no.nav.bidrag.reskontro.dto.response.innkrevingssak.BidragssakMedSkyldner
import no.nav.bidrag.reskontro.dto.request.TransaksjonerPåSaksnummerRequest
import no.nav.bidrag.reskontro.dto.request.TransaksjonerPåPersonRequest
import no.nav.bidrag.reskontro.dto.request.TransaksjonerPåTransaksjonsidRequest
import no.nav.bidrag.reskontro.dto.response.innkrevingssaksinformasjon.Innkrevingssaksinformasjon
import no.nav.bidrag.reskontro.dto.response.transaksjoner.Transaksjoner
import no.nav.bidrag.reskontro.service.ReskontroService
import no.nav.security.token.support.core.api.Protected
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Protected
class ReskontroController(val reskontroService: ReskontroService) {

    @PostMapping("/innkrevningssak/bidragssak")
    @Operation(
        description = "Henter saksinformasjon om bidragssaken",
        security = [SecurityRequirement(name = "bearer-key")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Hentet saksinformasjon om bidragssaken"),
            ApiResponse(responseCode = "400", description = "Feil i forespørselen", content = [Content()]),
            ApiResponse(responseCode = "401", description = "Maskinporten-token er ikke gyldig", content = [Content()])
        ]
    )
    fun hentInnkrevingssakPåBidragssak(@RequestBody bidragssak: InnkrevingssakPåSaksnummerRequest): Bidragssak {
        return reskontroService.hentInnkrevingssakPåSak(bidragssak)
    }

    @PostMapping("/innkrevningssak/person")
    @Operation(
        description = "Henter saksinformasjon om bidragssaker på personen",
        security = [SecurityRequirement(name = "bearer-key")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Hentet saksinformasjon om bidragssaker på personen"),
            ApiResponse(responseCode = "204", description = "Fant ingen data", content = [Content()]),
            ApiResponse(responseCode = "400", description = "Feil i forespørselen", content = [Content()]),
            ApiResponse(responseCode = "401", description = "Maskinporten-token er ikke gyldig", content = [Content()])
        ]
    )
    fun hentInnkrevingssakPåBidragssak(@RequestBody personIdent: InnkrevingssakPåPersonRequest): BidragssakMedSkyldner {
        return reskontroService.hentInnkrevingssakPåPerson(personIdent)
    }

    @PostMapping("/transaksjoner/bidragssak")
    @Operation(
        description = "Henter transaksjoner for bidragssaken",
        security = [SecurityRequirement(name = "bearer-key")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Hentet transaksjoner for bidragssaken"),
            ApiResponse(responseCode = "204", description = "Fant ingen data", content = [Content()]),
            ApiResponse(responseCode = "400", description = "Feil i forespørselen", content = [Content()]),
            ApiResponse(responseCode = "401", description = "Maskinporten-token er ikke gyldig", content = [Content()])
        ]
    )
    fun hentTransaksjonerPåBidragssak(@RequestBody transaksjonerPåBidragssak: TransaksjonerPåSaksnummerRequest): Transaksjoner {
        return reskontroService.hentTransaksjonerPåBidragssak(
            transaksjonerPåBidragssak.saksnummer,
            transaksjonerPåBidragssak.fomDato,
            transaksjonerPåBidragssak.tomDato,
            transaksjonerPåBidragssak.antallTransaksjoner
        )
    }

    @PostMapping("/transaksjoner/person")
    @Operation(
        description = "Henter transaksjoner for person",
        security = [SecurityRequirement(name = "bearer-key")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Hentet transaksjoner for person"),
            ApiResponse(responseCode = "204", description = "Fant ingen data", content = [Content()]),
            ApiResponse(responseCode = "400", description = "Feil i forespørselen", content = [Content()]),
            ApiResponse(responseCode = "401", description = "Maskinporten-token er ikke gyldig", content = [Content()])
        ]
    )
    fun hentTransaksjonerPåPerson(@RequestBody transaksjonerPåPerson: TransaksjonerPåPersonRequest): Transaksjoner {
        return reskontroService.hentTransaksjonerPåPerson(
            transaksjonerPåPerson.person,
            transaksjonerPåPerson.fomDato,
            transaksjonerPåPerson.tomDato,
            transaksjonerPåPerson.antallTransaksjoner
        )
    }

    @PostMapping("/transaksjoner/transaksjonsid")
    @Operation(
        description = "Henter transaksjoner på transaksjonsid",
        security = [SecurityRequirement(name = "bearer-key")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Hentet transaksjoner på transaksjonsid"),
            ApiResponse(responseCode = "204", description = "Fant ingen data", content = [Content()]),
            ApiResponse(responseCode = "400", description = "Feil i forespørselen", content = [Content()]),
            ApiResponse(responseCode = "401", description = "Maskinporten-token er ikke gyldig", content = [Content()])
        ]
    )
    fun hentTransaksjonerPåTransaksjonsid(@RequestBody transaksjonerPåTransaksjonsid: TransaksjonerPåTransaksjonsidRequest): Transaksjoner {
        return reskontroService.hentTransaksjonerPåTransaksjonsid(
            transaksjonerPåTransaksjonsid.transaksjonsid,
            transaksjonerPåTransaksjonsid.fomDato,
            transaksjonerPåTransaksjonsid.tomDato
        )
    }

    @PostMapping("/innkrevingsinformasjon")
    @Operation(
        description = "Henter informasjon om innkrevingssaken knyttet til person",
        security = [SecurityRequirement(name = "bearer-key")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Hentet informasjon om innkrevingssaken knyttet til person"),
            ApiResponse(responseCode = "204", description = "Fant ingen data", content = [Content()]),
            ApiResponse(responseCode = "400", description = "Feil i forespørselen", content = [Content()]),
            ApiResponse(responseCode = "401", description = "Maskinporten-token er ikke gyldig", content = [Content()])
        ]
    )
    fun hentInformasjonOmInnkrevingssaken(@RequestBody person: PersonIdent): Innkrevingssaksinformasjon {
        return reskontroService.hentInformasjonOmInnkrevingssaken(person)
    }

    @PatchMapping("/endreRmForSak")
    @Operation(
        description = "Endrer rm for sak",
        security = [SecurityRequirement(name = "bearer-key")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Endret rm for sak"),
            ApiResponse(responseCode = "400", description = "Feil i forespørselen", content = [Content()]),
            ApiResponse(responseCode = "401", description = "Maskinporten-token er ikke gyldig", content = [Content()])
        ]
    )
    fun endreRmForSak(@RequestBody endreRmForSak: EndreRmForSak) {
        reskontroService.endreRmForSak(endreRmForSak.saksnummer, endreRmForSak.barn, endreRmForSak.nyRm)
    }
}
