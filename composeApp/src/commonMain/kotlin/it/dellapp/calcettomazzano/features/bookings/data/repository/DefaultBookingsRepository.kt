package it.dellapp.calcettomazzano.features.bookings.data.repository

import it.dellapp.calcettomazzano.common.api.ApiClient
import it.dellapp.calcettomazzano.features.bookings.data.mappers.toDomain
import it.dellapp.calcettomazzano.features.bookings.domain.model.Booking
import it.dellapp.calcettomazzano.features.bookings.domain.repository.BookingsRepository


/**
 * Implementazione concreta del repository per la feature Bookings.
 */
class DefaultBookingsRepository constructor() : BookingsRepository {

    override suspend fun getBookingsData(date: String): Result<List<Booking>> {
        return try {
            val bookings = ApiClient.getBookings(date)
            Result.success(bookings.orEmpty().map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}