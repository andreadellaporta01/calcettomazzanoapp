package it.dellapp.calcettomazzano

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.dellapp.calcettomazzano.features.addbooking.data.di.addbookingDataModule
import it.dellapp.calcettomazzano.features.addbooking.domain.di.addbookingDomainModule
import it.dellapp.calcettomazzano.features.addbooking.presentation.AddbookingRoot
import it.dellapp.calcettomazzano.features.addbooking.presentation.addBookingRoute
import it.dellapp.calcettomazzano.features.addbooking.presentation.di.addbookingPresentationModule
import it.dellapp.calcettomazzano.common.api.NetworkModule
import it.dellapp.calcettomazzano.features.addbooking.presentation.AddbookingEvent
import it.dellapp.calcettomazzano.features.bookings.data.di.bookingsDataModule
import it.dellapp.calcettomazzano.features.bookings.domain.di.bookingsDomainModule
import it.dellapp.calcettomazzano.features.bookings.presentation.BookingsEvent
import it.dellapp.calcettomazzano.features.bookings.presentation.BookingsRoot
import it.dellapp.calcettomazzano.features.bookings.presentation.BookingsRoute
import it.dellapp.calcettomazzano.features.bookings.presentation.di.bookingsPresentationModule
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

@Composable
@Preview
fun App() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = BookingsRoute
    ) {
        composable<BookingsRoute> {
            BookingsRoot(onEvent = { event ->
                when (event) {
                    is BookingsEvent.NavigateToAddBooking -> navController.navigate(
                        "$addBookingRoute/${event.date}"
                    )

                    else -> {}
                }
            })
        }
        composable(
            "$addBookingRoute/{date}",
        ) { backStackEntry ->
            AddbookingRoot(
                onEvent = { event ->
                    when (event) {
                        is AddbookingEvent.NavigateBack -> navController.popBackStack()
                        is AddbookingEvent.AddBookingSuccess -> {
                            navController.popBackStack()
                        }
                        else -> {}
                    }
                },
                date = backStackEntry.savedStateHandle.get<String>("date").orEmpty()
            )
        }
    }
}

fun initKoin(config: KoinAppDeclaration? = null) {
    NetworkModule.initialize()
    startKoin {
        config?.invoke(this)
        modules(
            bookingsDataModule,
            bookingsDomainModule,
            bookingsPresentationModule,
            addbookingDataModule,
            addbookingDomainModule,
            addbookingPresentationModule,
        )
    }
}