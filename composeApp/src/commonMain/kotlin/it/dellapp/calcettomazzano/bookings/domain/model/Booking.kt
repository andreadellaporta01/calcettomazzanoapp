package it.dellapp.calcettomazzano.bookings.domain.model

/**
 * Rappresenta il modello di dominio principale per la feature Bookings.
 * Questa è la classe "pulita" che viene utilizzata all'interno dell'app (domain, presentation).
 *
 * @property id L'identificatore univoco del modello.
 * @property data Un campo dati di esempio per il modello.
 */
data class Booking(
    val id: Int,
    val fieldName: String,
    val userName: String,
    val date: String,    // "2025-06-12"
    val time: String
)