package it.dellapp.calcettomazzano.bookings.presentation

import it.dellapp.calcettomazzano.bookings.domain.model.Booking

/**
 * Definisce le azioni che possono essere inviate dalla UI al ViewModel
 * per la feature Bookings.
 */
sealed interface BookingsAction {
    data class BookingClicked(val booking: Booking) : BookingsAction
    class AddBookingClicked() : BookingsAction
}