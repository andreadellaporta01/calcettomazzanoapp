package it.dellapp.calcettomazzano.addbooking.domain.di

import it.dellapp.calcettomazzano.addbooking.domain.usecase.GetAddbookingDataUseCase
import org.koin.dsl.module

val addbookingDomainModule = module {
    factory { GetAddbookingDataUseCase(get()) }
}