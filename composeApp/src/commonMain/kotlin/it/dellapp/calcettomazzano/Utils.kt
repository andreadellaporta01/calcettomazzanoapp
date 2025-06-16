package it.dellapp.calcettomazzano

import kotlinx.datetime.LocalDate

fun formatDate(date: LocalDate): String {
    return "${date.dayOfMonth.toString().padStart(2, '0')}/" +
            "${date.monthNumber.toString().padStart(2, '0')}/" +
            "${date.year}"
}