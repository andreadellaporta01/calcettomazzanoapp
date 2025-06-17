package it.dellapp.calcettomazzano.bookings.presentation

import it.dellapp.calcettomazzano.bookings.domain.model.Booking
import kotlinx.datetime.LocalDate

/**
 * Definisce le azioni che possono essere inviate dalla UI al ViewModel
 * per la feature Bookings.
 */
sealed interface BookingsAction {
    data class BookingClicked(val booking: Booking) : BookingsAction
    class AddBookingClicked() : BookingsAction
    data class DateChanged(val date: LocalDate) : BookingsAction
}