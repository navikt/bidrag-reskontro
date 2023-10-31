package no.nav.bidrag.reskontro.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domain.ident.PersonIdent

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
        description = "Antall transaksjoner som ønskes returnert."
    )
    val antallTransaksjoner: Int?
)
