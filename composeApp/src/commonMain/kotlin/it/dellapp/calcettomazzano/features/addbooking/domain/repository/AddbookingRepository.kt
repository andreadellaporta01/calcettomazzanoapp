package it.dellapp.calcettomazzano.features.addbooking.domain.repository

import it.dellapp.calcettomazzano.features.addbooking.data.model.AddbookingDto
import it.dellapp.calcettomazzano.features.addbooking.domain.model.FreeSlot

/**
 * Interfaccia che definisce il contratto per il repository della feature Addbooking.
 */
interface AddbookingRepository {

    /**
     * Recupera i dati per la feature Addbooking.
     *
     * @return Un oggetto Result contenente il modello di dominio Addbooking in caso di successo,
     * o un'eccezione in caso di fallimento.
     */
    suspend fun getFreeSlots(date: String): Result<List<FreeSlot>>
    suspend fun addBooking(addbookingDto: AddbookingDto): Result<Unit>
}