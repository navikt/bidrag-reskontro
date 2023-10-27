package no.nav.bidrag.reskontro.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domain.string.Saksnummer
import no.nav.bidrag.domain.tid.FomDato
import no.nav.bidrag.domain.tid.TomDato

@Schema(
    name = "Transaksjoner på saksnummer",
    description = "Request for å alle transaksjoner/motposter knyttet til etterspurt saksnummer."
)
data class TransaksjonerPåSaksnummerRequest(

    @field:Schema(
        description = "Saksnummer for bidragssaken."
    )
    val saksnummer: Saksnummer,

    @field:Schema(
        description = "Dato transaksjonene skal være fra."
    )
    val fomDato: FomDato,

    @field:Schema(
        description = "Dato transaksjonene skal være til."
    )
    val tomDato: TomDato,

    @field:Schema(
        description = "Antall transaksjoner som ønskes returnert."
    )
    val antallTransaksjoner: Int?
)
