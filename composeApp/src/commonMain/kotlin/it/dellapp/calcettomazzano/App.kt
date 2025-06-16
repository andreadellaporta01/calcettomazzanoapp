package it.dellapp.calcettomazzano

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.dellapp.calcettomazzano.api.NetworkModule
import it.dellapp.calcettomazzano.bookings.data.di.bookingsDataModule
import it.dellapp.calcettomazzano.bookings.domain.di.bookingsDomainModule
import it.dellapp.calcettomazzano.bookings.presentation.BookingsRoot
import it.dellapp.calcettomazzano.bookings.presentation.BookingsRoute
import it.dellapp.calcettomazzano.bookings.presentation.di.bookingsPresentationModule
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
        composable<BookingsRoute> { BookingsRoot(onEvent = {}) }
    }
}

fun initKoin(config: KoinAppDeclaration? = null) {
    NetworkModule.initialize()
    startKoin {
        config?.invoke(this)
        modules(
            bookingsDataModule,
            bookingsDomainModule,
            bookingsPresentationModule
        )
    }
}