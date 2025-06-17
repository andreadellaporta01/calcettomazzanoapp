package it.dellapp.calcettomazzano.addbooking.data.model

import kotlinx.datetime.LocalTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object (DTO) per la feature Addbooking ricevuto da una fonte esterna (es. API).
 * La sua struttura rispecchia fedelmente la risposta della fonte dati.
 *
 * @property uniqueId L'identificatore proveniente dall'API (potrebbe avere un nome diverso).
 * @property payload Il dato grezzo proveniente dall'API, che potrebbe essere nullo.
 */
@Serializable
data class SlotDto(
    @Contextual val startTime: LocalTime,
    @Contextual val endTime: LocalTime
)