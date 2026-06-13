package es.romsolutions.padeltournament.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import es.romsolutions.padeltournament.R
import es.romsolutions.padeltournament.data.model.Player
import es.romsolutions.padeltournament.ui.components.AddPlayerDialog
import es.romsolutions.padeltournament.ui.components.EmptyState
import es.romsolutions.padeltournament.ui.viewmodel.PlayerViewModel

@Composable
fun PlayersListScreen(
    viewModel: PlayerViewModel,
    onNavigateToCreateLeague: (List<Int>) -> Unit,
    onNavigateToCreateTournament: (List<Int>) -> Unit
) {
    val players by viewModel.allPlayers.collectAsState()
    var selectedPlayerIds by remember { mutableStateOf(setOf<Int>()) }
    var editingPlayer by remember { mutableStateOf<Player?>(null) }
    
    Column(modifier = Modifier.fillMaxSize()) {
        if (selectedPlayerIds.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                if (selectedPlayerIds.size == 1) {
                    Button(onClick = { editingPlayer = players.find { it.id == selectedPlayerIds.first() } }) {
                        Text(stringResource(R.string.edit))
                    }
                }
                Button(onClick = { onNavigateToCreateLeague(selectedPlayerIds.toList()) }) {
                    Text(stringResource(R.string.tab_leagues))
                }
                Button(onClick = { onNavigateToCreateTournament(selectedPlayerIds.toList()) }) {
                    Text(stringResource(R.string.tab_tournaments))
                }
                Button(
                    onClick = { 
                        selectedPlayerIds.forEach { viewModel.delete(it) }
                        selectedPlayerIds = emptySet()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(stringResource(R.string.delete))
                }
            }
        }

        if (players.isEmpty()) {
            EmptyState(
                icon = Icons.Default.Person,
                title = stringResource(R.string.empty_players),
                description = ""
            )
        } else {
            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(players) { p ->
                    val isSelected = selectedPlayerIds.contains(p.id)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedPlayerIds = if (isSelected) selectedPlayerIds - p.id else selectedPlayerIds + p.id
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = isSelected, onCheckedChange = null)
                            Column(modifier = Modifier.padding(start = 8.dp)) {
                                Text(text = p.nombre, style = MaterialTheme.typography.titleLarge)
                                Text(text = "Tel: ${p.phone} | Email: ${p.email}", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                            }
                        }
                    }
                }
            }
        }
        
        Button(
            onClick = { viewModel.resetDatabase() },
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
        ) {
            Text("Cargar Demo")
        }
    }

    if (editingPlayer != null) {
        EditPlayerDialog(
            player = editingPlayer!!,
            onDismiss = { editingPlayer = null },
            onSave = { updated -> 
                viewModel.insert(updated)
                editingPlayer = null
                selectedPlayerIds = emptySet()
            }
        )
    }
}

@Composable
fun EditPlayerDialog(player: Player, onDismiss: () -> Unit, onSave: (Player) -> Unit) {
    var n by remember { mutableStateOf(player.nombre) }
    var p by remember { mutableStateOf(player.phone) }
    var e by remember { mutableStateOf(player.email) }

    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Editar Jugador", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
                OutlinedTextField(value = n, onValueChange = { n = it }, label = { Text("Nombre") })
                OutlinedTextField(value = p, onValueChange = { p = it }, label = { Text("Teléfono") })
                OutlinedTextField(value = e, onValueChange = { e = it }, label = { Text("Email") })
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancelar") }
                    Button(onClick = { onSave(player.copy(nombre = n, phone = p, email = e)) }) { Text("Guardar") }
                }
            }
        }
    }
}
