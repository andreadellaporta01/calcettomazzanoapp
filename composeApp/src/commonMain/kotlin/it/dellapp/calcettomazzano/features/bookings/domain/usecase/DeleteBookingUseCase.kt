package it.dellapp.calcettomazzano.features.bookings.domain.usecase

import it.dellapp.calcettomazzano.features.bookings.domain.model.Booking
import it.dellapp.calcettomazzano.features.bookings.domain.repository.BookingsRepository
import org.koin.core.scope.ScopeID


/**
 * Use case che incapsula la logica di business per ottenere i dati della feature Bookings.
 */
class DeleteBookingUseCase  constructor(
    private val repository: BookingsRepository
) {

    /**
     * Esegue lo use case.
     */
    suspend operator fun invoke(id: Int): Result<Unit> {
        return repository.deleteBooking(id)
    }
}