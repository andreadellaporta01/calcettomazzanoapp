package it.dellapp.calcettomazzano.features.addbooking.presentation

/**
 * Definisce gli eventi "one-off" che il ViewModel può inviare alla UI.
 * Questi eventi sono pensati per essere consumati una sola volta (es. navigazione, snackbar).
 */
sealed interface AddbookingEvent {
    // Esempio: data class NavigateToDetails(val screenId: String) : AddbookingEvent
    class NavigateBack : AddbookingEvent
    class AddBookingSuccess : AddbookingEvent
}