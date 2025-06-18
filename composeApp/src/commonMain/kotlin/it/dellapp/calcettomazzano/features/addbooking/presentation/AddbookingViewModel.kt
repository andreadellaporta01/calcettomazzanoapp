package it.dellapp.calcettomazzano.features.addbooking.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.dellapp.calcettomazzano.features.addbooking.data.model.AddbookingDto
import it.dellapp.calcettomazzano.features.addbooking.domain.usecase.GetAddbookingDataUseCase
import it.dellapp.calcettomazzano.features.addbooking.domain.usecase.GetFreeSlotsDataUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
            is AddbookingAction.AddBooking -> addBooking(action.booking)
            is AddbookingAction.Cancel -> viewModelScope.launch {
                _event.emit(AddbookingEvent.NavigateBack())
            }
            else -> {
                // Azione non gestita
            }
        }
    }

    private fun addBooking(addbookingDto: AddbookingDto) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            getAddbookingDataUseCase(addbookingDto)
                .onSuccess { success ->
                    _state.update { it.copy(bookingSuccess = Unit) }
                    _event.emit(AddbookingEvent.AddBookingSuccess())
                }
                .onFailure { error ->
                    _state.update { it.copy(error = "Errore nella prenotazione") }
                }

            _state.update { it.copy(isLoading = false) }
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