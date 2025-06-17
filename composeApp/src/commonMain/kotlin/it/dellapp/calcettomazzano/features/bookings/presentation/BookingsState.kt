package it.dellapp.calcettomazzano.features.bookings.presentation

import it.dellapp.calcettomazzano.features.bookings.domain.model.Booking

/**
 * Rappresenta lo stato della UI per la feature Bookings.
 *
 * @property isLoading Indica se è in corso un'operazione di caricamento.
 */
data class BookingsState(
    val isLoading: Boolean = false,
    val bookings: List<Booking> = emptyList(),
    val error: String? = null
)