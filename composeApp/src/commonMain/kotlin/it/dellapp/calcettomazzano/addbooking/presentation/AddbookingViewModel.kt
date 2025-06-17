package it.dellapp.calcettomazzano.addbooking.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope


import it.dellapp.calcettomazzano.addbooking.domain.usecase.GetAddbookingDataUseCase
import it.dellapp.calcettomazzano.addbooking.domain.usecase.GetFreeSlotsDataUseCase
import it.dellapp.calcettomazzano.addbooking.presentation.AddbookingAction
import it.dellapp.calcettomazzano.addbooking.presentation.AddbookingEvent
import it.dellapp.calcettomazzano.addbooking.presentation.AddbookingState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Gestisce la logica di business e lo stato per la feature Addbooking.
 */

class AddbookingViewModel  constructor(
    private val getAddbookingDataUseCase: GetAddbookingDataUseCase,
    private val getFreeSlotsDataUseCase: GetFreeSlotsDataUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AddbookingState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<AddbookingEvent>()
    val event = _event.asSharedFlow()

    fun onAction(action: AddbookingAction) {
        when (action) {
            is AddbookingAction.GetFreeSlots -> getFreeSlots(action.date)
            else -> {
                // Azione non gestita
            }
        }
    }

    private fun getFreeSlots(date: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            getFreeSlotsDataUseCase(date)
                .onSuccess { slots ->
                    _state.update { it.copy(freeSlots = slots) }
                }
                .onFailure { error ->
                    _state.update { it.copy(error = "Errore nel recupero degli slot") }
                }

            _state.update { it.copy(isLoading = false) }
        }
    }
}