package it.dellapp.calcettomazzano.common.api

import io.ktor.client.statement.HttpResponse
import it.dellapp.calcettomazzano.features.addbooking.data.model.*
import it.dellapp.calcettomazzano.features.bookings.data.model.*
import networking.DevengNetworkingModule
import networking.util.DevengHttpMethod

object ApiClient {
    suspend fun getBookings(date: String): List<BookingDto>? {
        return DevengNetworkingModule.sendRequest<Unit, List<BookingDto>?>(
            endpoint = "/fields/1/bookings",
            requestMethod = DevengHttpMethod.GET,
            queryParameters = mapOf("date" to date),
        )
    }

    suspend fun getFreeSlots(date: String): List<SlotDto>? {
        return DevengNetworkingModule.sendRequest<Unit, List<SlotDto>?>(
            endpoint = "/fields/1/free-slots",
            requestMethod = DevengHttpMethod.GET,
            queryParameters = mapOf("date" to date),
        )
    }

    suspend fun addBooking(addbookingDto: AddbookingDto): HttpResponse? {
        return DevengNetworkingModule.sendRequestForHttpResponse<AddbookingDto>(
            endpoint = "/bookings",
            requestMethod = DevengHttpMethod.POST,
            requestBody = addbookingDto
        )
    }
}