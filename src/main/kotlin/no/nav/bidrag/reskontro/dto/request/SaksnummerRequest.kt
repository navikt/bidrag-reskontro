package no.nav.bidrag.reskontro.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domain.string.Saksnummer

data class SaksnummerRequest(

    @field:Schema(
        description = "Saksnummer, refereres til hos skatt som bidragssaksnummer."
    )
    val saksnummer: Saksnummer
)
