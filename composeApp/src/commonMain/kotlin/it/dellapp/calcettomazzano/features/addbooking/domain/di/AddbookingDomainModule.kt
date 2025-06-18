package it.dellapp.calcettomazzano.features.addbooking.domain.di

import it.dellapp.calcettomazzano.features.addbooking.domain.usecase.GetAddbookingDataUseCase
import it.dellapp.calcettomazzano.features.addbooking.domain.usecase.GetFreeSlotsDataUseCase
import org.koin.dsl.module

val addbookingDomainModule = module {
    factory { GetAddbookingDataUseCase(get()) }
    factory { GetFreeSlotsDataUseCase(get()) }
}