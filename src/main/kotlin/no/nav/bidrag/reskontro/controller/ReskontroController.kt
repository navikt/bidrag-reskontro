package no.nav.bidrag.reskontro.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import no.nav.bidrag.domain.ident.PersonIdent
import no.nav.bidrag.reskontro.dto.EndreRmForSak
import no.nav.bidrag.reskontro.dto.InnkrevingssakPåPersonRequest
import no.nav.bidrag.reskontro.dto.InnkrevingssakPåSaksnummerRequest
import no.nav.bidrag.reskontro.dto.ReskontroBidragssak
import no.nav.bidrag.reskontro.dto.ReskontroBidragssakMedSkyldner
import no.nav.bidrag.reskontro.dto.TransaksjonerPåBidragssak
import no.nav.bidrag.reskontro.dto.TransaksjonerPåPerson
import no.nav.bidrag.reskontro.dto.TransaksjonerPåTransaksjonsid
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
            ApiResponse(responseCode = "400", description = "Feil i forespørselen"),
            ApiResponse(responseCode = "401", description = "Maskinporten-token er ikke gyldig")
        ]
    )
    fun hentInnkrevingssakPåBidragssak(@RequestBody bidragssak: InnkrevingssakPåSaksnummerRequest): ReskontroBidragssak {
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
            ApiResponse(responseCode = "400", description = "Feil i forespørselen"),
            ApiResponse(responseCode = "401", description = "Maskinporten-token er ikke gyldig")
        ]
    )
    fun hentInnkrevingssakPåBidragssak(@RequestBody personIdent: InnkrevingssakPåPersonRequest): ReskontroBidragssakMedSkyldner {
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
            ApiResponse(responseCode = "400", description = "Feil i forespørselen"),
            ApiResponse(responseCode = "401", description = "Maskinporten-token er ikke gyldig")
        ]
    )
    fun hentTransaksjonerPåBidragssak(@RequestBody transaksjonerPåBidragssak: TransaksjonerPåBidragssak): String {
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
            ApiResponse(responseCode = "400", description = "Feil i forespørselen"),
            ApiResponse(responseCode = "401", description = "Maskinporten-token er ikke gyldig")
        ]
    )
    fun hentTransaksjonerPåPerson(@RequestBody transaksjonerPåPerson: TransaksjonerPåPerson): String {
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
            ApiResponse(responseCode = "400", description = "Feil i forespørselen"),
            ApiResponse(responseCode = "401", description = "Maskinporten-token er ikke gyldig")
        ]
    )
    fun hentTransaksjonerPåTransaksjonsid(@RequestBody transaksjonerPåTransaksjonsid: TransaksjonerPåTransaksjonsid): String {
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
            ApiResponse(responseCode = "400", description = "Feil i forespørselen"),
            ApiResponse(responseCode = "401", description = "Maskinporten-token er ikke gyldig")
        ]
    )
    fun hentInformasjonOmInnkrevingssaken(@RequestBody person: PersonIdent): String {
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
            ApiResponse(responseCode = "400", description = "Feil i forespørselen"),
            ApiResponse(responseCode = "401", description = "Maskinporten-token er ikke gyldig")
        ]
    )
    fun endreRmForSak(@RequestBody endreRmForSak: EndreRmForSak): String {
        return reskontroService.endreRmForSak(endreRmForSak.saksnummer, endreRmForSak.barn, endreRmForSak.nyRm)
    }
}
