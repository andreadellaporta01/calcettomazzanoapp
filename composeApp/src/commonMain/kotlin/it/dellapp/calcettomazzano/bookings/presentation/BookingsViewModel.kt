package it.dellapp.calcettomazzano.bookings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope


import it.dellapp.calcettomazzano.bookings.domain.usecase.GetBookingsDataUseCase
import it.dellapp.calcettomazzano.bookings.presentation.BookingsAction
import it.dellapp.calcettomazzano.bookings.presentation.BookingsEvent
import it.dellapp.calcettomazzano.bookings.presentation.BookingsState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.toLocalDateTime

/**
 * Gestisce la logica di business e lo stato per la feature Bookings.
 */

class BookingsViewModel  constructor(
    private val getBookingsDataUseCase: GetBookingsDataUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(BookingsState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<BookingsEvent>()
    val event = _event.asSharedFlow()

    init {
        loadInitialData()
    }

    fun onAction(action: BookingsAction) {
        when (action) {
            // Aggiungere qui la gestione delle azioni specifiche
            else -> {
                // Azione non gestita
            }
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            getBookingsDataUseCase(date = today.toString())
                .onSuccess { bookings ->
                    _state.update { it.copy(bookings = bookings) }
                }
                .onFailure {
                    _state.update { it.copy(error = it.error) }
                }

            _state.update { it.copy(isLoading = false) }
        }
    }
}