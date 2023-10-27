package no.nav.bidrag.reskontro.dto.response.innkrevingssak

import no.nav.bidrag.domain.ident.PersonIdent
import no.nav.bidrag.domain.string.Saksnummer

data class EndreRmForSak(
    val saksnummer: Saksnummer,
    val barn: PersonIdent,
    val nyRm: PersonIdent
)
