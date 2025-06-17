package it.dellapp.calcettomazzano.addbooking.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import it.dellapp.calcettomazzano.addbooking.data.model.AddbookingDto
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

/**
 * Entry point composable per la feature Addbooking.
 */
@Composable
fun AddbookingRoot(
    viewModel: AddbookingViewModel = koinViewModel(),
    onEvent: (AddbookingEvent) -> Unit,
    date: String,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(date) {
        viewModel.onAction(AddbookingAction.GetFreeSlots(date = date))
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            onEvent(event)
        }
    }

    AddbookingScreen(
        state = state, onAction = viewModel::onAction, selectedDate = date
    )
}

/**
 * Composable "stateless" che disegna la UI per la feature Addbooking.
 */
@Composable
private fun AddbookingScreen(
    state: AddbookingState,
    onAction: (AddbookingAction) -> Unit,
    selectedDate: String,
) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var selectedSlotStart by remember { mutableStateOf<LocalTime?>(null) }
    var selectedSlotEnd by remember { mutableStateOf<LocalTime?>(null) }

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
            }
        },
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Nuova prenotazione per il giorno $selectedDate",
                style = MaterialTheme.typography.headlineSmall
            )

            if (state.isLoading) {
                CircularProgressIndicator()
            } else if (state.error != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(state.error, color = MaterialTheme.colorScheme.error)
                }
            } else if (state.freeSlots.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Nessuno slot libero per questa data")
                }
            } else {
                // Scelta slot
                Text("Scegli uno slot libero:")
                state.freeSlots.forEach { slot ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedSlotStart == slot.startTime, onClick = {
                                selectedSlotStart = slot.startTime
                                selectedSlotEnd = slot.endTime
                            })
                        Text("${slot.startTime} - ${slot.endTime}")
                    }
                }

                // Form dati utente
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = surname,
                    onValueChange = { surname = it },
                    label = { Text("Cognome") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Numero di telefono") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )

                // Azioni
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = {}) { Text("Annulla") }
                    Button(
                        onClick = {
                            onAction.invoke(
                                AddbookingAction.AddBooking(
                                    AddbookingDto(
                                        firstName = name,
                                        lastName = surname,
                                        phone = phone,
                                        email = email,
                                        date = LocalDate.parse(selectedDate),
                                        startTime = selectedSlotStart!!,
                                        endTime = selectedSlotEnd!!,
                                        fieldId = 1,
                                    )
                                )
                            )
                        },
                        enabled = !state.isLoading && name.isNotBlank() && phone.isNotBlank() && email.isNotBlank() && selectedSlotStart != null && selectedSlotEnd != null
                    ) {
                        Text("Prenota")
                    }
                }
            }
        }
    }
}

@Preview()
@Composable
private fun PreviewAddbookingScreen() {
    AddbookingScreen(
        state = AddbookingState(isLoading = false), onAction = {}, selectedDate = "2025-05-12"
    )
}