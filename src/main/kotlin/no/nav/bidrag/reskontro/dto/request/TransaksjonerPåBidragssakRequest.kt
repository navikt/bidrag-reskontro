package no.nav.bidrag.reskontro.dto.request

import no.nav.bidrag.domain.tid.FomDato
import no.nav.bidrag.domain.tid.TomDato

data class TransaksjonerPåBidragssakRequest(
    val saksnummer: Long,
    val fomDato: FomDato,
    val tomDato: TomDato,
    val antallTransaksjoner: Int?
)
