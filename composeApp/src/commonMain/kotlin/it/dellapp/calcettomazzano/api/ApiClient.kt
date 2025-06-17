package it.dellapp.calcettomazzano.api

import it.dellapp.calcettomazzano.addbooking.data.model.AddbookingDto
import it.dellapp.calcettomazzano.addbooking.data.model.SlotDto
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

    suspend fun getFreeSlots(date: String): List<SlotDto>? {
        return DevengNetworkingModule.sendRequest<Unit, List<SlotDto>?>(
            endpoint = "/fields/1/free-slots",
            requestMethod = DevengHttpMethod.GET,
            queryParameters = mapOf("date" to date),
        )
    }

    suspend fun addBooking(addbookingDto: AddbookingDto): Unit? {
        return DevengNetworkingModule.sendRequest<AddbookingDto, Unit?>(
            endpoint = "/bookings",
            requestMethod = DevengHttpMethod.POST,
            requestBody = addbookingDto
        )
    }
}