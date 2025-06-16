package it.dellapp.calcettomazzano.bookings.data.di

import it.dellapp.calcettomazzano.bookings.data.repository.DefaultBookingsRepository
import it.dellapp.calcettomazzano.bookings.domain.repository.BookingsRepository
import org.koin.dsl.module

val bookingsDataModule = module {
    factory<BookingsRepository> { DefaultBookingsRepository() }
}