package it.dellapp.calcettomazzano.features.bookings.domain.repository

import it.dellapp.calcettomazzano.features.bookings.domain.model.Booking

/**
 * Interfaccia che definisce il contratto per il repository della feature Bookings.
 */
interface BookingsRepository {

    /**
     * Recupera i dati per la feature Bookings.
     *
     * @return Un oggetto Result contenente il modello di dominio Bookings in caso di successo,
     * o un'eccezione in caso di fallimento.
     */
    suspend fun getBookingsData(date: String): Result<List<Booking>>
}