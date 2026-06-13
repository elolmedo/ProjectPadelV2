package es.romsolutions.padeltournament.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import es.romsolutions.padeltournament.data.model.Player
import es.romsolutions.padeltournament.data.repository.PlayerRepository
import es.romsolutions.padeltournament.data.database.DatabaseSeeder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlayerViewModel(private val repository: PlayerRepository) : ViewModel() {

    val allPlayers: StateFlow<List<Player>> = repository.allPlayers
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun insert(player: Player) = viewModelScope.launch {
        repository.insert(player)
    }

    fun delete(id: Int) = viewModelScope.launch {
        repository.delete(id)
    }

    fun resetDatabase() = viewModelScope.launch {
        repository.deleteAll()
        DatabaseSeeder.seedPlayersIfEmpty(repository.playerDao, viewModelScope)
    }
}

class PlayerViewModelFactory(private val repository: PlayerRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlayerViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
