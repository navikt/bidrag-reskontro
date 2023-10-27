package no.nav.bidrag.reskontro.dto.consumer

import java.math.BigInteger

data class ReskontroConsumerOutput(
    val innParametre: ReskontroConsumerInput,
    val skyldner: Skyldner,
    val bidragssak: Bidragssak,
    val transaksjoner: List<Transaksjon>? = null,
    val retur: Retur? = null,
    val gjeldendeBetalingsordning: GjeldendeBetalingsordning,
    val nyBetalingsordning: NyBetalingsordning? = null,
    val innkrevingssaksHistorikk: List<Aktivitet>? = emptyList()
)
data class Aktivitet(
    val beskrivelse: String? = null,
    val fodselsOrgNr: String? = null,
    val navn: String? = null,
    val dato: String? = null,
    val belop: BigInteger? = null
)

data class BarnISak(
    val fodselsnummer: String? = null,
    val restGjeldOffentlig: BigInteger? = null,
    val restGjeldPrivat: BigInteger? = null,
    val sumIkkeUtbetalt: BigInteger? = null,
    val sumForskuddUtbetalt: BigInteger? = null,
    val restGjeldPrivatAndel: BigInteger? = null,
    val sumInnbetaltAndel: BigInteger? = null,
    val sumForskuddUtbetaltAndel: BigInteger? = null,
    val periodeSisteDatoFom: String? = null,
    val periodeSisteDatoTom: String? = null,
    val stoppUtbetaling: String? = null
)

data class Bidragssak(
    val bidragssaksnummer: Long,
    val bmGjeldFastsettelsesgebyr: BigInteger,
    val bmGjeldRest: BigInteger,
    val bpGjeldFastsettelsesgebyr: BigInteger,
    val perBarnISak: List<BarnISak>? = emptyList()
)

data class GjeldendeBetalingsordning(
    val typeBetalingsordning: String? = null,
    val kildeOrgnummer: String? = null,
    val kildeNavn: String? = null,
    val datoSisteGiro: String? = null,
    val datoNesteForfall: String? = null,
    val belop: BigInteger? = null,
    val datoSistEndret: String? = null,
    val aarsakSistEndret: String? = null,
    val sumUbetalt: BigInteger? = null
)

data class NyBetalingsordning(
    val datoFraOgMed: String? = null,
    val belop: BigInteger? = null
)

data class Retur(
    val kode: Int,
    val beskrivelse: String? = null
)

data class Skyldner(
    val fodselsOrgnr: String? = null,
    val sumLopendeBidrag: BigInteger,
    val statusInnkrevingssak: String? = null,
    val fakturamaate: String? = null,
    val sisteAktivitet: String? = null,
    val innbetBelopUfordelt: BigInteger,
    val gjeldIlagtGebyr: BigInteger
)

data class Transaksjon(
    val transaksjonsId: Long,
    val kode: String? = null,
    val beskrivelse: String? = null,
    val dato: String? = null,
    val kildeFodselsOrgNr: String? = null,
    val mottakerFodslesOrgNr: String? = null,
    val opprinneligBeloep: BigInteger? = null,
    val restBeloep: BigInteger? = null,
    val valutaOpprinneligBeloep: BigInteger? = null,
    val valutakode: String? = null,
    val bidragssaksnummer: Long,
    val periodeSisteDatoFom: String? = null,
    val periodeSisteDatoTom: String? = null,
    val barnFodselsnr: String? = null,
    val bidragsId: String? = null,
    val soeknadsType: String? = null
)
