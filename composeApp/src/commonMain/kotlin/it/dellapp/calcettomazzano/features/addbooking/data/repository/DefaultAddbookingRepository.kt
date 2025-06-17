package it.dellapp.calcettomazzano.features.addbooking.data.repository

import it.dellapp.calcettomazzano.common.api.ApiClient
import it.dellapp.calcettomazzano.features.addbooking.data.mappers.toDomain
import it.dellapp.calcettomazzano.features.addbooking.data.model.AddbookingDto
import it.dellapp.calcettomazzano.features.addbooking.domain.model.FreeSlot
import it.dellapp.calcettomazzano.features.addbooking.domain.repository.AddbookingRepository


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
            return if(response?.status?.value!! >= 200 && response.status.value < 300) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Errore nella prenotazione"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}