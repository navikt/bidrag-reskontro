package no.nav.bidrag.reskontro.service

import no.nav.bidrag.commons.security.maskinporten.MaskinportenClientException
import no.nav.bidrag.domene.ident.Ident
import no.nav.bidrag.domene.ident.Organisasjonsnummer
import no.nav.bidrag.domene.ident.Personident
import no.nav.bidrag.domene.sak.Saksnummer
import no.nav.bidrag.domene.tid.Datoperiode
import no.nav.bidrag.reskontro.consumer.SkattReskontroConsumer
import no.nav.bidrag.reskontro.dto.consumer.ReskontroConsumerOutput
import no.nav.bidrag.reskontro.dto.request.SaksnummerRequest
import no.nav.bidrag.reskontro.dto.response.innkrevingssak.Bidragssak
import no.nav.bidrag.reskontro.dto.response.innkrevingssak.BidragssakMedSkyldner
import no.nav.bidrag.reskontro.dto.response.innkrevingssak.SaksinformasjonBarn
import no.nav.bidrag.reskontro.dto.response.innkrevingssak.Skyldner
import no.nav.bidrag.reskontro.dto.response.innkrevingssaksinformasjon.GjeldendeBetalingsordning
import no.nav.bidrag.reskontro.dto.response.innkrevingssaksinformasjon.Innkrevingssakshistorikk
import no.nav.bidrag.reskontro.dto.response.innkrevingssaksinformasjon.Innkrevingssaksinformasjon
import no.nav.bidrag.reskontro.dto.response.innkrevingssaksinformasjon.NyBetalingsordning
import no.nav.bidrag.reskontro.dto.response.innkrevingssaksinformasjon.Skyldnerinformasjon
import no.nav.bidrag.reskontro.dto.response.transaksjoner.Transaksjon
import no.nav.bidrag.reskontro.dto.response.transaksjoner.Transaksjoner
import no.nav.bidrag.reskontro.exceptions.IngenDataFraSkattException
import no.nav.bidrag.transport.person.PersonRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ReskontroService(private val skattReskontroConsumer: SkattReskontroConsumer) {
    fun hentInnkrevingssakPåSak(saksnummerRequest: SaksnummerRequest): Bidragssak {
        val innkrevingssakResponse = skattReskontroConsumer.hentInnkrevningssakerPåSak(saksnummerRequest.saksnummer.verdi.toLong())
        val innkrevingssak = validerOutput(innkrevingssakResponse)

        return Bidragssak(
            saksnummer = Saksnummer(innkrevingssak.bidragssak!!.bidragssaksnummer.toString()),
            bmGjeldFastsettelsesgebyr = innkrevingssak.bidragssak.bmGjeldFastsettelsesgebyr,
            bpGjeldFastsettelsesgebyr = innkrevingssak.bidragssak.bpGjeldFastsettelsesgebyr,
            bmGjeldRest = innkrevingssak.bidragssak.bmGjeldRest,
            barn =
                innkrevingssak.bidragssak.perBarnISak?.map {
                    SaksinformasjonBarn(
                        personident = Personident(it.fodselsnummer!!),
                        restGjeldOffentlig = it.restGjeldOffentlig!!,
                        restGjeldPrivat = it.restGjeldPrivat!!,
                        sumIkkeUtbetalt = it.sumIkkeUtbetalt!!,
                        sumForskuddUtbetalt = it.sumForskuddUtbetalt!!,
                        periode =
                            Datoperiode(
                                LocalDateTime.parse(it.periodeSisteDatoFom!!).toLocalDate(),
                                it.periodeSisteDatoTom?.let { tom -> LocalDateTime.parse(tom).toLocalDate().plusDays(1) },
                            ),
                        erStoppIUtbetaling = it.stoppUtbetaling!! == "J",
                    )
                } ?: emptyList(),
        )
    }

    fun hentInnkrevingssakPåPerson(personRequest: PersonRequest): BidragssakMedSkyldner {
        val innkrevingssakResponse = skattReskontroConsumer.hentInnkrevningssakerPåPerson(personRequest.ident)
        val innkrevingssak = validerOutput(innkrevingssakResponse)

        return BidragssakMedSkyldner(
            skyldner =
                Skyldner(
                    personident = Personident(innkrevingssak.skyldner!!.fodselsOrgnr!!),
                    innbetaltBeløpUfordelt = innkrevingssak.skyldner.innbetBelopUfordelt,
                    gjeldIlagtGebyr = innkrevingssak.skyldner.gjeldIlagtGebyr,
                ),
            bidragssak =
                Bidragssak(
                    saksnummer = Saksnummer(innkrevingssak.bidragssak!!.bidragssaksnummer.toString()),
                    bmGjeldFastsettelsesgebyr = innkrevingssak.bidragssak.bmGjeldFastsettelsesgebyr,
                    bpGjeldFastsettelsesgebyr = innkrevingssak.bidragssak.bpGjeldFastsettelsesgebyr,
                    bmGjeldRest = innkrevingssak.bidragssak.bmGjeldRest,
                    barn =
                        innkrevingssak.bidragssak.perBarnISak?.map {
                            SaksinformasjonBarn(
                                personident = Personident(it.fodselsnummer!!),
                                restGjeldOffentlig = it.restGjeldOffentlig!!,
                                restGjeldPrivat = it.restGjeldPrivat!!,
                                sumForskuddUtbetalt = it.sumForskuddUtbetalt!!,
                                restGjeldPrivatAndel = it.restGjeldPrivatAndel!!,
                                sumInnbetaltAndel = it.sumInnbetaltAndel!!,
                                sumForskuddUtbetaltAndel = it.sumForskuddUtbetaltAndel!!,
                            )
                        } ?: emptyList(),
                ),
        )
    }

    fun hentTransaksjonerPåBidragssak(saksnummerRequest: SaksnummerRequest): Transaksjoner {
        val transaksjonerResponse =
            skattReskontroConsumer.hentTransaksjonerPåBidragssak(saksnummerRequest.saksnummer.verdi.toLong())
        val transaksjoner = validerOutput(transaksjonerResponse)
        return opprettTransaksjonerResponse(transaksjoner)
    }

    fun hentTransaksjonerPåPerson(personRequest: PersonRequest): Transaksjoner {
        val transaksjonerResponse = skattReskontroConsumer.hentTransaksjonerPåPerson(personRequest.ident)
        val transaksjoner = validerOutput(transaksjonerResponse)
        return opprettTransaksjonerResponse(transaksjoner)
    }

    fun hentTransaksjonerPåTransaksjonsid(transaksjonsid: Long): Transaksjoner {
        val transaksjonerResponse = skattReskontroConsumer.hentTransaksjonerPåTransaksjonsId(transaksjonsid)
        val transaksjoner = validerOutput(transaksjonerResponse)
        return opprettTransaksjonerResponse(transaksjoner)
    }

    fun hentInformasjonOmInnkrevingssaken(personRequest: PersonRequest): Innkrevingssaksinformasjon {
        val innkrevingsinformasjonResponse = skattReskontroConsumer.hentInformasjonOmInnkrevingssaken(personRequest.ident)
        val innkrevingsinformasjon = validerOutput(innkrevingsinformasjonResponse)
        return Innkrevingssaksinformasjon(
            skyldnerinformasjon =
                Skyldnerinformasjon(
                    personident = Personident(innkrevingsinformasjon.skyldner!!.fodselsOrgnr!!),
                    sumLøpendeBidrag = innkrevingsinformasjon.skyldner.sumLopendeBidrag,
                    innkrevingssaksstatus = innkrevingsinformasjon.skyldner.statusInnkrevingssak!!,
                    fakturamåte = innkrevingsinformasjon.skyldner.fakturamaate!!,
                    sisteAktivitet = innkrevingsinformasjon.skyldner.sisteAktivitet!!,
                ),
            gjeldendeBetalingsordning =
                GjeldendeBetalingsordning(
                    typeBehandlingsordning = innkrevingsinformasjon.gjeldendeBetalingsordning!!.typeBetalingsordning!!,
                    kilde = Organisasjonsnummer(innkrevingsinformasjon.gjeldendeBetalingsordning.kildeOrgnummer!!),
                    kildeNavn = innkrevingsinformasjon.gjeldendeBetalingsordning.kildeNavn!!,
                    datoSisteGiro = LocalDateTime.parse(innkrevingsinformasjon.gjeldendeBetalingsordning.datoSisteGiro!!),
                    nesteForfall = LocalDateTime.parse(innkrevingsinformasjon.gjeldendeBetalingsordning.datoNesteForfall!!),
                    beløp = innkrevingsinformasjon.gjeldendeBetalingsordning.belop!!,
                    sistEndret = LocalDateTime.parse(innkrevingsinformasjon.gjeldendeBetalingsordning.datoSistEndret!!),
                    sistEndretÅrsak = innkrevingsinformasjon.gjeldendeBetalingsordning.aarsakSistEndret!!,
                    sumUbetalt = innkrevingsinformasjon.gjeldendeBetalingsordning.sumUbetalt!!,
                ),
            nyBetalingsordning =
                NyBetalingsordning(
                    dato = Datoperiode(LocalDateTime.parse(innkrevingsinformasjon.nyBetalingsordning!!.datoFraOgMed!!).toLocalDate(), null),
                    beløp = innkrevingsinformasjon.nyBetalingsordning.belop!!,
                ),
            innkrevingssakshistorikk =
                innkrevingsinformasjon.innkrevingssaksHistorikk!!.map {
                    Innkrevingssakshistorikk(
                        beskrivelse = it.beskrivelse!!,
                        ident = Ident(it.fodselsOrgNr!!),
                        navn = it.navn!!,
                        dato = LocalDateTime.parse(it.dato!!),
                        beløp = it.belop!!,
                    )
                },
        )
    }

    fun endreRmForSak(
        saksnummer: Saksnummer,
        barn: Personident,
        nyRm: Personident,
    ) {
        val endreRmResponse = skattReskontroConsumer.endreRmForSak(saksnummer.verdi.toLong(), barn, nyRm)
        validerOutput(endreRmResponse)
    }

    private fun opprettTransaksjonerResponse(transaksjoner: ReskontroConsumerOutput) =
        Transaksjoner(
            transaksjoner =
                transaksjoner.transaksjoner!!.map {
                    Transaksjon(
                        transaksjonsid = it.transaksjonsId,
                        transaksjonskode = it.kode!!,
                        beskrivelse = it.beskrivelse!!,
                        dato = LocalDateTime.parse(it.dato!!).toLocalDate(),
                        skyldner = Personident(it.kildeFodselsOrgNr!!),
                        mottaker = Personident(it.mottakerFodslesOrgNr!!),
                        beløp = it.opprinneligBeloep!!,
                        restBeløp = it.restBeloep!!,
                        beløpIOpprinneligValuta = it.valutaOpprinneligBeloep!!,
                        valutakode = it.valutakode!!,
                        saksnummer = Saksnummer(it.bidragssaksnummer.toString()),
                        periode =
                            Datoperiode(
                                LocalDateTime.parse(it.periodeSisteDatoFom!!).toLocalDate(),
                                it.periodeSisteDatoTom?.let { tom -> LocalDateTime.parse(tom).toLocalDate().plusDays(1) },
                            ),
                        barn = Personident(it.barnFodselsnr!!),
                        delytelsesid = it.bidragsId!!,
                        søknadstype = it.soeknadsType,
                    )
                },
        )

    /*
    Dette må gjøres siden det ikke returneres en korrekt HTTP statuskode i REST kallet mot Skatteetaten.
    Det blir derimot vedlagt en returnkode i responsen som avgjør om kallet var velykket eller ikke.
    Mulige returkoder er følgende:
        0  = OK
        -1 = Feilmelding
        -2 = Ugyldig aksjonskode - Aksjonskoden er satt individuelt for hvert endepunkt på vår side, burde ikke oppstå.
        -3 = Ingen data funnet - Tilsvarer 204 No Content.
     */
    private fun validerOutput(reskontroConsumerOutputResponse: ResponseEntity<ReskontroConsumerOutput>): ReskontroConsumerOutput {
        if (reskontroConsumerOutputResponse.statusCode == HttpStatus.UNAUTHORIZED) {
            throw MaskinportenClientException("Feil i maskinportentoken benyttet mot skatt")
        }
        if (reskontroConsumerOutputResponse.body == null) {
            error("Det mangler body i responsen fra Skatt!")
        }
        if (reskontroConsumerOutputResponse.body!!.retur == null) {
            error("Responsekoden mangler i responsen fra Skatt!")
        }

        when (reskontroConsumerOutputResponse.body!!.retur!!.kode) {
            0 -> return reskontroConsumerOutputResponse.body!!
            -1 -> error("Kallet mot skatt feilet med feilmelding: ${reskontroConsumerOutputResponse.body!!.retur!!.beskrivelse}")
            -2 -> error("Kallet mot skatt hadde ugyldig aksjonskode! Dette er ikke basert på innput og må rettes i koden/hos skatt.")
            -3 -> throw IngenDataFraSkattException("Skatt svarte med ingen data.")
            else -> error("Kallet mot skatt returnerte ukjent returnkode ${reskontroConsumerOutputResponse.body!!.retur!!.kode}")
        }
    }
}
