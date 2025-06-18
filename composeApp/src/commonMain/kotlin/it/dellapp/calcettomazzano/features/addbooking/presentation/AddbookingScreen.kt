package it.dellapp.calcettomazzano.features.addbooking.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import it.dellapp.calcettomazzano.features.addbooking.data.model.AddbookingDto
import it.dellapp.calcettomazzano.features.addbooking.domain.model.FreeSlot
import it.dellapp.calcettomazzano.formatLocalDateString
import it.dellapp.calcettomazzano.isValidEmail
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddbookingRoot(
    viewModel: AddbookingViewModel = koinViewModel(),
    onEvent: (AddbookingEvent) -> Unit,
    date: String,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(date) {
        viewModel.onAction(AddbookingAction.GetFreeSlots(date = date))
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            if (event is AddbookingEvent.AddBookingSuccess) {
                scope.launch {
                    val result = snackbarHostState.showSnackbar(
                        "Prenotazione effettuata con successo!",
                    )
                    if (result == SnackbarResult.Dismissed) {
                        onEvent(event)
                    }
                }
            } else {
                onEvent(event)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        AddbookingScreen(
            state = state,
            onAction = viewModel::onAction,
            selectedDate = date,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddbookingScreen(
    state: AddbookingState,
    onAction: (AddbookingAction) -> Unit,
    selectedDate: String,
) {
    // Colori custom
    val green = Color(0xFF388E3C)
    val greenLight = Color(0xFF66BB6A)
    val fieldShape = RoundedCornerShape(12.dp)
    val textStyle = TextStyle(color = green, fontWeight = FontWeight.SemiBold)

    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
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

    // Controllo sulle 4 cifre del codice di prenotazione
    val isCodeValid = code.length == 4 && code.all { it.isDigit() }
    // Validatore email
    val emailIsValid = isValidEmail(email)

    Scaffold(
        topBar = {
            Column {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(green)
                        .statusBarsPadding()
                        .padding(vertical = 18.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Calcetto Mazzano",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Titolo più carino
            Text(
                "Nuova prenotazione",
                color = green,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 26.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
            // Data formattata
            Text(
                text = "per il giorno ${formatLocalDateString(selectedDate)}",
                color = greenLight,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            if (state.isLoading) {
                Spacer(modifier = Modifier.height(40.dp))
                CircularProgressIndicator(color = green)
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
                    Text("Nessuno slot libero per questa data", color = green)
                }
            } else {
                // Scelta slot con menu a tendina, stile colorato (ma senza colors custom)
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedSlotText,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Scegli l'orario", color = green) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        textStyle = textStyle,
                        shape = fieldShape
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        slotOptions.forEachIndexed { index, text ->
                            DropdownMenuItem(
                                text = { Text(text, color = green) },
                                onClick = {
                                    expanded = false
                                    val slot = state.freeSlots[index]
                                    selectedSlotStart = slot.startTime
                                    selectedSlotEnd = slot.endTime
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                modifier = Modifier.background(
                                    if (selectedIndex == index) greenLight.copy(alpha = 0.2f) else Color.Transparent
                                )
                            )
                        }
                    }
                }

                // Form dati utente
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome", color = green) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    textStyle = textStyle,
                    shape = fieldShape
                )
                OutlinedTextField(
                    value = surname,
                    onValueChange = { surname = it },
                    label = { Text("Cognome", color = green) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    textStyle = textStyle,
                    shape = fieldShape
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email", color = green) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = textStyle,
                    shape = fieldShape,
                    isError = email.isNotEmpty() && !emailIsValid
                )
                // Messaggio di errore email
                if (email.isNotEmpty() && !emailIsValid) {
                    Text(
                        text = "Inserisci una mail valida",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 2.dp, bottom = 8.dp)
                    )
                }

                OutlinedTextField(
                    value = code,
                    onValueChange = { input ->
                        // Consenti solo cifre e max 4 caratteri
                        if (input.length <= 4 && input.all { it.isDigit() }) {
                            code = input
                        }
                    },
                    label = { Text("Codice di prenotazione", color = green) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = textStyle,
                    shape = fieldShape,
                    isError = code.isNotEmpty() && !isCodeValid
                )
                // Spiegazione sotto il campo codice prenotazione
                Text(
                    text = "Scegli un codice di 4 cifre che ti servirà per poter cancellare la prenotazione.",
                    color = if (isCodeValid || code.isEmpty()) Color.Gray else MaterialTheme.colorScheme.error,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))
                // Azioni
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(
                        onClick = { onAction.invoke(AddbookingAction.Cancel()) },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = green
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Annulla", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                    Button(
                        onClick = {
                            onAction.invoke(
                                AddbookingAction.AddBooking(
                                    AddbookingDto(
                                        firstName = name,
                                        lastName = surname,
                                        code = code,
                                        email = email,
                                        date = selectedDate,
                                        startTime = selectedSlotStart!!,
                                        endTime = selectedSlotEnd!!,
                                        fieldId = 1,
                                    )
                                )
                            )
                        },
                        enabled = !state.isLoading
                                && name.isNotBlank()
                                && isCodeValid
                                && email.isNotBlank()
                                && emailIsValid
                                && selectedSlotStart != null
                                && selectedSlotEnd != null,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = green,
                            contentColor = Color.White,
                            disabledContainerColor = greenLight.copy(alpha = 0.4f),
                            disabledContentColor = Color.White.copy(alpha = 0.7f)
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Prenota", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAddbookingScreen() {
    AddbookingScreen(
        state = AddbookingState(
            isLoading = false, freeSlots = listOf(
                FreeSlot("09:00", "10:00"),
                FreeSlot("09:30", "10:30"),
                FreeSlot("10:00", "11:00"),
            )
        ),
        onAction = {},
        selectedDate = "2025-05-12"
    )
}