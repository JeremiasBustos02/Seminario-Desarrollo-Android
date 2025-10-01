package ar.edu.unicen.seminario.ui.filter

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ar.edu.unicen.seminario.R
import ar.edu.unicen.seminario.data.model.GameFilters
import ar.edu.unicen.seminario.databinding.ActivityFilterBinding
import ar.edu.unicen.seminario.ui.main.GameActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * FilterActivity es una actividad que permite al usuario aplicar filtros.
 * Incluye un botón para regresar a MainActivity.
 */
@AndroidEntryPoint
class FilterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilterBinding
    private val viewModel: FilterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSpinners()
        setupRatingSeekBar()
        loadSavedFilters()
        setupButtons()
    }

    private fun setupSpinners() {
        // Plataformas
        ArrayAdapter.createFromResource(
            this,
            R.array.platforms,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.platformSpinner.adapter = adapter
        }

        // Géneros
        ArrayAdapter.createFromResource(
            this,
            R.array.genres,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.genreSpinner.adapter = adapter
        }

        // Años
        ArrayAdapter.createFromResource(
            this,
            R.array.years,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.yearSpinner.adapter = adapter
        }
    }

    private fun setupRatingSeekBar() {
        binding.ratingSeekBar.max = 100
        binding.ratingSeekBar.progress = 0
        binding.ratingText.text = "0.0"

        binding.ratingSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val rating = progress / 10.0
                binding.ratingText.text = String.format("%.1f", rating)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun loadSavedFilters() {
        val savedFilters = viewModel.loadFilters()

        // Cargar plataforma guardada
        savedFilters.platform?.let { platform ->
            val platforms = resources.getStringArray(R.array.platforms)
            val position = platforms.indexOf(platform)
            if (position >= 0) {
                binding.platformSpinner.setSelection(position)
            }
        }

        // Cargar género guardado
        savedFilters.genre?.let { genre ->
            val genres = resources.getStringArray(R.array.genres)
            val position = genres.indexOf(genre)
            if (position >= 0) {
                binding.genreSpinner.setSelection(position)
            }
        }

        // Cargar año guardado
        savedFilters.year?.let { year ->
            val years = resources.getStringArray(R.array.years)
            val position = years.indexOf(year.toString())
            if (position >= 0) {
                binding.yearSpinner.setSelection(position)
            }
        }

        // Cargar rating guardado
        savedFilters.minRating?.let { rating ->
            binding.ratingSeekBar.progress = (rating * 10).toInt()
        }
    }

    private fun setupButtons() {
        binding.applyFiltersBtn.setOnClickListener {
            applyFilters()
        }

        binding.clearFiltersBtn.setOnClickListener {
            clearFilters()
        }

        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun applyFilters() {
        val platform = binding.platformSpinner.selectedItem.toString()
            .takeIf { it != "Todas las plataformas" }

        val genre = binding.genreSpinner.selectedItem.toString()
            .takeIf { it != "Todos los géneros" }

        val yearString = binding.yearSpinner.selectedItem.toString()
        val year = if (yearString != "Todos los años") yearString.toIntOrNull() else null

        val minRating = (binding.ratingSeekBar.progress / 10.0f).takeIf { it > 0 }

        val filters = GameFilters(
            platform = platform,
            genre = genre,
            year = year,
            minRating = minRating
        )

        viewModel.saveFilters(filters)
        finish() // Volver a GameActivity
    }

    private fun clearFilters() {
        viewModel.clearFilters()

        // Resetear UI
        binding.platformSpinner.setSelection(0)
        binding.genreSpinner.setSelection(0)
        binding.yearSpinner.setSelection(0)
        binding.ratingSeekBar.progress = 0
    }
}