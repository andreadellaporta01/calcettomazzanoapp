package it.dellapp.calcettomazzano.features.addbooking.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import it.dellapp.calcettomazzano.features.addbooking.data.model.AddbookingDto
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddbookingScreen(
    state: AddbookingState,
    onAction: (AddbookingAction) -> Unit,
    selectedDate: String,
) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var selectedSlotStart by remember { mutableStateOf<String?>(null) }
    var selectedSlotEnd by remember { mutableStateOf<String?>(null) }

    // Dropdown state
    var expanded by remember { mutableStateOf(false) }
    val slotOptions = state.freeSlots.map { "${it.startTime} - ${it.endTime}" }
    val selectedIndex = slotOptions.indexOf(
        if (selectedSlotStart != null && selectedSlotEnd != null)
            "$selectedSlotStart - $selectedSlotEnd"
        else ""
    )
    val selectedSlotText = slotOptions.getOrNull(selectedIndex).orEmpty()

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
    ) { innerPadding ->
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()).padding(innerPadding)
                .padding(20.dp),
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
                // Scelta slot con menu a tendina
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedSlotText,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Slot libero") },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        slotOptions.forEachIndexed { index, text ->
                            DropdownMenuItem(
                                text = { Text(text) },
                                onClick = {
                                    expanded = false
                                    val slot = state.freeSlots[index]
                                    selectedSlotStart = slot.startTime
                                    selectedSlotEnd = slot.endTime
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
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
                                        date = selectedDate,
                                        startTime = selectedSlotStart!!.toString(),
                                        endTime = selectedSlotEnd!!.toString(),
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