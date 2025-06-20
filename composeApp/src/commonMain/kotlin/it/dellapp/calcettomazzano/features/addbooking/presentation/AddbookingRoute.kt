package it.dellapp.calcettomazzano.features.addbooking.presentation
import kotlinx.serialization.Serializable

/**
 * Definisce la rotta di navigazione per la schermata Addbooking.
 * Utilizzato da una libreria di navigazione type-safe.
 */

@Serializable
data class AddbookingRoute(val date: String)