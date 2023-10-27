package no.nav.bidrag.reskontro.dto.request

import no.nav.bidrag.domain.ident.PersonIdent
import no.nav.bidrag.domain.tid.FomDato
import no.nav.bidrag.domain.tid.TomDato

data class TransaksjonerPåPersonRequest(
    val person: PersonIdent,
    val fomDato: FomDato,
    val tomDato: TomDato,
    val antallTransaksjoner: Int?
)
