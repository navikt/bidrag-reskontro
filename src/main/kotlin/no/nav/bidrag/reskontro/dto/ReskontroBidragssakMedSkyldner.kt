package no.nav.bidrag.reskontro.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "Bidragssak med skyldner",
    description = "Inneholder informasjon om bidragssaken fra skatt med skyldner"
)
data class ReskontroBidragssakMedSkyldner(
    val skyldner: ReskontroSkyldner,
    val bidragssak: ReskontroBidragssak
)
