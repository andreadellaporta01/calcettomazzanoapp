package it.dellapp.calcettomazzano.addbooking.data.di

import it.dellapp.calcettomazzano.addbooking.data.repository.DefaultAddbookingRepository
import it.dellapp.calcettomazzano.addbooking.domain.repository.AddbookingRepository
import org.koin.dsl.module

val addbookingDataModule = module {
    factory<AddbookingRepository> { DefaultAddbookingRepository() }
}