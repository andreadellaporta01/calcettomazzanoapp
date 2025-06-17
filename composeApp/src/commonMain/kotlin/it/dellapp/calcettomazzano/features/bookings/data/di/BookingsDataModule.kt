package it.dellapp.calcettomazzano.features.bookings.data.di

import it.dellapp.calcettomazzano.features.bookings.data.repository.DefaultBookingsRepository
import it.dellapp.calcettomazzano.features.bookings.domain.repository.BookingsRepository
import org.koin.dsl.module

val bookingsDataModule = module {
    factory<BookingsRepository> { DefaultBookingsRepository() }
}