package no.nav.bidrag.reskontro.service

import no.nav.bidrag.domain.ident.PersonIdent
import no.nav.bidrag.reskontro.consumer.BidragPersonConsumer
import no.nav.bidrag.reskontro.model.HentPersonResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ExampleService(val bidragPersonConsumer: BidragPersonConsumer) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun hentDialogerForPerson(personIdent: PersonIdent): HentPersonResponse {
        logger.info("Henter samtalereferat for person")
        return bidragPersonConsumer.hentPerson(personIdent)
    }
}
