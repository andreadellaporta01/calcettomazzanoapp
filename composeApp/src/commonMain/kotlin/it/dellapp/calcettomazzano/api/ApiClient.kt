package it.dellapp.calcettomazzano.api

import it.dellapp.calcettomazzano.bookings.data.model.BookingsDto
import networking.DevengNetworkingModule
import networking.util.DevengHttpMethod

object ApiClient {
    suspend fun getBookings(date: String): List<BookingsDto>? {
        return DevengNetworkingModule.sendRequest<Unit, List<BookingsDto>?>(
            endpoint = "/fields/1/bookings",
            requestMethod = DevengHttpMethod.GET,
            queryParameters = mapOf("date" to date),
        )
    }
}