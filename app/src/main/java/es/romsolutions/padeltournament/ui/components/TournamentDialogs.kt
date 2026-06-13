package es.romsolutions.padeltournament.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import es.romsolutions.padeltournament.data.model.Tournament
import es.romsolutions.padeltournament.ui.viewmodel.PlayerViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTournamentDialog(
    playerViewModel: PlayerViewModel,
    initialPlayerIds: List<Int> = emptyList(),
    onDismiss: () -> Unit,
    onSave: (Tournament, List<Int>) -> Unit
) {
    val players by playerViewModel.allPlayers.collectAsState()
    var name by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("AMERICANA") }
    var scoreType by remember { mutableStateOf("TIME") }
    var numSets by remember { mutableIntStateOf(1) }
    var matchDuration by remember { mutableIntStateOf(15) }
    var manualDuration by remember { mutableStateOf("") }
    var selectedPlayerIds by remember { mutableStateOf(initialPlayerIds.toSet()) }
    var courts by remember { mutableStateOf("1") }
    
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }
    
    var showTimePicker by remember { mutableStateOf(false) }
    var startHour by remember { mutableIntStateOf(9) }
    var startMinute by remember { mutableIntStateOf(0) }
    val timePickerState = rememberTimePickerState(initialHour = startHour, initialMinute = startMinute)
    
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    if (showDatePicker) {
        DatePickerDialog(onDismissRequest = { showDatePicker = false }, confirmButton = {
            TextButton(onClick = { selectedDate = datePickerState.selectedDateMillis ?: System.currentTimeMillis(); showDatePicker = false }) { Text("OK") }
        }) { DatePicker(state = datePickerState) }
    }
    if (showTimePicker) {
        Dialog(onDismissRequest = { showTimePicker = false }) {
            Card {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    TimePicker(state = timePickerState)
                    Row {
                        TextButton(onClick = { showTimePicker = false }) { Text("Cancelar") }
                        TextButton(onClick = { 
                            startHour = timePickerState.hour
                            startMinute = timePickerState.minute
                            showTimePicker = false 
                        }) { Text("OK") }
                    }
                }
            }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.fillMaxWidth().padding(16.dp), shape = MaterialTheme.shapes.medium) {
            Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Nuevo Torneo", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre del Torneo") }, modifier = Modifier.fillMaxWidth())
                
                Text("Tipo de Torneo", style = MaterialTheme.typography.titleSmall)
                Column {
                    listOf("AMERICANA", "EXPRESS").forEach { type ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { selectedType = type }) {
                            RadioButton(selected = selectedType == type, onClick = { selectedType = type })
                            Text(text = type)
                        }
                    }
                }

                if (selectedType == "AMERICANA") {
                    Text("Sistema de Puntuación", style = MaterialTheme.typography.titleSmall)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { scoreType = "TIME" }) {
                            RadioButton(selected = scoreType == "TIME", onClick = { scoreType = "TIME" }); Text("Tiempo")
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { scoreType = "SETS" }) {
                            RadioButton(selected = scoreType == "SETS", onClick = { scoreType = "SETS" }); Text("Sets")
                        }
                    }
                    if (scoreType == "TIME") {
                        Text("Duración de partidos", style = MaterialTheme.typography.titleSmall)
                        Column {
                            listOf(10, 15, 20).forEach { m ->
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { matchDuration = m; manualDuration = "" }) {
                                    RadioButton(selected = matchDuration == m && manualDuration.isEmpty(), onClick = { matchDuration = m; manualDuration = "" })
                                    Text("$m min")
                                }
                            }
                        }
                        OutlinedTextField(
                            value = manualDuration, 
                            onValueChange = { if(it.all{c->c.isDigit()}) { manualDuration=it; matchDuration=it.toIntOrNull()?:matchDuration } }, 
                            label = { Text("Otro (min)") }, 
                            modifier = Modifier.fillMaxWidth(), 
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    } else {
                        Text("Número de Sets", style = MaterialTheme.typography.titleSmall)
                        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                            listOf(1, 2).forEach { s ->
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { numSets = s }) {
                                    RadioButton(selected = numSets == s, onClick = { numSets = s }); Text("$s Set${if(s>1) "s" else ""}")
                                }
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = sdf.format(Date(selectedDate)), 
                    onValueChange = {}, 
                    label = { Text("Fecha") }, 
                    readOnly = true, 
                    trailingIcon = { Icon(Icons.Default.DateRange, null, modifier = Modifier.clickable { showDatePicker = true }) }, 
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = String.format("%02d:%02d", startHour, startMinute), 
                    onValueChange = {}, 
                    label = { Text("Hora de Inicio") }, 
                    readOnly = true, 
                    trailingIcon = { Icon(Icons.Default.Settings, null, modifier = Modifier.clickable { showTimePicker = true }) }, 
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Pistas Disponibles", style = MaterialTheme.typography.titleSmall)
                OutlinedTextField(value = courts, onValueChange = { if(it.all{c->c.isDigit()}) courts=it }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                
                Text("Jugadores (${selectedPlayerIds.size})", style = MaterialTheme.typography.titleSmall)
                Column(modifier = Modifier.height(150.dp).verticalScroll(rememberScrollState())) {
                    players.forEach { p ->
                        Row(modifier = Modifier.fillMaxWidth().clickable { selectedPlayerIds = if(selectedPlayerIds.contains(p.id)) selectedPlayerIds - p.id else selectedPlayerIds + p.id }, verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = selectedPlayerIds.contains(p.id), onCheckedChange = null)
                            Text(p.nombre, modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancelar") }
                    Button(onClick = {
                        val cal = Calendar.getInstance().apply { timeInMillis = selectedDate; set(Calendar.HOUR_OF_DAY, startHour); set(Calendar.MINUTE, startMinute) }
                        onSave(
                            Tournament(
                                name = name, type = selectedType, scoreType = scoreType, numSets = numSets, 
                                matchDuration = if(scoreType=="TIME") matchDuration else 0, dateTour = cal.timeInMillis, 
                                numberPlayers = selectedPlayerIds.size, numberCourts = courts.toIntOrNull()?:1
                            ), 
                            selectedPlayerIds.toList()
                        )
                    }, enabled = name.isNotBlank() && selectedPlayerIds.size >= 4) { Text("Guardar") }
                }
            }
        }
    }
}
