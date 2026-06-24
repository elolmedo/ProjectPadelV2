package es.romsolutions.padeltournament.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.romsolutions.padeltournament.data.model.Tournament
import es.romsolutions.padeltournament.ui.components.EmptyState
import es.romsolutions.padeltournament.ui.components.TournamentTeamSetupDialog
import es.romsolutions.padeltournament.ui.viewmodel.PlayerViewModel
import es.romsolutions.padeltournament.ui.viewmodel.TournamentViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TournamentsListScreen(
    tournamentViewModel: TournamentViewModel,
    playerViewModel: PlayerViewModel,
    onTournamentStarted: (Int?) -> Unit
) {
    val tournaments by tournamentViewModel.allTournaments.collectAsState()
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    var selectedTournamentForStart by remember { mutableStateOf<Tournament?>(null) }

    if (tournaments.isEmpty()) {
        EmptyState(
            icon = Icons.Default.MilitaryTech,
            title = "No hay Torneos",
            description = "Organiza un torneo americano o express y empieza a jugar."
        )
    } else {
        var tournamentToFinish by remember { mutableStateOf<Tournament?>(null) }
        
        if (tournamentToFinish != null) {
            AlertDialog(
                onDismissRequest = { tournamentToFinish = null },
                title = { Text("Finalizar Torneo") },
                text = { Text("¿Estás seguro de finalizar '${tournamentToFinish?.name}'? Se cerrará y generará el ranking.") },
                confirmButton = {
                    TextButton(onClick = {
                        tournamentViewModel.finishTournament(tournamentToFinish!!)
                        tournamentToFinish = null
                    }) { Text("Confirmar") }
                },
                dismissButton = {
                    TextButton(onClick = { tournamentToFinish = null }) { Text("Cancelar") }
                }
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 300.dp),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tournaments) { tournament ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { if(tournament.isStarted) onTournamentStarted(tournament.id) }
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = tournament.name, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                            val displayType = if (tournament.type == "POZO") "Rey de la Pista" else tournament.type
                            Text(text = "Tipo: $displayType | Pistas: ${tournament.numberCourts}")
                            Text(text = "Inicio: ${sdf.format(Date(tournament.dateTour ?: 0))}")
                            val status = if (tournament.isFinished) "FINALIZADO" else if (tournament.isStarted) "EN CURSO" else "PENDIENTE"
                            Text(
                                text = "ESTADO: $status", 
                                fontWeight = FontWeight.Bold,
                                color = if(tournament.isFinished) MaterialTheme.colorScheme.secondary else if(tournament.isStarted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        
                        if (!tournament.isStarted) {
                            IconButton(onClick = { 
                                if (tournament.isTeamBased) {
                                    selectedTournamentForStart = tournament
                                } else {
                                    tournamentViewModel.startTournament(tournament)
                                    onTournamentStarted(tournament.id) 
                                }
                            }) {
                                Icon(Icons.Default.PlayArrow, contentDescription = "Iniciar")
                            }
                        } else if (!tournament.isFinished) {
                            Button(
                                onClick = { tournamentToFinish = tournament },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                            ) {
                                Text("Finalizar")
                            }
                        }

                        IconButton(onClick = { tournamentViewModel.delete(tournament) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }

    if (selectedTournamentForStart != null) {
        TournamentTeamSetupDialog(
            tournament = selectedTournamentForStart!!,
            playerViewModel = playerViewModel,
            tournamentViewModel = tournamentViewModel,
            onDismiss = { selectedTournamentForStart = null },
            onConfirm = { teams ->
                tournamentViewModel.startTournamentWithTeams(selectedTournamentForStart!!, teams)
                val id = selectedTournamentForStart!!.id
                selectedTournamentForStart = null
                onTournamentStarted(id)
            }
        )
    }
}
