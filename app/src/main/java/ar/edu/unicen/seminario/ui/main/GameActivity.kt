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
    private lateinit var gameAdapter: GameAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()  // NUEVO: Configurar adapter desde el inicio
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

    // NUEVO: Configura el RecyclerView una sola vez
    private fun setupRecyclerView() {
        gameAdapter = GameAdapter(
            games = emptyList(),
            onGameClick = { game ->
                Toast.makeText(this, "Game ${game.name} clicked", Toast.LENGTH_SHORT).show()
            }
        )
        binding.gamesList.adapter = gameAdapter
    }

    private fun subscribeToViewModel() {
        // Observa el estado de carga y actualiza la UI
        viewModel.loading.onEach { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.INVISIBLE
        }.launchIn(lifecycleScope)

        // Observa la lista de juegos y actualiza el adaptador
        viewModel.games.onEach { games ->
            // MODIFICADO: Actualiza el adapter existente en lugar de crear uno nuevo
            gameAdapter = GameAdapter(
                games = games,
                onGameClick = { game ->
                    Toast.makeText(this, "Game ${game.name} clicked", Toast.LENGTH_SHORT).show()
                }
            )
            binding.gamesList.adapter = gameAdapter
        }.launchIn(lifecycleScope)

        // Observa el estado de error
        viewModel.error.onEach { error ->
            binding.error.visibility = if (error) View.VISIBLE else View.INVISIBLE
        }.launchIn(lifecycleScope)

        // Observa los filtros activos
        viewModel.activeFilters.onEach { filters ->
            updateActiveFiltersDisplay(filters)
        }.launchIn(lifecycleScope)
    }

    private fun updateActiveFiltersDisplay(filters: GameFilters?) {
        if (filters == null || !viewModel.hasActiveFilters()) {
            binding.activeFiltersText.visibility = View.GONE
            return
        }

        val filterParts = mutableListOf<String>()

        filters.platform?.let { filterParts.add("Plataforma: $it") }
        filters.genre?.let { filterParts.add("Género: $it") }
        filters.year?.let { filterParts.add("Año: $it") }
        filters.minRating?.let {
            filterParts.add("Calificación: ${String.format("%.1f", it)}+")
        }

        if (filterParts.isNotEmpty()) {
            binding.activeFiltersText.text = "Filtros: ${filterParts.joinToString(" | ")}"
            binding.activeFiltersText.visibility = View.VISIBLE
        } else {
            binding.activeFiltersText.visibility = View.GONE
        }
    }
}