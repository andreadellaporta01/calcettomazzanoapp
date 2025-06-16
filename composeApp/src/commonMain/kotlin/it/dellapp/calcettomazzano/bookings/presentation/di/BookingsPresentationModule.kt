package it.dellapp.calcettomazzano.bookings.presentation.di

import it.dellapp.calcettomazzano.bookings.presentation.BookingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val bookingsPresentationModule = module {
    viewModel { BookingsViewModel(get()) }
}