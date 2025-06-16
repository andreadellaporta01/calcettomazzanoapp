package it.dellapp.calcettomazzano.bookings.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import it.dellapp.calcettomazzano.bookings.domain.model.Booking
import it.dellapp.calcettomazzano.bookings.presentation.BookingsAction
import it.dellapp.calcettomazzano.bookings.presentation.BookingsEvent
import it.dellapp.calcettomazzano.bookings.presentation.BookingsState
import it.dellapp.calcettomazzano.bookings.presentation.BookingsViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

/**
 * Entry point composable per la feature Bookings.
 */
@Composable
fun BookingsRoot(
    viewModel: BookingsViewModel = koinViewModel(),
    onEvent: (BookingsEvent) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            onEvent(event)
        }
    }

    BookingsScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

/**
 * Composable "stateless" che disegna la UI per la feature Bookings.
 */
@Composable
private fun BookingsScreen(
    state: BookingsState,
    onAction: (BookingsAction) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (state.isLoading) {
            CircularProgressIndicator()
        } else if (state.error != null) {

        } else {
            BookingsScreen(state.bookings, onAction)
        }
    }
}

@Composable
fun BookingsScreen(
    bookings: List<Booking>,
    onAction: (BookingsAction) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { onAction(BookingsAction.AddBookingClicked()) }) {
                Icon(Icons.Default.Add, contentDescription = "Aggiungi prenotazione")
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (bookings.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nessuna prenotazione")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp)
                ) {
                    items(bookings) { booking ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shadowElevation = 2.dp
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text(booking.userName, style = MaterialTheme.typography.headlineLarge)
                                Text(booking.date, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview()
@Composable
private fun PreviewBookingsScreen() {
    BookingsScreen(
        state = BookingsState(isLoading = false),
        onAction = {}
    )
}