package no.nav.bidrag.reskontro.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domain.string.Saksnummer

@Schema(
    name = "Innkrevingssak på bidragssak",
    description = "Request for å hente innkrevingssak på saksnummer."
)
data class InnkrevingssakPåSaksnummerRequest(

    @field:Schema(
        description = "Saksnummer, refereres til hos skatt som bidragssaksnummer."
    )
    val saksnummer: Saksnummer
)
