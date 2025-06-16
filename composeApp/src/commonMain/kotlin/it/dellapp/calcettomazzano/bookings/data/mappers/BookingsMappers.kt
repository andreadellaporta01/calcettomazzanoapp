package it.dellapp.calcettomazzano.bookings.data.mappers

import it.dellapp.calcettomazzano.bookings.data.model.BookingsDto
import it.dellapp.calcettomazzano.bookings.domain.model.Booking

/**
 * Mappa un oggetto BookingsDto (Data Layer) a un oggetto Bookings (Domain Layer).
 *
 * @return L'oggetto Bookings mappato.
 */
fun BookingsDto.toDomain(): Booking {
    return Booking(
        id = this.id,
        fieldName = this.fieldName,
        userName = this.userName,
        date = this.date,
        time = this.time
    )
}