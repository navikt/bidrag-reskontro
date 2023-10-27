package no.nav.bidrag.reskontro.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigInteger

@Schema(
    name = "Bidragssak",
    description = "Inneholder informasjon om bidragssaken fra skatt"
)
data class Bidragssak(

    @field:Schema(
        description = "Identifikasjonen til bidragssaken."
    )
    val saksnummer: Long,

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
    val barn: List<SaksinformasjonBarn>
)
