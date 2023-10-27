package no.nav.bidrag.reskontro.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domain.ident.PersonIdent

@Schema(
    name = "Innkrevingssak på person",
    description = "Request for å hente innkrevingssak på personident."
)
data class InnkrevingssakPåPersonRequest(

    @field:Schema(
        description = "Ident til person det hentes innkrevingssak for."
    )
    val personIdent: PersonIdent

)
