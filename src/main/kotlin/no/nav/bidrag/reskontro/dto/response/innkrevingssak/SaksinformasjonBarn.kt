package no.nav.bidrag.reskontro.dto.response.innkrevingssak

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domain.ident.PersonIdent
import no.nav.bidrag.domain.tid.FomDato
import no.nav.bidrag.domain.tid.TomDato
import java.math.BigDecimal

@Schema(
    name = "SaksinformasjonBarn",
    description = "Liste over alle barn i bidragssaken med tilhørende innkrevingsinformasjon."
)
data class SaksinformasjonBarn(

    @field:Schema(
        description = "Ident til barnet."
    )
    val personIdent: PersonIdent,

    @field:Schema(
        description = "Resterende gjeld til det offentlige (C1, C2, C3)."
    )
    val restGjeldOffentlig: BigDecimal,

    @field:Schema(
        description = "Resterende gjeld privat (B1, D1, E1, F1, J1, J2)."
    )
    val restGjeldPrivat: BigDecimal,

    @field:Schema(
        description = "Sum av beløp som ikke er utbetalt tilbake til bidragspliktig. Dette kan skje ved for mye innbetalt eller annullerte beløp. " +
            "Beregnes ikke for kall på personIdent."
    )
    val sumIkkeUtbetalt: BigDecimal? = null,

    @field:Schema(
        description = "Sum av restbeløp på forskudd (A1)."
    )
    val sumForskuddUtbetalt: BigDecimal,

    @field:Schema(
        description = "Sum av restbeløp på forskudd (A1). " +
            "Beregnes ikke for kall på saksnummer."
    )
    val restGjeldPrivatAndel: BigDecimal? = null,

    @field:Schema(
        description = "Sum av restbeløp på forskudd (A1). " +
            "Beregnes ikke for kall på saksnummer."
    )
    val sumInnbetaltAndel: BigDecimal? = null,

    @field:Schema(
        description = "Sum av restbeløp på forskudd (A1). " +
            "Beregnes ikke for kall på saksnummer."
    )
    val sumForskuddUtbetaltAndel: BigDecimal? = null,

    @field:Schema(
        description = "Siste fra og med dato for B1, D1 eller F1. Angitt som første dato i måneden. " +
            "Beregnes ikke for kall på personIdent."
    )
    val fomDato: FomDato? = null,

    @field:Schema(
        description = "Siste til og med dato for B1, D1 eller F1. Angitt som første dato i måneden. " +
            "Beregnes ikke for kall på personIdent."
    )
    val tomDato: TomDato? = null,

    @field:Schema(
        description = "Angir om det er stopp i utbetaling. " +
            "Beregnes ikke for kall på personIdent."
    )
    val erStoppIUtbetaling: Boolean? = null
)
