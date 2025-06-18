package it.dellapp.calcettomazzano.features.bookings.domain.di

import it.dellapp.calcettomazzano.features.bookings.domain.usecase.DeleteBookingUseCase
import it.dellapp.calcettomazzano.features.bookings.domain.usecase.GetBookingsDataUseCase
import org.koin.dsl.module

val bookingsDomainModule = module {
    factory { GetBookingsDataUseCase(get()) }
    factory { DeleteBookingUseCase(get()) }
}