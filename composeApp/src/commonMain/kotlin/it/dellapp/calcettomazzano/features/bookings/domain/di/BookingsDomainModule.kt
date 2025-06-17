package it.dellapp.calcettomazzano.features.bookings.domain.di

import it.dellapp.calcettomazzano.features.addbooking.domain.usecase.GetFreeSlotsDataUseCase
import it.dellapp.calcettomazzano.features.bookings.domain.usecase.GetBookingsDataUseCase
import org.koin.dsl.module

val bookingsDomainModule = module {
    factory { GetBookingsDataUseCase(get()) }
    factory { GetFreeSlotsDataUseCase(get()) }
}