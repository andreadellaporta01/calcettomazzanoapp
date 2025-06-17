package it.dellapp.calcettomazzano.features.bookings.presentation

/**
 * Definisce gli eventi "one-off" che il ViewModel può inviare alla UI.
 * Questi eventi sono pensati per essere consumati una sola volta (es. navigazione, snackbar).
 */
sealed interface BookingsEvent {
    // Esempio: data class NavigateToDetails(val screenId: String) : BookingsEvent
    data class NavigateToAddBooking(val date: String) : BookingsEvent
}