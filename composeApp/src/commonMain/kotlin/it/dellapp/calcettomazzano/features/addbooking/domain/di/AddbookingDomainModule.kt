package it.dellapp.calcettomazzano.features.addbooking.domain.di

import it.dellapp.calcettomazzano.features.addbooking.domain.usecase.GetAddbookingDataUseCase
import org.koin.dsl.module

val addbookingDomainModule = module {
    factory { GetAddbookingDataUseCase(get()) }
}