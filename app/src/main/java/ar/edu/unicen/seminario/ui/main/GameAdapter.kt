package ar.edu.unicen.seminario.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ar.edu.unicen.seminario.data.model.Game
import ar.edu.unicen.seminario.databinding.ListItemGameBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class GameAdapter(
    private val games: List<Game>,
    private val onGameClick: (Game) -> Unit
): RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemGameBinding.inflate(layoutInflater, parent, false)
        return GameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = games[position]
        holder.bind(game)
    }

    override fun getItemCount(): Int = games.size

    inner class GameViewHolder(
        private val binding: ListItemGameBinding
    ): RecyclerView.ViewHolder(binding.root){

        fun bind(game: Game) {
            binding.gameName.text = game.name
            binding.gameReleased.text = "Released: ${game.released ?: "TBA"}"

            // Rating
            binding.gameRating.text = game.rating?.toString() ?: "N/A"

            // Plataformas
            val platformNames = game.platforms?.joinToString(", ") { it.platform.name } ?: "N/A"
            binding.gamePlatforms.text = platformNames

            // Imagen con transición suave
            Glide.with(itemView.context)
                .load(game.background_image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.gameImage)

            binding.root.setOnClickListener {
                onGameClick(game)
            }
        }
    }
}