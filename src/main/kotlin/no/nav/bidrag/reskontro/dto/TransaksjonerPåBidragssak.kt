package no.nav.bidrag.reskontro.dto

import no.nav.bidrag.domain.tid.FomDato
import no.nav.bidrag.domain.tid.TomDato

data class TransaksjonerPåBidragssak(
    val bidragssaksnummer: Long,
    val fomDato: FomDato,
    val tomDato: TomDato,
    val antallTransaksjoner: Int?
)
