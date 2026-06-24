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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import es.romsolutions.padeltournament.data.model.League
import es.romsolutions.padeltournament.data.model.Player
import es.romsolutions.padeltournament.data.model.TeamInput
import es.romsolutions.padeltournament.ui.viewmodel.LeagueViewModel
import es.romsolutions.padeltournament.ui.viewmodel.PlayerViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLeagueDialog(
    playerViewModel: PlayerViewModel,
    authManager: es.romsolutions.padeltournament.auth.AuthManager? = null,
    initialPlayerIds: List<Int> = emptyList(),
    onDismiss: () -> Unit,
    onSave: (League, List<Int>) -> Unit
) {
    val players by playerViewModel.allPlayers.collectAsState()
    var name by remember { mutableStateOf("") }
    var weeklyMatches by remember { mutableIntStateOf(1) }
    var courts by remember { mutableStateOf("1") }
    var selectedPlayerIds by remember { mutableStateOf(initialPlayerIds.toSet()) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    if (showDatePicker) {
        DatePickerDialog(onDismissRequest = { showDatePicker = false }, confirmButton = {
            TextButton(onClick = { selectedDate = datePickerState.selectedDateMillis ?: System.currentTimeMillis(); showDatePicker = false }) { Text("OK") }
        }) { DatePicker(state = datePickerState) }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.fillMaxWidth().padding(16.dp), shape = MaterialTheme.shapes.medium) {
            Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Nueva Liga", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre de la Liga") }, modifier = Modifier.fillMaxWidth())
                
                Text("Partidos semanales", style = MaterialTheme.typography.titleSmall)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    listOf(1, 2, 3).forEach { num ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = weeklyMatches == num, onClick = { weeklyMatches = num })
                            Text(text = num.toString())
                        }
                    }
                }
                
                OutlinedTextField(
                    value = sdf.format(Date(selectedDate)), 
                    onValueChange = {}, 
                    label = { Text("Fecha de Inicio") }, 
                    readOnly = true, 
                    trailingIcon = { Icon(Icons.Default.DateRange, "Calendario", modifier = Modifier.clickable { showDatePicker = true }) }, 
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Pistas Disponibles", style = MaterialTheme.typography.titleSmall)
                OutlinedTextField(
                    value = courts, 
                    onValueChange = { if(it.all{c->c.isDigit()}) courts=it }, 
                    modifier = Modifier.fillMaxWidth(), 
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Text("Jugadores (${selectedPlayerIds.size})", style = MaterialTheme.typography.titleSmall)
                if (selectedPlayerIds.size % 2 != 0) Text(text = "Debe ser un número par", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                
                Column(modifier = Modifier.height(200.dp).verticalScroll(rememberScrollState())) {
                    players.forEach { player ->
                        Row(modifier = Modifier.fillMaxWidth().clickable { selectedPlayerIds = if (selectedPlayerIds.contains(player.id)) selectedPlayerIds - player.id else selectedPlayerIds + player.id }, verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = selectedPlayerIds.contains(player.id), onCheckedChange = null)
                            Text(text = player.nombre, modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                }
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancelar") }
                    Button(
                        onClick = {
                            val numTeams = selectedPlayerIds.size / 2
                            val totalMatches = if (numTeams > 1) (numTeams * (numTeams - 1)) / 2 else 0
                            val weeks = if (weeklyMatches > 0) Math.ceil(totalMatches.toDouble() / weeklyMatches).toLong() else 0
                            val endDate = selectedDate + (weeks * 7 * 24 * 60 * 60 * 1000)
                            onSave(
                                League(
                                    name = name, 
                                    size = numTeams, 
                                    weeklyMatches = weeklyMatches, 
                                    startDate = selectedDate, 
                                    endDate = endDate, 
                                    isTeamBased = true,
                                    numberCourts = courts.toIntOrNull() ?: 1,
                                    adminId = authManager?.getCurrentUserId()
                                ), 
                                selectedPlayerIds.toList()
                            )
                        }, 
                        enabled = name.isNotBlank() && selectedPlayerIds.size >= 4 && selectedPlayerIds.size % 2 == 0
                    ) { Text("Guardar") }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartLeagueDialog(league: League, onDismiss: () -> Unit, onConfirm: (List<Int>, Int, Int, Int) -> Unit) {
    val days = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
    var selected by remember { mutableStateOf(setOf<Int>()) }
    var courts by remember { mutableStateOf(league.numberCourts.toString()) }
    var startHour by remember { mutableIntStateOf(18) }
    var startMinute by remember { mutableIntStateOf(0) }
    var showTimePicker by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState(initialHour = startHour, initialMinute = startMinute)

    if (showTimePicker) {
        Dialog(onDismissRequest = { showTimePicker = false }) {
            Card {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    TimePicker(state = timePickerState)
                    Row {
                        TextButton(onClick = { showTimePicker = false }) { Text("Cancelar") }
                        TextButton(onClick = { startHour = timePickerState.hour; startMinute = timePickerState.minute; showTimePicker = false }) { Text("OK") }
                    }
                }
            }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Configurar Inicio", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                Text("Días de juego (${league.weeklyMatches}):")
                days.forEachIndexed { i, d ->
                    val n = i + 1
                    Row(modifier = Modifier.fillMaxWidth().clickable { if(selected.contains(n)) selected -= n else if(selected.size < league.weeklyMatches) selected += n }, verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = selected.contains(n), onCheckedChange = null, enabled = selected.contains(n) || selected.size < league.weeklyMatches)
                        Text(d, modifier = Modifier.padding(start = 8.dp))
                    }
                }
                
                Text("Configuración de Pistas", style = MaterialTheme.typography.titleSmall)
                OutlinedTextField(value = courts, onValueChange = { if(it.all{c->c.isDigit()}) courts=it }, label = { Text("Número de Pistas") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                
                OutlinedTextField(
                    value = String.format("%02d:%02d", startHour, startMinute),
                    onValueChange = {},
                    label = { Text("Hora de Comienzo") },
                    readOnly = true,
                    trailingIcon = { Icon(Icons.Default.Settings, null, modifier = Modifier.clickable { showTimePicker = true }) },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancelar") }
                    Button(onClick = { onConfirm(selected.toList(), courts.toIntOrNull()?:1, startHour, startMinute) }, enabled = selected.size == league.weeklyMatches) { Text("Iniciar Liga") }
                }
            }
        }
    }
}

@Composable
fun TeamSetupDialog(league: League, playerViewModel: PlayerViewModel, leagueViewModel: LeagueViewModel, onDismiss: () -> Unit, onConfirm: (List<TeamInput>) -> Unit) {
    val allPlayers by playerViewModel.allPlayers.collectAsState()
    var leaguePlayerIds by remember { mutableStateOf<List<Int>>(emptyList()) }
    LaunchedEffect(league.id) { leaguePlayerIds = leagueViewModel.getPlayersInLeague(league.id) }
    var index by remember { mutableIntStateOf(0) }
    var name by remember { mutableStateOf("Equipo ${index + 1}") }
    var p1 by remember { mutableStateOf<Int?>(null) }
    var p2 by remember { mutableStateOf<Int?>(null) }
    var used by remember { mutableStateOf(setOf<Int>()) }
    val teamsState = remember { mutableStateOf<List<TeamInput>>(emptyList()) }

    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Configurar Equipos (${index + 1}/${league.size})", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                Text("Selecciona 2 jugadores:")
                Column(modifier = Modifier.height(150.dp).verticalScroll(rememberScrollState())) {
                    allPlayers.filter { it.id in leaguePlayerIds && (it.id !in used || it.id == p1 || it.id == p2) }.forEach { p ->
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
                        if (index + 1 < league.size) { index++; name = "Equipo ${index + 1}"; p1 = null; p2 = null } 
                        else { onConfirm(teamsState.value) }
                    }, enabled = name.isNotBlank() && p1 != null && p2 != null) { Text(if(index + 1 < league.size) "Siguiente" else "Finalizar") }
                }
            }
        }
    }
}
