package it.dellapp.calcettomazzano.features.addbooking.domain.model

/**
 * Rappresenta il modello di dominio principale per la feature Addbooking.
 * Questa è la classe "pulita" che viene utilizzata all'interno dell'app (domain, presentation).
 *
 * @property id L'identificatore univoco del modello.
 * @property data Un campo dati di esempio per il modello.
 */
data class Addbooking(
    val id: String,
    val data: String
)