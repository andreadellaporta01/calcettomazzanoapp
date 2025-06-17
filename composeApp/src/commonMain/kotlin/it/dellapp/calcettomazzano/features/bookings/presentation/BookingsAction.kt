package it.dellapp.calcettomazzano.features.bookings.presentation

import it.dellapp.calcettomazzano.features.bookings.domain.model.Booking
import kotlinx.datetime.LocalDate

/**
 * Definisce le azioni che possono essere inviate dalla UI al ViewModel
 * per la feature Bookings.
 */
sealed interface BookingsAction {
    data class BookingClicked(val booking: Booking) : BookingsAction
    data class DateChanged(val date: LocalDate) : BookingsAction
    data class AddBookingDateSelected(val date: LocalDate) : BookingsAction
}