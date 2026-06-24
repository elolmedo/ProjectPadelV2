package es.romsolutions.padeltournament.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import es.romsolutions.padeltournament.R
import es.romsolutions.padeltournament.data.model.League
import es.romsolutions.padeltournament.data.model.TeamInput
import es.romsolutions.padeltournament.ui.components.EmptyState
import es.romsolutions.padeltournament.ui.components.StartLeagueDialog
import es.romsolutions.padeltournament.ui.components.TeamSetupDialog
import es.romsolutions.padeltournament.ui.viewmodel.LeagueViewModel
import es.romsolutions.padeltournament.ui.viewmodel.PlayerViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun LeaguesListScreen(
    leagueViewModel: LeagueViewModel,
    playerViewModel: PlayerViewModel,
    onLeagueStarted: (Int?) -> Unit
) {
    val leagues by leagueViewModel.allLeagues.collectAsState()
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    var selectedLeagueForStart by remember { mutableStateOf<League?>(null) }
    
    var showTeamSetupDialog by remember { mutableStateOf<League?>(null) }
    var selectedDays by remember { mutableStateOf<List<Int>>(emptyList()) }
    var numCourts by remember { mutableIntStateOf(1) }
    var startH by remember { mutableIntStateOf(0) }
    var startM by remember { mutableIntStateOf(0) }

    if (leagues.isEmpty()) {
        EmptyState(
            icon = Icons.Default.EmojiEvents,
            title = stringResource(R.string.empty_leagues),
            description = ""
        )
    } else {
        var leagueToFinish by remember { mutableStateOf<League?>(null) }
        
        if (leagueToFinish != null) {
            AlertDialog(
                onDismissRequest = { leagueToFinish = null },
                title = { Text(stringResource(R.string.finish)) },
                text = { Text("${stringResource(R.string.confirm)} ${leagueToFinish?.name}?") },
                confirmButton = {
                    TextButton(onClick = {
                        leagueViewModel.finishLeague(leagueToFinish!!)
                        leagueToFinish = null
                    }) { Text(stringResource(R.string.confirm)) }
                },
                dismissButton = {
                    TextButton(onClick = { leagueToFinish = null }) { Text(stringResource(R.string.cancel)) }
                }
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 300.dp),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(leagues) { l ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { if (l.isStarted) onLeagueStarted(l.id) }
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = l.name, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                            Text(text = "Equipos: ${l.size} | Inicio: ${sdf.format(Date(l.startDate))}")
                            val status = if (l.isFinished) "FINALIZADA" else if (l.isStarted) "EN CURSO" else "PENDIENTE"
                            Text(
                                text = "ESTADO: $status", 
                                fontWeight = FontWeight.Bold, 
                                color = if(l.isFinished) MaterialTheme.colorScheme.secondary else if(l.isStarted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        
                        if (!l.isStarted) {
                            IconButton(onClick = { selectedLeagueForStart = l }) {
                                Icon(Icons.Default.PlayArrow, contentDescription = "Iniciar")
                            }
                        } else if (!l.isFinished) {
                            Button(
                                onClick = { leagueToFinish = l },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                            ) {
                                Text("Finalizar")
                            }
                        }

                        IconButton(onClick = { leagueViewModel.delete(l) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }

    if (selectedLeagueForStart != null) {
        StartLeagueDialog(
            league = selectedLeagueForStart!!,
            onDismiss = { selectedLeagueForStart = null },
            onConfirm = { days, nCourts, sh, sm ->
                showTeamSetupDialog = selectedLeagueForStart
                selectedDays = days
                numCourts = nCourts
                startH = sh
                startM = sm
                selectedLeagueForStart = null
            }
        )
    }

    if (showTeamSetupDialog != null) {
        TeamSetupDialog(
            league = showTeamSetupDialog!!,
            playerViewModel = playerViewModel,
            leagueViewModel = leagueViewModel,
            onDismiss = { showTeamSetupDialog = null },
            onConfirm = { teams ->
                leagueViewModel.startLeagueWithTeams(showTeamSetupDialog!!, selectedDays, teams, numCourts, startH, startM)
                val id = showTeamSetupDialog!!.id
                showTeamSetupDialog = null
                onLeagueStarted(id)
            }
        )
    }
}
