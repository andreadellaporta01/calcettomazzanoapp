package it.dellapp.calcettomazzano.features.bookings.data.model

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
data class BookingDto(
    val date: String,
    val email: String,
    val endTime: String,
    val fieldId: Int,
    val firstName: String,
    val id: Int,
    val lastName: String,
    val code: String,
    val startTime: String
)