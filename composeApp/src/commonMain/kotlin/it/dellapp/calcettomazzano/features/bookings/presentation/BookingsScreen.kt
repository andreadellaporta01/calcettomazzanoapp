package it.dellapp.calcettomazzano.features.bookings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import it.dellapp.calcettomazzano.features.bookings.domain.model.Booking
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BookingsRoot(
    viewModel: BookingsViewModel = koinViewModel(),
    onEvent: (BookingsEvent) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val today =
        remember { Clock.System.now().toLocalDateTime(TimeZone.of("Europe/Rome")).date }
    var selectedDate by remember { mutableStateOf(today) }

    LaunchedEffect(selectedDate) {
        viewModel.onAction(BookingsAction.DateChanged(selectedDate))
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            onEvent(event)
        }
    }

    BookingsScreen(
        state = state,
        selectedDate = selectedDate,
        onDateChange = { selectedDate = it },
        onAction = viewModel::onAction
    )
}

@Composable
private fun BookingsScreen(
    state: BookingsState,
    selectedDate: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    onAction: (BookingsAction) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (state.isLoading) {
            CircularProgressIndicator()
        } else if (state.error != null) {
            Text("Errore: ${state.error}")
        } else {
            BookingsContent(
                bookings = state.bookings,
                selectedDate = selectedDate,
                onDateChange = onDateChange,
                onAction = onAction
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingsContent(
    bookings: List<Booking>,
    selectedDate: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    onAction: (BookingsAction) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val today = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
    val todayMillis = today.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
    val dateState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds(),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= todayMillis
            }
        }
    )
    Scaffold(
        topBar = {
            Column {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF388E3C))
                        .statusBarsPadding()
                        .padding(vertical = 18.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Calcetto Mazzano",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                DatePickerSelector(
                    selectedDate = selectedDate,
                    onDateChange = onDateChange
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showDialog = true
                },
                containerColor = Color(0xFF388E3C),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Aggiungi prenotazione")
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (showDialog) {
                DatePickerDialog(
                    onDismissRequest = { showDialog = false },
                    confirmButton = {
                        TextButton(onClick = {
                            showDialog = false
                            val pickedDate =
                                Instant.fromEpochMilliseconds(dateState.selectedDateMillis ?: 0)
                                    .toLocalDateTime(TimeZone.UTC)
                                    .date
                            onAction.invoke(BookingsAction.AddBookingDateSelected(pickedDate))
                        }) {
                            Text("OK")
                        }
                    }
                ) {
                    DatePicker(
                        state = dateState,
                        showModeToggle = true,
                    )
                }
            }
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
                                Text(
                                    "${booking.firstName} ${booking.lastName}",
                                    style = MaterialTheme.typography.headlineLarge
                                )
                                Text(booking.date, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerSelector(
    selectedDate: LocalDate,
    onDateChange: (LocalDate) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val today = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
    val todayMillis = today.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
    val dateState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds(),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= todayMillis
            }
        }
    )

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    val pickedDate =
                        Instant.fromEpochMilliseconds(dateState.selectedDateMillis ?: 0)
                            .toLocalDateTime(TimeZone.UTC)
                            .date
                    onDateChange(pickedDate)
                }) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(
                state = dateState,
                showModeToggle = true,
            )
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = selectedDate.toString(),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .clickable { showDialog = true }
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(vertical = 8.dp, horizontal = 16.dp)
        )
    }
}

@Preview
@Composable
private fun PreviewBookingsScreen() {
    // Mock per preview
    val today = Clock.System.now().toLocalDateTime(TimeZone.of("Europe/Rome")).date
    BookingsContent(
        bookings = listOf(
            Booking(
                startTime = "",
                endTime = "",
                firstName = "",
                lastName = "",
                date = today.toString()
            ),
            Booking(
                startTime = "",
                endTime = "",
                firstName = "",
                lastName = "",
                date = today.toString()
            ),
        ),
        selectedDate = today,
        onDateChange = {},
        onAction = {}
    )
}