package it.dellapp.calcettomazzano.bookings.domain.di

import it.dellapp.calcettomazzano.bookings.domain.usecase.GetBookingsDataUseCase
import org.koin.dsl.module

val bookingsDomainModule = module {
    factory { GetBookingsDataUseCase(get()) }
}