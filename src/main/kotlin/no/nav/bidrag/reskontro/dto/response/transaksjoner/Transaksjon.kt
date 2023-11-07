package no.nav.bidrag.reskontro.dto.response.transaksjoner

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.regnskap.Transaksjonskode
import no.nav.bidrag.domene.ident.Personident
import no.nav.bidrag.domene.streng.Saksnummer
import no.nav.bidrag.domene.streng.Valutakode
import no.nav.bidrag.domene.tid.Periode
import java.math.BigDecimal
import java.time.LocalDate

@Schema(
    name = "Transaksjon",
    description = "Transaksjon på bidragssak."
)
data class Transaksjon(

    @field:Schema(
        description = "Id på transaksjonen."
    )
    val transaksjonsid: Long,

    @field:Schema(
        description = "Transaksjonskoden for transaksjonen."
    )
    val transaksjonskode: Transaksjonskode, //TODO(Legge inn gjennværende transaksjonskoder i domain. Her mangler trolig mange. Må få liste fra skatt)

    @field:Schema(
        description = "Beskrivelse av transaksjonen."
    )
    val beskrivelse: String,

    @field:Schema(
        description = "Dato uvist over hva" //TODO(Avklare hva slags dato dette er med skatt)
    )
    val dato: LocalDate,

    @field:Schema(
        description = "Ident til skyldner."
    )
    val skyldner: Personident,

    @field:Schema(
        description = "Ident til mottaker."
    )
    val mottaker: Personident,

    @field:Schema(
        description = "Opprinnelig beløp på transaksjonen."
    )
    val beløp: BigDecimal,

    @field:Schema(
        description = "Resterende beløp."
    )
    val restBeløp: BigDecimal,

    @field:Schema(
        description = "Beløp i opprinnelig valuta."
    )
    val beløpIOpprinneligValuta: BigDecimal,

    @field:Schema(
        description = "Valutakode." //TODO(Avklare om dette er valutakode for opprinnelige beløpet eller beløps valutakode)
    )
    val valutakode: Valutakode,

    @field:Schema(
        description = "Saksnummer for bidragssaken."
    )
    val saksnummer: Saksnummer,

    @field:Schema(
        description = "Periode for transaksjonen."
    )
    val periode: Periode<LocalDate>,

    @field:Schema(
        description = "Ident til barn."
    )
    val barn: Personident,

    @field:Schema(
        description = "Delytelsesid for transaksjonen."
    )
    val delytelsesid: String,

    @field:Schema(
        description = "Søknadstype. Tom for A6, A7, B10, D10, E10, F10, G2, H2, I2, J10, K1, 301, 302, 303, 304, 305, 306, 307, 308, 309, 371, 372, 373, 374, 375, 376, 377, 378, 378, 379, 400."
    )
    val søknadstype: String?
)
