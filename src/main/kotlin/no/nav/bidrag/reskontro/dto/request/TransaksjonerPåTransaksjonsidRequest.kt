package no.nav.bidrag.reskontro.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domain.tid.FomDato
import no.nav.bidrag.domain.tid.TomDato

@Schema(
    name = "Transaksjoner på transaksjonsid",
    description = "Request for å alle transaksjoner/motposter knyttet til etterspurt transaksjonsid."
)
data class TransaksjonerPåTransaksjonsidRequest(

    @field:Schema(
        description = "Id for transaksjonen."
    )
    val transaksjonsid: Long,

    @field:Schema(
        description = "Dato transaksjonene skal være fra."
    )
    val fomDato: FomDato,

    @field:Schema(
        description = "Dato transaksjonene skal være til."
    )
    val tomDato: TomDato
)
