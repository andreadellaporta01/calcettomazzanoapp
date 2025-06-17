package it.dellapp.calcettomazzano.features.addbooking.domain.usecase

import it.dellapp.calcettomazzano.features.addbooking.data.model.AddbookingDto
import it.dellapp.calcettomazzano.features.addbooking.domain.model.Addbooking
import it.dellapp.calcettomazzano.features.addbooking.domain.model.FreeSlot
import it.dellapp.calcettomazzano.features.addbooking.domain.repository.AddbookingRepository


/**
 * Use case che incapsula la logica di business per ottenere i dati della feature Addbooking.
 */
class GetFreeSlotsDataUseCase  constructor(
    private val repository: AddbookingRepository
) {

    /**
     * Esegue lo use case.
     */
    suspend operator fun invoke(date: String): Result<List<FreeSlot>> {
        return repository.getFreeSlots(date)
    }
}