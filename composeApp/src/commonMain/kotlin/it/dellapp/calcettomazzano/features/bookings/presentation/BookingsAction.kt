package it.dellapp.calcettomazzano.features.bookings.presentation

import it.dellapp.calcettomazzano.features.bookings.domain.model.Booking
import kotlinx.datetime.LocalDate

/**
 * Definisce le azioni che possono essere inviate dalla UI al ViewModel
 * per la feature Bookings.
 */
sealed interface BookingsAction {
    data class DateChanged(val date: LocalDate) : BookingsAction
    data class AddBookingDateSelected(val date: LocalDate) : BookingsAction
    class Refresh(val date: LocalDate) : BookingsAction
    data class DeleteBooking(val bookingId: Int, val date: LocalDate) : BookingsAction
}