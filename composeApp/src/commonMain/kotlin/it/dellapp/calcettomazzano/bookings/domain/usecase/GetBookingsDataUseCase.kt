package it.dellapp.calcettomazzano.bookings.domain.usecase

import it.dellapp.calcettomazzano.bookings.domain.model.Booking
import it.dellapp.calcettomazzano.bookings.domain.repository.BookingsRepository


/**
 * Use case che incapsula la logica di business per ottenere i dati della feature Bookings.
 */
class GetBookingsDataUseCase  constructor(
    private val repository: BookingsRepository
) {

    /**
     * Esegue lo use case.
     */
    suspend operator fun invoke(date: String): Result<List<Booking>> {
        return repository.getBookingsData(date)
    }
}