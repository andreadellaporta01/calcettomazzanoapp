package it.dellapp.calcettomazzano.features.addbooking.presentation

import it.dellapp.calcettomazzano.features.addbooking.data.model.AddbookingDto

/**
 * Definisce le azioni che possono essere inviate dalla UI al ViewModel
 * per la feature Addbooking.
 */
sealed interface AddbookingAction {
    data class GetFreeSlots(val date: String) : AddbookingAction
    data class AddBooking(val booking: AddbookingDto) : AddbookingAction
}