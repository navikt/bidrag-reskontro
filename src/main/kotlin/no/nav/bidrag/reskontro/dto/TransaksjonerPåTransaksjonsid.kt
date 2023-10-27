package no.nav.bidrag.reskontro.dto

import no.nav.bidrag.domain.ident.PersonIdent
import no.nav.bidrag.domain.tid.FomDato
import no.nav.bidrag.domain.tid.TomDato

data class TransaksjonerPåTransaksjonsid(
    val transaksjonsid: Long,
    val fomDato: FomDato,
    val tomDato: TomDato
)
