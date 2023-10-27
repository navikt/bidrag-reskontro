package no.nav.bidrag.reskontro.service

import no.nav.bidrag.domain.ident.PersonIdent
import no.nav.bidrag.domain.tid.FomDato
import no.nav.bidrag.domain.tid.TomDato
import no.nav.bidrag.reskontro.consumer.SkattReskontroConsumer
import no.nav.bidrag.reskontro.dto.request.InnkrevingssakPåPersonRequest
import no.nav.bidrag.reskontro.dto.request.InnkrevingssakPåSaksnummerRequest
import no.nav.bidrag.reskontro.dto.response.Bidragssak
import no.nav.bidrag.reskontro.dto.response.BidragssakMedSkyldner
import no.nav.bidrag.reskontro.dto.consumer.ReskontroConsumerOutput
import no.nav.bidrag.reskontro.dto.response.SaksinformasjonBarn
import no.nav.bidrag.reskontro.dto.response.Skyldner
import no.nav.bidrag.reskontro.exceptions.IngenDataFraSkattException
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ReskontroService(private val skattReskontroConsumer: SkattReskontroConsumer) {

    fun hentInnkrevingssakPåSak(saksnummer: InnkrevingssakPåSaksnummerRequest): Bidragssak {
        val innkrevingssakResponse = skattReskontroConsumer.hentInnkrevningssakerPåSak(saksnummer.saksnummer)
        val innkrevingssak = validerOutput(innkrevingssakResponse)

        return Bidragssak(
            saksnummer = innkrevingssak.bidragssak.bidragssaksnummer,
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
                saksnummer = innkrevingssak.bidragssak.bidragssaksnummer,
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
        saksnummer: Long,
        fomDato: FomDato,
        tomDato: TomDato,
        antallTransaksjoner: Int?
    ): String {
        val transaksjonerResponse = skattReskontroConsumer.hentTransaksjonerPåBidragssak(saksnummer, fomDato, tomDato, antallTransaksjoner)
        val transaksjoner = validerOutput(transaksjonerResponse)




        return "transaksjoner"
    }

    fun hentTransaksjonerPåPerson(
        person: PersonIdent,
        fomDato: FomDato,
        tomDato: TomDato,
        antallTransaksjoner: Int?
    ): String {
        val transaksjonerResponse = skattReskontroConsumer.hentTransaksjonerPåPerson(person, fomDato, tomDato, antallTransaksjoner)
        return "transaksjoner"
    }

    fun hentTransaksjonerPåTransaksjonsid(transaksjonsid: Long, fomDato: FomDato, tomDato: TomDato): String {
        val transaksjonerResponse = skattReskontroConsumer.hentTransaksjonerPåTransaksjonsId(transaksjonsid, fomDato, tomDato)
        return "transaksjoner"
    }

    fun hentInformasjonOmInnkrevingssaken(person: PersonIdent): String {
        val innkrevingsinformasjonResponse = skattReskontroConsumer.hentInformasjonOmInnkrevingssaken(person)
        return "informasjonOmInnkrevingssaken"
    }

    fun endreRmForSak(saksnummer: Long, barn: PersonIdent, nyRm: PersonIdent): String {
        val endreRmResponse = skattReskontroConsumer.endreRmForSak(saksnummer, barn, nyRm)
        return "endreRM"
    }

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
