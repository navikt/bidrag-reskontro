package no.nav.bidrag.reskontro.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigInteger

@Schema(
    name = "Saksinformasjon",
    description = "Inneholder informasjon om bidragssaken fra skatt"
)
data class ReskontroBidragssak(

    @field:Schema(
        description = "Identifikasjonen til bidragssaken."
    )
    val bidragssaksnummer: Long,

    @field:Schema(
        description = "Resterende gjeld til BM på fastsettelsesgebyret (G1)."
    )
    val bmGjeldFastsettelsesgebyr: BigInteger,

    @field:Schema(
        description = "Resterende gjeld til BM. Gjelder for H1 - Tilbakekreving."
    )
    val bmGjeldRest: BigInteger,

    @field:Schema(
        description = "Resterende gjeld til BP på fastsettelsesgebyret (G1)."
    )
    val bpGjeldFastsettelsesgebyr: BigInteger,

    @field:Schema(
        description = "Liste over alle barn i bidragssaken med tilhørende innkrevingsinformasjon."
    )
    val barn: List<ReskontroSaksinformasjonBarn>
)
