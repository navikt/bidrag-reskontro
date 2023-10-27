package no.nav.bidrag.reskontro.dto

import no.nav.bidrag.domain.ident.PersonIdent
import no.nav.bidrag.domain.tid.FomDato
import no.nav.bidrag.domain.tid.TomDato

data class TransaksjonerPåPerson(
    val person: PersonIdent,
    val fomDato: FomDato,
    val tomDato: TomDato,
    val antallTransaksjoner: Int?
)
