package it.dellapp.calcettomazzano.bookings.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object (DTO) per la feature Bookings ricevuto da una fonte esterna (es. API).
 * La sua struttura rispecchia fedelmente la risposta della fonte dati.
 *
 * @property uniqueId L'identificatore proveniente dall'API (potrebbe avere un nome diverso).
 * @property payload Il dato grezzo proveniente dall'API, che potrebbe essere nullo.
 */
@Serializable
data class BookingsDto(
    val id: Int,
    val fieldName: String,
    val userName: String,
    val date: String,    // "2025-06-12"
    val time: String
)