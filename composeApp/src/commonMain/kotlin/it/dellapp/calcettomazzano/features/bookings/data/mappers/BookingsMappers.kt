package it.dellapp.calcettomazzano.features.bookings.data.mappers

import it.dellapp.calcettomazzano.features.bookings.data.model.BookingDto
import it.dellapp.calcettomazzano.features.bookings.domain.model.Booking

/**
 * Mappa un oggetto BookingsDto (Data Layer) a un oggetto Bookings (Domain Layer).
 *
 * @return L'oggetto Bookings mappato.
 */
fun BookingDto.toDomain(): Booking {
    return Booking(
        date = date,
        startTime = startTime,
        endTime = endTime,
        firstName = firstName,
        lastName = lastName
    )
}