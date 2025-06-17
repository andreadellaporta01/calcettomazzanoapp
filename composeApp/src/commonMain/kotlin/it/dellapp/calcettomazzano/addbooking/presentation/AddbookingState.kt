package it.dellapp.calcettomazzano.addbooking.presentation

import it.dellapp.calcettomazzano.addbooking.domain.model.FreeSlot

/**
 * Rappresenta lo stato della UI per la feature Addbooking.
 *
 * @property isLoading Indica se è in corso un'operazione di caricamento.
 */
data class AddbookingState(
    val isLoading: Boolean = false,
    val freeSlots: List<FreeSlot> = emptyList(),
    val bookingSuccess: Unit? = null,
    val error: String? = null
)