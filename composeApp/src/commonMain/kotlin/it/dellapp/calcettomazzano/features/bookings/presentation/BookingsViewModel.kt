package it.dellapp.calcettomazzano.features.bookings.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.dellapp.calcettomazzano.features.bookings.domain.usecase.GetBookingsDataUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Gestisce la logica di business e lo stato per la feature Bookings.
 */

class BookingsViewModel constructor(
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
            is BookingsAction.DateChanged -> getBookingsData(date = action.date.toString())
            is BookingsAction.AddBookingDateSelected -> viewModelScope.launch {
                _event.emit(
                    BookingsEvent.NavigateToAddBooking(action.date.toString())
                )
            }
            else -> {
                // Azione non gestita
            }
        }
    }

    private fun loadInitialData() {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        getBookingsData(today.toString())
    }

    private fun getBookingsData(date: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getBookingsDataUseCase(date = date)
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