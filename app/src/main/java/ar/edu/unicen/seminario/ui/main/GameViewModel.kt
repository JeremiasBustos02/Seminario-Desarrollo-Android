package ar.edu.unicen.seminario.ui.main

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unicen.seminario.data.model.Game
import ar.edu.unicen.seminario.data.model.GameFilters
import ar.edu.unicen.seminario.data.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val repository: GameRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _games = MutableStateFlow<List<Game>>(emptyList())
    val games: StateFlow<List<Game>> = _games.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow(false)
    val error: StateFlow<Boolean> = _error.asStateFlow()

    // NUEVO: StateFlow para los filtros activos
    private val _activeFilters = MutableStateFlow<GameFilters?>(null)
    val activeFilters: StateFlow<GameFilters?> = _activeFilters.asStateFlow()

    private var currentFilters: GameFilters? = null

    init {
        loadFilters()
        getAllGames(20)
    }

    fun getAllGames(pageSize: Int) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = false
            try {
                val gamesList = repository.getAllGames(
                    pageSize = pageSize,
                    filters = currentFilters
                )
                _games.value = gamesList
            } catch (e: Exception) {
                _error.value = true
            } finally {
                _loading.value = false
            }
        }
    }

    private fun loadFilters() {
        currentFilters = GameFilters(
            platform = sharedPreferences.getString("filter_platform", null),
            genre = sharedPreferences.getString("filter_genre", null),
            year = sharedPreferences.getInt("filter_year", 0).takeIf { it != 0 },
            minRating = sharedPreferences.getFloat("filter_rating", 0f).takeIf { it != 0f }
        )
        // NUEVO: Actualiza el StateFlow
        _activeFilters.value = currentFilters
    }

    fun reloadWithFilters() {
        loadFilters()
        getAllGames(20)
    }

    // NUEVO: Verifica si hay filtros activos
    fun hasActiveFilters(): Boolean {
        return currentFilters?.let {
            it.platform != null || it.genre != null || it.year != null || it.minRating != null
        } ?: false
    }
}