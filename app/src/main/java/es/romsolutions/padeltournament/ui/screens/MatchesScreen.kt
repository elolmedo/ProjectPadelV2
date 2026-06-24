package es.romsolutions.padeltournament.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import es.romsolutions.padeltournament.R
import es.romsolutions.padeltournament.data.model.Match
import es.romsolutions.padeltournament.ui.components.EditMatchScoreDialog
import es.romsolutions.padeltournament.ui.viewmodel.LeagueViewModel
import es.romsolutions.padeltournament.ui.viewmodel.PlayerViewModel
import es.romsolutions.padeltournament.ui.viewmodel.TournamentViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchesScreen(
    leagueViewModel: LeagueViewModel,
    tournamentViewModel: TournamentViewModel,
    playerViewModel: PlayerViewModel,
    initialLeagueId: Int? = null,
    initialTournamentId: Int? = null,
    onBack: () -> Unit
) {
    val matches by leagueViewModel.allMatches.collectAsState()
    val leagues by leagueViewModel.allLeagues.collectAsState()
    val tournaments by tournamentViewModel.allTournaments.collectAsState()
    val players by playerViewModel.allPlayers.collectAsState()
    val teams by leagueViewModel.allTeams.collectAsState()
    
    var filterLeagueId by remember { mutableStateOf(initialLeagueId) }
    var filterTournamentId by remember { mutableStateOf(initialTournamentId) }
    
    var selectedWeek by remember { mutableStateOf<Int?>(null) }
    var selectedRound by remember { mutableStateOf<Int?>(null) }
    
    val currentTournament = filterTournamentId?.let { tid -> tournaments.find { it.id == tid } }
    val isPozo = currentTournament?.type == "POZO"

    // Auto-seleccionar la última ronda para Pozo si no hay una seleccionada
    LaunchedEffect(matches, filterTournamentId) {
        if (isPozo && filterTournamentId != null) {
            val maxRound = matches.filter { it.tournamentId == filterTournamentId }.maxOfOrNull { it.roundNumber }
            if (maxRound != null) {
                selectedRound = maxRound
            }
        }
    }
    
    var editingMatch by remember { mutableStateOf<Match?>(null) }
    
    LaunchedEffect(initialLeagueId, initialTournamentId) {
        filterLeagueId = initialLeagueId
        filterTournamentId = initialTournamentId
    }
    
    var expandedFilter by remember { mutableStateOf(false) }
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    
    val filteredMatches = matches.filter { 
        (filterLeagueId == null || it.leagueId == filterLeagueId) &&
        (filterTournamentId == null || it.tournamentId == filterTournamentId)
    }.filter { m ->
        (selectedWeek == null || m.weekNumber == selectedWeek) &&
        (selectedRound == null || m.roundNumber == selectedRound)
    }.sortedBy { it.playStart }

    Scaffold(
        topBar = {
            Surface(tonalElevation = 4.dp) {
                Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Text(
                        text = stringResource(R.string.app_name), 
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center, 
                        modifier = Modifier.fillMaxWidth(), 
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        TextButton(onClick = onBack) { Text(stringResource(R.string.back)) }
                        
                        ExposedDropdownMenuBox(expanded = expandedFilter, onExpandedChange = { expandedFilter = !expandedFilter }) {
                            val currentName = if(filterLeagueId != null) leagues.find { it.id == filterLeagueId }?.name 
                                             else if(filterTournamentId != null) tournaments.find { it.id == filterTournamentId }?.name
                                             else stringResource(R.string.filter_all)
                            OutlinedTextField(
                                value = currentName ?: stringResource(R.string.filter_all), 
                                onValueChange = {}, readOnly = true, 
                                modifier = Modifier.menuAnchor().width(120.dp),
                                textStyle = MaterialTheme.typography.bodySmall,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFilter) }
                            )
                            ExposedDropdownMenu(expanded = expandedFilter, onDismissRequest = { expandedFilter = false }) {
                                DropdownMenuItem(text = { Text(stringResource(R.string.filter_all)) }, onClick = { filterLeagueId = null; filterTournamentId = null; expandedFilter = false })
                                leagues.filter { it.isStarted }.forEach { l -> DropdownMenuItem(text = { Text("${stringResource(R.string.tab_leagues)}: ${l.name}") }, onClick = { filterLeagueId = l.id; filterTournamentId = null; expandedFilter = false }) }
                                tournaments.filter { it.isStarted }.forEach { t -> DropdownMenuItem(text = { Text("${stringResource(R.string.tab_tournaments)}: ${t.name}") }, onClick = { filterTournamentId = t.id; filterLeagueId = null; expandedFilter = false }) }
                            }
                        }

                        if (filterLeagueId != null || (isPozo && filterTournamentId != null)) {
                            var expR by remember { mutableStateOf(false) }
                            Spacer(Modifier.width(4.dp))
                            ExposedDropdownMenuBox(expanded = expR, onExpandedChange = { expR = !expR }) {
                                OutlinedTextField(
                                    value = if(selectedRound == null) stringResource(R.string.filter_round) else "R$selectedRound", 
                                    onValueChange = {}, readOnly = true, 
                                    modifier = Modifier.menuAnchor().width(80.dp),
                                    textStyle = MaterialTheme.typography.bodySmall
                                )
                                ExposedDropdownMenu(expanded = expR, onDismissRequest = { expR = false }) {
                                    DropdownMenuItem(text = { Text(stringResource(R.string.filter_all)) }, onClick = { selectedRound = null; expR = false })
                                    
                                    // Filtrar rondas ÚNICAMENTE del torneo/liga seleccionado
                                    val availableRounds = matches
                                        .filter { (filterLeagueId != null && it.leagueId == filterLeagueId) || (filterTournamentId != null && it.tournamentId == filterTournamentId) }
                                        .map { it.roundNumber }
                                        .distinct()
                                        .sorted()
                                    
                                    availableRounds.forEach { r -> 
                                        DropdownMenuItem(
                                            text = { Text("${stringResource(R.string.filter_round)} $r") }, 
                                            onClick = { selectedRound = r; expR = false }
                                        ) 
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            val showFinishBtn = if(filterLeagueId != null) leagues.find { it.id == filterLeagueId }?.isFinished != true
                              else if(filterTournamentId != null) tournaments.find { it.id == filterTournamentId }?.isFinished != true
                              else true
            
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                if (filterTournamentId != null && showFinishBtn) {
                    val currentMatchesForTour = matches.filter { it.tournamentId == filterTournamentId }
                    val lastRoundCreated = currentMatchesForTour.maxOfOrNull { it.roundNumber } ?: 1
                    val allFinishedInLastRound = currentMatchesForTour.filter { it.roundNumber == lastRoundCreated }.all { it.playFinish != null }
                    
                    val numPlayers = currentTournament?.numberPlayers ?: 0
                    val isTeamBased = currentTournament?.isTeamBased ?: false
                    
                    val maxRounds = when(currentTournament?.type) {
                        "AMERICANA" -> if (!isTeamBased) numPlayers - 1 else (numPlayers / 2) - 1
                        "POZO" -> if (!isTeamBased) numPlayers - 1 else (numPlayers / 2) - 1
                        "EXPRESS" -> (numPlayers / 4)
                        else -> 1
                    }
                    
                    val isCycleCompleted = lastRoundCreated >= maxRounds

                    Button(
                        onClick = { 
                            if (isCycleCompleted) {
                                tournamentViewModel.finishTournament(currentTournament!!)
                            } else {
                                if (isPozo) {
                                    tournamentViewModel.generateNextPozoRound(filterTournamentId!!) 
                                } else {
                                    selectedRound = lastRoundCreated + 1
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = allFinishedInLastRound,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isCycleCompleted) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        val buttonText = if (isCycleCompleted) "FINALIZAR TORNEO (CICLO COMPLETADO)" 
                                        else if (isPozo) "SIGUIENTE RONDA (ROTAR)"
                                        else "IR A SIGUIENTE RONDA (${lastRoundCreated + 1}/$maxRounds)"
                        Text(buttonText)
                    }
                    Spacer(Modifier.height(8.dp))
                }

                if (filterLeagueId != null && showFinishBtn) {
                    val currentMatchesForLeague = matches.filter { it.leagueId == filterLeagueId }
                    val lastRoundWithResults = currentMatchesForLeague.filter { it.playFinish != null }.maxOfOrNull { it.roundNumber } ?: 0
                    val totalRounds = currentMatchesForLeague.maxOfOrNull { it.roundNumber } ?: 1
                    
                    if (lastRoundWithResults < totalRounds) {
                        Button(
                            onClick = { selectedRound = lastRoundWithResults + 1 },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                        ) {
                            Text("IR A PRÓXIMA JORNADA PENDIENTE (${lastRoundWithResults + 1})")
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                }

                if (showFinishBtn) {
                    Button(
                        onClick = { leagueViewModel.finishMatchesRandomly(filterLeagueId, filterTournamentId) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium
                    ) { 
                        Text(stringResource(R.string.simulate_results)) 
                    }
                }
            }
        }
    ) { pv ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(pv).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (filteredMatches.isEmpty()) { 
                item { Text(text = stringResource(R.string.empty_matches), modifier = Modifier.fillMaxWidth().padding(32.dp), textAlign = TextAlign.Center) }
            }
            items(filteredMatches) { m ->
                val team1 = if (m.player1Id != -1) {
                    teams.find { it.leagueId == m.leagueId && (it.playerOneId == m.player1Id || it.playerOneId == m.player2Id) && (it.playerTwoId == m.player1Id || it.playerTwoId == m.player2Id) }?.nameTeam ?: "${players.find { it.id == m.player1Id }?.nombre?.substringBefore(" ") ?: "?"}/${players.find { it.id == m.player2Id }?.nombre?.substringBefore(" ") ?: ""}"
                } else "Vacío"
                
                val team2 = if (m.player3Id != -1) {
                    teams.find { it.leagueId == m.leagueId && (it.playerOneId == m.player3Id || it.playerOneId == m.player2Id) && (it.playerTwoId == m.player3Id || it.playerTwoId == m.player4Id) }?.nameTeam ?: "${players.find { it.id == m.player3Id }?.nombre?.substringBefore(" ") ?: "?"}/${players.find { it.id == m.player4Id }?.nombre?.substringBefore(" ") ?: ""}"
                } else if (m.courtNumber == 0) "EN RESERVA" else "Vacío"

                Card(
                    modifier = Modifier.fillMaxWidth().clickable { if(m.courtNumber > 0) editingMatch = m },
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if(m.courtNumber == 0) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Ronda ${m.roundNumber}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                            Text(
                                text = if(m.courtNumber == 0) "RESERVA" else "Pista ${m.courtNumber}", 
                                style = MaterialTheme.typography.labelSmall, 
                                fontWeight = FontWeight.Bold, 
                                color = if(m.courtNumber == 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(text = sdf.format(Date(m.playStart ?: 0)), style = MaterialTheme.typography.bodySmall)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = team1, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, maxLines = 1)
                                Text(text = m.scoreTeamOne.toString(), style = MaterialTheme.typography.headlineMedium, color = if(m.scoreTeamOne > m.scoreTeamTwo) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface)
                            }
                            Text("VS", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 8.dp), color = MaterialTheme.colorScheme.secondary)
                            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = team2, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, maxLines = 1)
                                Text(text = m.scoreTeamTwo.toString(), style = MaterialTheme.typography.headlineMedium, color = if(m.scoreTeamTwo > m.scoreTeamOne) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface)
                            }
                        }
                        if (m.playFinish != null) { 
                            Text(text = "FINALIZADO", color = MaterialTheme.colorScheme.secondary, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.ExtraBold) 
                        }
                    }
                }
            }
        }
    }

    if (editingMatch != null) {
        EditMatchScoreDialog(
            match = editingMatch!!, players = players, teams = teams,
            onDismiss = { editingMatch = null },
            onSave = { updated -> leagueViewModel.updateMatch(updated); editingMatch = null }
        )
    }
}
