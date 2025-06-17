package it.dellapp.calcettomazzano.addbooking.domain.usecase

import it.dellapp.calcettomazzano.addbooking.data.model.AddbookingDto
import it.dellapp.calcettomazzano.addbooking.domain.model.Addbooking
import it.dellapp.calcettomazzano.addbooking.domain.repository.AddbookingRepository


/**
 * Use case che incapsula la logica di business per ottenere i dati della feature Addbooking.
 */
class GetAddbookingDataUseCase  constructor(
    private val repository: AddbookingRepository
) {

    /**
     * Esegue lo use case.
     */
    suspend operator fun invoke(addbookingDto: AddbookingDto): Result<Unit> {
        return repository.addBooking(addbookingDto)
    }
}