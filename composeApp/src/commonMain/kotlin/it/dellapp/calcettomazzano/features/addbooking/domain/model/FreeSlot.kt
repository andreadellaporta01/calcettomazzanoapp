package it.dellapp.calcettomazzano.features.addbooking.domain.model

import kotlinx.datetime.LocalTime
import kotlinx.serialization.Contextual

/**
 * Rappresenta il modello di dominio principale per la feature Addbooking.
 * Questa è la classe "pulita" che viene utilizzata all'interno dell'app (domain, presentation).
 *
 * @property id L'identificatore univoco del modello.
 * @property data Un campo dati di esempio per il modello.
 */
data class FreeSlot(
    val startTime: String,
    val endTime: String
)