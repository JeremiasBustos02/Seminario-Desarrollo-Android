package ar.edu.unicen.seminario.ui.filter

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import ar.edu.unicen.seminario.data.model.GameFilters
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    fun saveFilters(filters: GameFilters) {
        sharedPreferences.edit().apply {
            putString("filter_platform", filters.platform)
            putString("filter_genre", filters.genre)
            putInt("filter_year", filters.year ?: 0)
            putFloat("filter_rating", filters.minRating ?: 0f)
            apply()
        }
    }

    fun loadFilters(): GameFilters {
        return GameFilters(
            platform = sharedPreferences.getString("filter_platform", null),
            genre = sharedPreferences.getString("filter_genre", null),
            year = sharedPreferences.getInt("filter_year", 0).takeIf { it != 0 },
            minRating = sharedPreferences.getFloat("filter_rating", 0f).takeIf { it != 0f }
        )
    }

    fun clearFilters() {
        sharedPreferences.edit().apply {
            remove("filter_platform")
            remove("filter_genre")
            remove("filter_year")
            remove("filter_rating")
            apply()
        }
    }
}