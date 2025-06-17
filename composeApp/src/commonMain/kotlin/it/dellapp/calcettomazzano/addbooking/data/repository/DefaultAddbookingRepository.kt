package it.dellapp.calcettomazzano.addbooking.data.repository

import it.dellapp.calcettomazzano.addbooking.data.mappers.toDomain
import it.dellapp.calcettomazzano.addbooking.data.model.AddbookingDto
import it.dellapp.calcettomazzano.addbooking.domain.model.FreeSlot
import it.dellapp.calcettomazzano.addbooking.domain.repository.AddbookingRepository
import it.dellapp.calcettomazzano.api.ApiClient
import kotlin.collections.map
import kotlin.collections.orEmpty


/**
 * Implementazione concreta del repository per la feature Addbooking.
 */
class DefaultAddbookingRepository  constructor() : AddbookingRepository {

    override suspend fun getFreeSlots(date: String): Result<List<FreeSlot>> {
        return try {
            val slots = ApiClient.getFreeSlots(date)
            Result.success(slots.orEmpty().map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addBooking(addbookingDto: AddbookingDto): Result<Unit> {
        return try {
            val response = ApiClient.addBooking(addbookingDto)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}