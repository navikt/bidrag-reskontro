package no.nav.bidrag.reskontro.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domain.ident.PersonIdent
import no.nav.bidrag.domain.tid.FomDato
import no.nav.bidrag.domain.tid.TomDato

@Schema(
    name = "Transaksjoner på person",
    description = "Request for å alle transaksjoner/motposter knyttet til etterspurt person."
)
data class TransaksjonerPåPersonRequest(

    @field:Schema(
        description = "Ident til person."
    )
    val person: PersonIdent,

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
