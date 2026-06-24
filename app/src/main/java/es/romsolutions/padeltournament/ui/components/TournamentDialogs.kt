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
import es.romsolutions.padeltournament.data.model.TeamInput
import es.romsolutions.padeltournament.ui.viewmodel.PlayerViewModel
import es.romsolutions.padeltournament.ui.viewmodel.TournamentViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTournamentDialog(
    playerViewModel: PlayerViewModel,
    authManager: es.romsolutions.padeltournament.auth.AuthManager? = null,
    analyticsManager: es.romsolutions.padeltournament.analytics.AnalyticsManager? = null,
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
    var isTeamBased by remember { mutableStateOf(false) }
    var pairByLevel by remember { mutableStateOf(false) }
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
                    listOf("AMERICANA", "EXPRESS", "Rey de la Pista").forEach { type ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { selectedType = if (type == "Rey de la Pista") "POZO" else type }) {
                            RadioButton(selected = (selectedType == type || (selectedType == "POZO" && type == "Rey de la Pista")), onClick = { selectedType = if (type == "Rey de la Pista") "POZO" else type })
                            Text(text = type)
                        }
                    }
                }

                if (selectedType == "AMERICANA" || selectedType == "POZO") {
                    Text("Formato", style = MaterialTheme.typography.titleSmall)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { isTeamBased = false }) {
                            RadioButton(selected = !isTeamBased, onClick = { isTeamBased = false }); Text("Rotación")
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { isTeamBased = true }) {
                            RadioButton(selected = isTeamBased, onClick = { isTeamBased = true }); Text("Parejas Fijas")
                        }
                    }
                    
                    if (isTeamBased) {
                        Spacer(Modifier.height(4.dp))
                        Text("Formato de Parejas Fijas seleccionado.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                    }

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
                val recommendedCourts = (selectedPlayerIds.size / 4).toString()
                OutlinedTextField(
                    value = courts, 
                    onValueChange = { if(it.all{c->c.isDigit()}) courts=it }, 
                    modifier = Modifier.fillMaxWidth(), 
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    supportingText = { 
                        val courtsInt = courts.toIntOrNull() ?: 1
                        val playersOnCourts = courtsInt * 4
                        
                        // Cálculo de rondas sugeridas para todos los tipos
                        val suggestedRounds = if (selectedPlayerIds.size > 0) {
                            when(selectedType) {
                                "AMERICANA" -> if (!isTeamBased) selectedPlayerIds.size - 1 else (selectedPlayerIds.size / 2) - 1
                                "POZO" -> if (!isTeamBased) selectedPlayerIds.size - 1 else (selectedPlayerIds.size / 2) - 1
                                "EXPRESS" -> (selectedPlayerIds.size / 4) // Simplificado para Express
                                else -> 0
                            }
                        } else 0
                        
                        val totalTime = if (scoreType == "TIME") {
                            suggestedRounds * (matchDuration + 5)
                        } else {
                            suggestedRounds * 45 // Estimado para sets
                        }

                        if (selectedType == "POZO") {
                            val reserves = selectedPlayerIds.size - playersOnCourts
                            Text("Recomendado: $recommendedCourts pistas. Rondas: $suggestedRounds (~${totalTime} min). Reservas: $reserves")
                        } else {
                            Text("Rondas estimadas: $suggestedRounds. Tiempo total aprox: ${totalTime} min.")
                        }
                    }
                )
                
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
                        analyticsManager?.logTournamentCreated(selectedType, selectedPlayerIds.size, isTeamBased)
                        onSave(
                            Tournament(
                                name = name, type = selectedType, scoreType = scoreType, numSets = numSets, 
                                matchDuration = if(scoreType=="TIME") matchDuration else 0, dateTour = cal.timeInMillis, 
                                numberPlayers = selectedPlayerIds.size, numberCourts = courts.toIntOrNull()?:1,
                                isTeamBased = isTeamBased,
                                isMixed = pairByLevel, // Usamos isMixed para guardar el flag de nivel
                                adminId = authManager?.getCurrentUserId()
                            ), 
                            selectedPlayerIds.toList()
                        )
                    }, enabled = name.isNotBlank() && selectedPlayerIds.size >= 4 && (!isTeamBased || selectedPlayerIds.size % 2 == 0)) { Text("Guardar") }
                }
            }
        }
    }
}

@Composable
fun TournamentTeamSetupDialog(
    tournament: Tournament, 
    playerViewModel: PlayerViewModel, 
    tournamentViewModel: TournamentViewModel, 
    onDismiss: () -> Unit, 
    onConfirm: (List<TeamInput>) -> Unit
) {
    val allPlayers by playerViewModel.allPlayers.collectAsState()
    var tournamentPlayerIds by remember { mutableStateOf<List<Int>>(emptyList()) }
    LaunchedEffect(tournament.id) { tournamentPlayerIds = tournamentViewModel.getPlayersInTournament(tournament.id) }
    
    val numTeams = tournament.numberPlayers / 2
    var index by remember { mutableIntStateOf(0) }
    var name by remember { mutableStateOf("Pareja ${index + 1}") }
    var p1 by remember { mutableStateOf<Int?>(null) }
    var p2 by remember { mutableStateOf<Int?>(null) }
    var used by remember { mutableStateOf(setOf<Int>()) }
    val teamsState = remember { mutableStateOf<List<TeamInput>>(emptyList()) }
    
    val players by playerViewModel.allPlayers.collectAsState()
    
    // Auto-setup logic if requested
    LaunchedEffect(tournamentPlayerIds) {
        // En una versión futura podríamos automatizar esto aquí o en el ViewModel
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Configurar Parejas (${index + 1}/$numTeams)", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre de la Pareja") }, modifier = Modifier.fillMaxWidth())
                Text("Selecciona 2 jugadores:")
                Column(modifier = Modifier.height(200.dp).verticalScroll(rememberScrollState())) {
                    allPlayers.filter { it.id in tournamentPlayerIds && (it.id !in used || it.id == p1 || it.id == p2) }.forEach { p ->
                        Row(modifier = Modifier.fillMaxWidth().clickable { if(p1==p.id) p1=null else if(p2==p.id) p2=null else if(p1==null) p1=p.id else if(p2==null) p2=p.id }, verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = p1==p.id || p2==p.id, onCheckedChange = null)
                            Text(p.nombre, modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancelar") }
                    Button(onClick = {
                        val t = TeamInput(name, p1!!, p2!!)
                        teamsState.value = teamsState.value + t
                        used += setOf(p1!!, p2!!)
                        if (index + 1 < numTeams) { 
                            index++
                            name = "Pareja ${index + 1}"
                            p1 = null
                            p2 = null 
                        } 
                        else { 
                            onConfirm(teamsState.value) 
                        }
                    }, enabled = name.isNotBlank() && p1 != null && p2 != null) { Text(if(index + 1 < numTeams) "Siguiente" else "Finalizar") }
                }
            }
        }
    }
}
