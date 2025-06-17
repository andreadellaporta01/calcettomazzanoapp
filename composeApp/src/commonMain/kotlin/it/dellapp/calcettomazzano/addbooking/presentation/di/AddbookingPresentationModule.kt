package it.dellapp.calcettomazzano.addbooking.presentation.di

import it.dellapp.calcettomazzano.addbooking.presentation.AddbookingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val addbookingPresentationModule = module {
    viewModel { AddbookingViewModel(get(), get()) }
}