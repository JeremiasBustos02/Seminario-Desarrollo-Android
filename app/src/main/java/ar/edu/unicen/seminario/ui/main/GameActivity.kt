package ar.edu.unicen.seminario.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import ar.edu.unicen.seminario.R
import ar.edu.unicen.seminario.data.model.GameFilters
import ar.edu.unicen.seminario.databinding.ActivityMainBinding
import ar.edu.unicen.seminario.ui.filter.FilterActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class GameActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val viewModel by viewModels<GameViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        subscribeToUi()
        subscribeToViewModel()

        binding.buttonFilter.setOnClickListener {
            val intent = Intent(this, FilterActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.reloadWithFilters()
    }

    private fun subscribeToUi() {
        binding.buttonLoadGames.setOnClickListener {
            viewModel.getAllGames(20)
        }
    }

    private fun subscribeToViewModel() {
        // Observa el estado de carga y actualiza la UI
        viewModel.loading.onEach { loading ->
            if (loading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.INVISIBLE
            }
            binding.buttonLoadGames.isEnabled = !loading
        }.launchIn(lifecycleScope)

        // Observa la lista de juegos y actualiza el adaptador del RecyclerView
        viewModel.games.onEach { games ->
            binding.gamesList.adapter = GameAdapter(
                games = games,
                onGameClick = { game ->
                    Toast.makeText(this, "Game ${game.name} clicked", Toast.LENGTH_SHORT).show()
                }
            )
        }.launchIn(lifecycleScope)

        // Observa el estado de error y actualiza la visibilidad del mensaje de error
        viewModel.error.onEach { error ->
            if (error) {
                binding.error.visibility = View.VISIBLE
            } else {
                binding.error.visibility = View.INVISIBLE
            }
        }.launchIn(lifecycleScope)

        // NUEVO: Observa los filtros activos y actualiza la UI
        viewModel.activeFilters.onEach { filters ->
            updateActiveFiltersDisplay(filters)
        }.launchIn(lifecycleScope)
    }

    // NUEVO: Actualiza el TextView de filtros activos
    private fun updateActiveFiltersDisplay(filters: GameFilters?) {
        if (filters == null || !viewModel.hasActiveFilters()) {
            binding.activeFiltersText.visibility = View.GONE
            return
        }

        val filterParts = mutableListOf<String>()

        filters.platform?.let { filterParts.add("🎮 $it") }
        filters.genre?.let { filterParts.add("📂 $it") }
        filters.year?.let { filterParts.add("📅 $it") }
        filters.minRating?.let {
            filterParts.add("⭐ ${String.format("%.1f", it)}+")
        }

        if (filterParts.isNotEmpty()) {
            binding.activeFiltersText.text = "Filtros: ${filterParts.joinToString(" • ")}"
            binding.activeFiltersText.visibility = View.VISIBLE
        } else {
            binding.activeFiltersText.visibility = View.GONE
        }
    }
}