package it.dellapp.calcettomazzano.features.addbooking.data.di

import it.dellapp.calcettomazzano.features.addbooking.data.repository.DefaultAddbookingRepository
import it.dellapp.calcettomazzano.features.addbooking.domain.repository.AddbookingRepository
import org.koin.dsl.module

val addbookingDataModule = module {
    factory<AddbookingRepository> { DefaultAddbookingRepository() }
}