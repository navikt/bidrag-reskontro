package no.nav.bidrag.reskontro.dto.response.innkrevingssaksinformasjon

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domain.tid.FomDato
import java.math.BigDecimal

@Schema(
    name = "NyBetalingsordning",
    description = "Inneholder informasjon om ny betalingsordning."
)
data class NyBetalingsordning (

    @field:Schema(
        description = "Dato når ny betalingsordning gjelder fra."
    )
    val fomDato: FomDato,

    @field:Schema(
        description = "Nytt beløp."
    )
    val beløp: BigDecimal
)