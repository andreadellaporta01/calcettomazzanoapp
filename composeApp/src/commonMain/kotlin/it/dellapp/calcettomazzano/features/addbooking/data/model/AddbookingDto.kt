package it.dellapp.calcettomazzano.features.addbooking.data.model

import kotlinx.serialization.Serializable

/**
 * Data Transfer Object (DTO) per la feature Addbooking ricevuto da una fonte esterna (es. API).
 * La sua struttura rispecchia fedelmente la risposta della fonte dati.
 *
 * @property uniqueId L'identificatore proveniente dall'API (potrebbe avere un nome diverso).
 * @property payload Il dato grezzo proveniente dall'API, che potrebbe essere nullo.
 */
@Serializable
data class AddbookingDto(
    val fieldId: Int,
    val date: String,
    val startTime: String,
    val endTime: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String? = null,
    val notes: String? = null
)