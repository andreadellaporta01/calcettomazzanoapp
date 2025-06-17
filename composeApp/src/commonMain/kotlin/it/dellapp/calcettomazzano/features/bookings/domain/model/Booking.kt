package it.dellapp.calcettomazzano.features.bookings.domain.model

/**
 * Rappresenta il modello di dominio principale per la feature Bookings.
 * Questa è la classe "pulita" che viene utilizzata all'interno dell'app (domain, presentation).
 *
 * @property id L'identificatore univoco del modello.
 * @property data Un campo dati di esempio per il modello.
 */
data class Booking(
    val date: String,
    val startTime: String,
    val endTime: String,
    val firstName: String,
    val lastName: String
)