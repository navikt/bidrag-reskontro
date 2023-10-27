package no.nav.bidrag.reskontro.dto

import no.nav.bidrag.domain.ident.PersonIdent

data class EndreRmForSak(
    val bidragssaksnummer: Long,
    val barn: PersonIdent,
    val nyRm: PersonIdent
)
