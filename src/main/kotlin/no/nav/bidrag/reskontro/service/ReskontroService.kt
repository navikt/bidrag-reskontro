package no.nav.bidrag.reskontro.service

import no.nav.bidrag.domain.enums.regnskap.Transaksjonskode
import no.nav.bidrag.domain.ident.Ident
import no.nav.bidrag.domain.ident.Organisasjonsnummer
import no.nav.bidrag.domain.ident.PersonIdent
import no.nav.bidrag.domain.string.Saksnummer
import no.nav.bidrag.domain.string.Valutakode
import no.nav.bidrag.domain.tid.FomDato
import no.nav.bidrag.domain.tid.TomDato
import no.nav.bidrag.reskontro.consumer.SkattReskontroConsumer
import no.nav.bidrag.reskontro.dto.consumer.ReskontroConsumerOutput
import no.nav.bidrag.reskontro.dto.request.InnkrevingssakPåPersonRequest
import no.nav.bidrag.reskontro.dto.request.InnkrevingssakPåSaksnummerRequest
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
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class ReskontroService(private val skattReskontroConsumer: SkattReskontroConsumer) {

    fun hentInnkrevingssakPåSak(saksnummerRequest: InnkrevingssakPåSaksnummerRequest): Bidragssak {
        val innkrevingssakResponse = skattReskontroConsumer.hentInnkrevningssakerPåSak(saksnummerRequest.saksnummer.verdi.toLong())
        val innkrevingssak = validerOutput(innkrevingssakResponse)

        return Bidragssak(
            saksnummer = Saksnummer(innkrevingssak.bidragssak.bidragssaksnummer.toString()),
            bmGjeldFastsettelsesgebyr = innkrevingssak.bidragssak.bmGjeldFastsettelsesgebyr,
            bpGjeldFastsettelsesgebyr = innkrevingssak.bidragssak.bpGjeldFastsettelsesgebyr,
            bmGjeldRest = innkrevingssak.bidragssak.bmGjeldRest,
            barn = innkrevingssak.bidragssak.perBarnISak?.map {
                SaksinformasjonBarn(
                    personIdent = PersonIdent(it.fodselsnummer!!),
                    restGjeldOffentlig = it.restGjeldOffentlig!!,
                    restGjeldPrivat = it.restGjeldPrivat!!,
                    sumIkkeUtbetalt = it.sumIkkeUtbetalt!!,
                    sumForskuddUtbetalt = it.sumForskuddUtbetalt!!,
                    sisteFomDato = FomDato(LocalDate.parse(it.periodeSisteDatoFom!!)),
                    sisteTomDato = TomDato(LocalDate.parse(it.periodeSisteDatoTom!!)),
                    erStoppIUtbetaling = it.stoppUtbetaling!! == "J"
                )
            } ?: emptyList()
        )
    }

    fun hentInnkrevingssakPåPerson(personIdent: InnkrevingssakPåPersonRequest): BidragssakMedSkyldner {
        val innkrevingssakResponse = skattReskontroConsumer.hentInnkrevningssakerPåPerson(personIdent.personIdent)
        val innkrevingssak = validerOutput(innkrevingssakResponse)

        return BidragssakMedSkyldner(
            skyldner = Skyldner(
                personIdent = PersonIdent(innkrevingssak.skyldner.fodselsOrgnr!!),
                innbetaltBeløpUfordelt = innkrevingssak.skyldner.innbetBelopUfordelt,
                gjeldIlagtGebyr = innkrevingssak.skyldner.gjeldIlagtGebyr
            ),
            bidragssak = Bidragssak(
                saksnummer = Saksnummer(innkrevingssak.bidragssak.bidragssaksnummer.toString()),
                bmGjeldFastsettelsesgebyr = innkrevingssak.bidragssak.bmGjeldFastsettelsesgebyr,
                bpGjeldFastsettelsesgebyr = innkrevingssak.bidragssak.bpGjeldFastsettelsesgebyr,
                bmGjeldRest = innkrevingssak.bidragssak.bmGjeldRest,
                barn = innkrevingssak.bidragssak.perBarnISak?.map {
                    SaksinformasjonBarn(
                        personIdent = PersonIdent(it.fodselsnummer!!),
                        restGjeldOffentlig = it.restGjeldOffentlig!!,
                        restGjeldPrivat = it.restGjeldPrivat!!,
                        sumForskuddUtbetalt = it.sumForskuddUtbetalt!!,
                        restGjeldPrivatAndel = it.restGjeldPrivatAndel!!,
                        sumInnbetaltAndel = it.sumInnbetaltAndel!!,
                        sumForskuddUtbetaltAndel = it.sumForskuddUtbetaltAndel!!
                    )
                } ?: emptyList()
            )
        )
    }

    fun hentTransaksjonerPåBidragssak(
        saksnummer: Saksnummer,
        antallTransaksjoner: Int?
    ): Transaksjoner {
        val transaksjonerResponse =
            skattReskontroConsumer.hentTransaksjonerPåBidragssak(saksnummer.verdi.toLong(), antallTransaksjoner)
        val transaksjoner = validerOutput(transaksjonerResponse)
        return opprettTransaksjonerResponse(transaksjoner)
    }

    fun hentTransaksjonerPåPerson(
        person: PersonIdent,
        antallTransaksjoner: Int?
    ): Transaksjoner {
        val transaksjonerResponse = skattReskontroConsumer.hentTransaksjonerPåPerson(person, antallTransaksjoner)
        val transaksjoner = validerOutput(transaksjonerResponse)
        return opprettTransaksjonerResponse(transaksjoner)
    }

    fun hentTransaksjonerPåTransaksjonsid(transaksjonsid: Long): Transaksjoner {
        val transaksjonerResponse = skattReskontroConsumer.hentTransaksjonerPåTransaksjonsId(transaksjonsid)
        val transaksjoner = validerOutput(transaksjonerResponse)
        return opprettTransaksjonerResponse(transaksjoner)
    }

    fun hentInformasjonOmInnkrevingssaken(person: InnkrevingssakPåPersonRequest): Innkrevingssaksinformasjon {
        val innkrevingsinformasjonResponse = skattReskontroConsumer.hentInformasjonOmInnkrevingssaken(person.personIdent)
        val innkrevingsinformasjon = validerOutput(innkrevingsinformasjonResponse)
        return Innkrevingssaksinformasjon(
            skyldnerinformasjon = Skyldnerinformasjon(
                personIdent = PersonIdent(innkrevingsinformasjon.skyldner.fodselsOrgnr!!),
                sumLøpendeBidrag = innkrevingsinformasjon.skyldner.sumLopendeBidrag,
                innkrevingssaksstatus = innkrevingsinformasjon.skyldner.statusInnkrevingssak!!,
                fakturamåte = innkrevingsinformasjon.skyldner.fakturamaate!!,
                sisteAktivitet = innkrevingsinformasjon.skyldner.sisteAktivitet!!
            ),
             gjeldendeBetalingsordning = GjeldendeBetalingsordning(
                 typeBehandlingsordning = innkrevingsinformasjon.gjeldendeBetalingsordning.typeBetalingsordning!!,
                 kilde = Organisasjonsnummer(innkrevingsinformasjon.gjeldendeBetalingsordning.kildeOrgnummer!!),
                 kildeNavn = innkrevingsinformasjon.gjeldendeBetalingsordning.kildeNavn!!,
                 datoSisteGiro = LocalDateTime.parse(innkrevingsinformasjon.gjeldendeBetalingsordning.datoSisteGiro!!),
                 nesteForfall = LocalDateTime.parse(innkrevingsinformasjon.gjeldendeBetalingsordning.datoNesteForfall!!),
                 beløp = innkrevingsinformasjon.gjeldendeBetalingsordning.belop!!,
                 sistEndret = LocalDateTime.parse(innkrevingsinformasjon.gjeldendeBetalingsordning.datoSistEndret!!),
                 sistEndretÅrsak = innkrevingsinformasjon.gjeldendeBetalingsordning.aarsakSistEndret!!,
                 sumUbetalt = innkrevingsinformasjon.gjeldendeBetalingsordning.sumUbetalt!!
             ),
             nyBetalingsordning = NyBetalingsordning(
                 fomDato = FomDato(LocalDate.parse(innkrevingsinformasjon.nyBetalingsordning!!.datoFraOgMed!!)),
                 beløp = innkrevingsinformasjon.nyBetalingsordning.belop!!
             ),
             innkrevingssakshistorikk = innkrevingsinformasjon.innkrevingssaksHistorikk!!.map {
                 Innkrevingssakshistorikk(
                     beskrivelse = it.beskrivelse!!,
                     ident = Ident(it.fodselsOrgNr!!),
                     navn = it.navn!!,
                     dato = LocalDateTime.parse(it.dato!!),
                     beløp = it.belop!!
                 )
             }
        )
    }

    fun endreRmForSak(saksnummer: Saksnummer, barn: PersonIdent, nyRm: PersonIdent) {
        val endreRmResponse = skattReskontroConsumer.endreRmForSak(saksnummer.verdi.toLong(), barn, nyRm)
        validerOutput(endreRmResponse)
    }
    private fun opprettTransaksjonerResponse(transaksjoner: ReskontroConsumerOutput) = Transaksjoner(
        transaksjoner = transaksjoner.transaksjoner!!.map {
            Transaksjon(
                transaksjonsid = it.transaksjonsId,
                transaksjonskode = Transaksjonskode.valueOf(it.kode!!),
                beskrivelse = it.beskrivelse!!,
                dato = LocalDate.parse(it.dato!!),
                skyldner = PersonIdent(it.kildeFodselsOrgNr!!),
                mottaker = PersonIdent(it.mottakerFodslesOrgNr!!),
                beløp = it.opprinneligBeloep!!,
                restBeløp = it.restBeloep!!,
                beløpIOpprinneligValuta = it.valutaOpprinneligBeloep!!,
                valutakode = Valutakode(it.valutakode!!),
                saksnummer = Saksnummer(it.bidragssaksnummer.toString()),
                fomDato = FomDato(LocalDate.parse(it.periodeSisteDatoFom!!)),
                tomDato = TomDato(LocalDate.parse(it.periodeSisteDatoTom!!)),
                barn = PersonIdent(it.barnFodselsnr!!),
                delytelsesid = it.bidragsId!!,
                søknadstype = it.soeknadsType
            )
        }
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
        if (reskontroConsumerOutputResponse.body == null) {
            error("Det mangler body i responsen fra Skatt!")
        } else if (reskontroConsumerOutputResponse.body!!.retur == null) {
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
