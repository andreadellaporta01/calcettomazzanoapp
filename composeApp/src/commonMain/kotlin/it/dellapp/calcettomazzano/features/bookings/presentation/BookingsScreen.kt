package it.dellapp.calcettomazzano.features.bookings.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import it.dellapp.calcettomazzano.features.bookings.domain.model.Booking
import it.dellapp.calcettomazzano.formatLocalDate
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookingsScreen(
    state: BookingsState,
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
                contentColor = Color.White,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 12.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Aggiungi prenotazione")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Aggiungi prenotazione",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else if (state.error != null) {
                Text("Errore: ${state.error}")
            } else {
                BookingsContent(
                    bookings = state.bookings,
                    innerPadding = innerPadding,
                    onAction = onAction,
                    isLoading = state.isLoading,
                    selectedDate = selectedDate
                )
            }
        }
    }

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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingsContent(
    bookings: List<Booking>,
    innerPadding: PaddingValues,
    onAction: (BookingsAction) -> Unit,
    isLoading: Boolean,
    selectedDate: LocalDate
) {
    // Stato per la bottom sheet
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedBooking by remember { mutableStateOf<Booking?>(null) }
    var inputEmail by remember { mutableStateOf("") }
    var inputCode by remember { mutableStateOf("") }

    // Sheet con Material3
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Funzione di reset campi
    fun resetSheetFields() {
        inputEmail = ""
        inputCode = ""
    }

    // Sheet dialog
    if (showBottomSheet && selectedBooking != null) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
                resetSheetFields()
            },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Cancella Prenotazione",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF388E3C),
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                Text(
                    text = "Per cancellare la prenotazione inserisci email e telefono dichiarati durante la prenotazione e comparirà il tasto cancella",
                    color = Color.DarkGray,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 18.dp)
                )
                // Email
                OutlinedTextField(
                    value = inputEmail,
                    onValueChange = { inputEmail = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(10.dp))
                // Telefono
                OutlinedTextField(
                    value = inputCode,
                    onValueChange = { inputCode = it },
                    label = { Text("Codice Prenotazione di 4 cifre") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(18.dp))
                // Mostra il pulsante solo se i dati sono corretti
                val emailMatch = inputEmail.trim() == (selectedBooking?.email?.trim() ?: "")
                val phoneMatch = inputCode.trim() == (selectedBooking?.code?.trim() ?: "")
                if (emailMatch && phoneMatch) {
                    Button(
                        onClick = {
                            // Chiudi sheet e invia azione
                            showBottomSheet = false
                            resetSheetFields()
                            onAction(
                                BookingsAction.DeleteBooking(
                                    bookingId = selectedBooking!!.id,
                                    date = selectedDate
                                )
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF388E3C),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cancella Prenotazione")
                    }
                }
            }
        }
    }
    PullToRefreshBox(
        isRefreshing = isLoading,
        onRefresh = { onAction(BookingsAction.Refresh(selectedDate)) },
        modifier = Modifier.fillMaxSize().padding(innerPadding)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp)
        ) {
            if (bookings.isEmpty()) {
                item {
                    Box(
                        Modifier
                            .fillParentMaxHeight()
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Nessuna prenotazione")
                    }
                }
            } else {
                item {
                    Text(
                        text = "Prenotazioni effettuate",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF388E3C),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp, start = 24.dp, end = 24.dp, bottom = 12.dp)
                    )
                }
                items(bookings) { booking ->
                    BookingCard(
                        booking = booking,
                        onClick = {
                            selectedBooking = booking
                            showBottomSheet = true
                        }
                    )
                }
            }
        }
    }
}

// Modifica BookingCard per ricevere onClick:
@Composable
fun BookingCard(booking: Booking, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable { onClick() },
        border = BorderStroke(2.dp, Color(0xFF388E3C)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            val slot = if (booking.startTime.isNotBlank() && booking.endTime.isNotBlank())
                "${booking.startTime} - ${booking.endTime}"
            else "Orario non specificato"
            Text(
                text = slot,
                color = Color(0xFF388E3C),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${booking.firstName} ${booking.lastName}".trim(),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
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

    // OutlinedButton con icona calendario e testo chiaro e grande
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(vertical = 12.dp, horizontal = 18.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.White,
                contentColor = Color(0xFF388E3C)
            ),
            border = BorderStroke(2.dp, Color(0xFF388E3C))
        ) {
            Icon(
                imageVector = Icons.Filled.DateRange,
                contentDescription = "Seleziona data",
                tint = Color(0xFF388E3C),
                modifier = Modifier.padding(end = 10.dp)
            )
            Text(
                text = "Seleziona data: ${formatLocalDate(selectedDate)}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF388E3C)
            )
        }
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
                startTime = "18:00",
                endTime = "19:00",
                firstName = "Andrea",
                lastName = "Rossi",
                date = today.toString(),
                email = "william.henry.harrison@example-pet-store.com",
                code = "1234",
                id = 1
            ),
            Booking(
                startTime = "19:00",
                endTime = "20:00",
                firstName = "Luca",
                lastName = "Bianchi",
                date = today.toString(),
                email = "james.s.sherman@example-pet-store.com",
                code = "1234",
                id = 2
            ),
        ),
        innerPadding = PaddingValues(),
        onAction = {},
        isLoading = false,
        selectedDate = today
    )
}