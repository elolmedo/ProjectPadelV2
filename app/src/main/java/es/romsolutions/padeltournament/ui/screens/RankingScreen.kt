package es.romsolutions.padeltournament.ui.screens

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
import es.romsolutions.padeltournament.ui.viewmodel.LeagueViewModel
import es.romsolutions.padeltournament.ui.viewmodel.PlayerViewModel
import es.romsolutions.padeltournament.ui.viewmodel.TournamentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RankingListScreen(
    leagueViewModel: LeagueViewModel,
    tournamentViewModel: TournamentViewModel,
    playerViewModel: PlayerViewModel
) {
    val players by playerViewModel.allPlayers.collectAsState()
    val generalRanking by leagueViewModel.generalRanking.collectAsState()
    val leagues by leagueViewModel.allLeagues.collectAsState()
    val tournaments by tournamentViewModel.allTournaments.collectAsState()
    
    var selectedId by remember { mutableStateOf<Int?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }

    val currentRanking = if (selectedId == null) generalRanking 
                         else leagueViewModel.getRankingForLeague(selectedId!!).collectAsState(initial = emptyList()).value

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text(stringResource(R.string.confirm)) },
            text = { Text("¿Deseas reiniciar TODO el ranking general? Se borrarán todos los puntos acumulados.") },
            confirmButton = {
                TextButton(onClick = {
                    leagueViewModel.resetGeneralRanking()
                    showResetDialog = false
                }) { Text(stringResource(R.string.confirm)) }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) { Text(stringResource(R.string.cancel)) }
            }
        )
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(stringResource(R.string.tab_ranking), style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
                if (selectedId == null) {
                    TextButton(onClick = { showResetDialog = true }) {
                        Text("Reiniciar General", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
            
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                val currentText = if (selectedId == null) stringResource(R.string.filter_all)
                                 else if (selectedId!! > 0) leagues.find { it.id == selectedId }?.name ?: stringResource(R.string.tab_leagues)
                                 else tournaments.find { it.id == -selectedId!! }?.name ?: stringResource(R.string.tab_tournaments)
                
                OutlinedTextField(
                    value = currentText,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.menuAnchor().width(150.dp),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    textStyle = MaterialTheme.typography.bodySmall
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(text = { Text(stringResource(R.string.filter_all)) }, onClick = { selectedId = null; expanded = false })
                    leagues.forEach { l -> DropdownMenuItem(text = { Text("${stringResource(R.string.tab_leagues)}: ${l.name}") }, onClick = { selectedId = l.id; expanded = false }) }
                    tournaments.filter { it.isFinished }.forEach { t -> DropdownMenuItem(text = { Text("${stringResource(R.string.tab_tournaments)}: ${t.name}") }, onClick = { selectedId = -t.id; expanded = false }) }
                }
            }
        }

        LazyColumn(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                        Text("Jugador", modifier = Modifier.weight(3f), fontWeight = FontWeight.Bold)
                        Text("PJ", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                        Text("PTS", modifier = Modifier.weight(1f), textAlign = TextAlign.End, fontWeight = FontWeight.Bold)
                    }
                }
            }
            items(currentRanking) { r ->
                val pName = players.find { it.id == r.playerId }?.nombre ?: "Desconocido"
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(pName, modifier = Modifier.weight(3f))
                        Text(r.matchesPlayed.toString(), modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                        Text(
                            text = r.points.toString(),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.End,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
