package it.dellapp.calcettomazzano.features.bookings.presentation.di

import it.dellapp.calcettomazzano.features.bookings.presentation.BookingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val bookingsPresentationModule = module {
    viewModel { BookingsViewModel(get(), get()) }
}