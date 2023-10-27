package no.nav.bidrag.reskontro.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domain.ident.PersonIdent
import java.math.BigInteger

@Schema(
    name = "Skyldner",
    description = "Informasjon om skyldner."
)
data class Skyldner(

    @field:Schema(
        description = "Identen til skyldner"
    )
    val personIdent: PersonIdent,

    @field:Schema(
        description = "Summen av det løpende bidraget på skyldner. " +
            "Beregnes ikke for innkrevningssak på person, kun for informasjon om innkrevingssaken."
    )
    val sumLøpendeBidrag: BigInteger? = null,

    @field:Schema(
        description = "Sum av beløpsfelt fra innbetalinger i historikk avhengig av fordelingsstatus."
    )
    val innbetaltBeløpUfordelt: BigInteger,

    @field:Schema(
        description = "Sum av saldo gebyrer på sak."
    )
    val gjeldIlagtGebyr: BigInteger

)
