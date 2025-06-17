package it.dellapp.calcettomazzano.addbooking.data.mappers

import it.dellapp.calcettomazzano.addbooking.data.model.AddbookingDto
import it.dellapp.calcettomazzano.addbooking.data.model.SlotDto
import it.dellapp.calcettomazzano.addbooking.domain.model.Addbooking
import it.dellapp.calcettomazzano.addbooking.domain.model.FreeSlot

/**
 * Mappa un oggetto AddbookingDto (Data Layer) a un oggetto Addbooking (Domain Layer).
 *
 * @return L'oggetto Addbooking mappato.
 */
fun SlotDto.toDomain(): FreeSlot {
    return FreeSlot(
        startTime = startTime,
        endTime = endTime
    )
}