package it.dellapp.calcettomazzano

import kotlinx.datetime.LocalDate

fun formatLocalDate(localDate: LocalDate): String {
    val day = localDate.dayOfMonth.toString().padStart(2, '0')
    val month = localDate.monthNumber.toString().padStart(2, '0')
    val year = localDate.year.toString()
    return "$day/$month/$year"
}

// Funzione di formattazione data (senza java.time)
fun formatLocalDateString(dateString: String): String {
    // accetta "YYYY-MM-DD" (es: 2025-06-18)
    val parts = dateString.split("-")
    return if (parts.size == 3) {
        "${parts[2].padStart(2, '0')}/${parts[1].padStart(2, '0')}/${parts[0]}"
    } else {
        dateString
    }
}

// Funzione di validazione email semplice (non perfetta ma sufficiente per UI)
fun isValidEmail(email: String): Boolean {
    // Semplice controllo: almeno un carattere prima e dopo la @, almeno un punto dopo la @
    val parts = email.split("@")
    if (parts.size != 2) return false
    val local = parts[0]
    val domain = parts[1]
    if (local.isBlank() || domain.isBlank()) return false
    if (!domain.contains('.')) return false
    if (domain.startsWith('.') || domain.endsWith('.')) return false
    if (domain.contains("..")) return false
    return true
}