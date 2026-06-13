package es.romsolutions.padeltournament.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import es.romsolutions.padeltournament.data.model.Match
import es.romsolutions.padeltournament.data.model.Player
import es.romsolutions.padeltournament.data.model.Team

@Composable
fun EditMatchScoreDialog(
    match: Match, 
    players: List<Player>, 
    teams: List<Team>, 
    onDismiss: () -> Unit, 
    onSave: (Match) -> Unit
) {
    var s1 by remember { mutableStateOf(match.scoreTeamOne.toString()) }
    var s2 by remember { mutableStateOf(match.scoreTeamTwo.toString()) }
    
    val p1 = players.find { it.id == match.player1Id }?.nombre ?: "?"
    val p2 = players.find { it.id == match.player2Id }?.nombre ?: "?"
    val p3 = players.find { it.id == match.player3Id }?.nombre ?: "?"
    val p4 = players.find { it.id == match.player4Id }?.nombre ?: "?"
    
    val t1 = teams.find { it.leagueId == match.leagueId && (it.playerOneId == match.player1Id || it.playerOneId == match.player2Id) }?.nameTeam ?: "Pareja 1"
    val t2 = teams.find { it.leagueId == match.leagueId && (it.playerOneId == match.player3Id || it.playerOneId == match.player4Id) }?.nameTeam ?: "Pareja 2"

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Actualizar Marcador", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                        Text(t1, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                        Text("$p1 / $p2", style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = s1, 
                            onValueChange = { if(it.all { c -> c.isDigit() }) s1 = it }, 
                            modifier = Modifier.width(70.dp), 
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                        )
                    }
                    
                    Text("VS", style = MaterialTheme.typography.titleLarge, modifier = Modifier.align(Alignment.CenterVertically), color = MaterialTheme.colorScheme.secondary)
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                        Text(t2, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                        Text("$p3 / $p4", style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = s2, 
                            onValueChange = { if(it.all { c -> c.isDigit() }) s2 = it }, 
                            modifier = Modifier.width(70.dp), 
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                        )
                    }
                }
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancelar") }
                    Button(
                        onClick = { 
                            onSave(match.copy(
                                scoreTeamOne = s1.toIntOrNull() ?: 0, 
                                scoreTeamTwo = s2.toIntOrNull() ?: 0, 
                                playFinish = System.currentTimeMillis()
                            )) 
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) { Text("Guardar") }
                }
            }
        }
    }
}
