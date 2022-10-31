package com.test.perqaratest.domain.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.test.perqaratest.data.models.Game
import com.test.perqaratest.databinding.ItemGameBinding
import com.test.perqaratest.domain.utils.loadImage

class BookmarkedGamesAdapter: RecyclerView.Adapter<BookmarkedGamesViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Game>() {
        override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkedGamesViewHolder =
        BookmarkedGamesViewHolder(ItemGameBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: BookmarkedGamesViewHolder, position: Int) {
        val game = differ.currentList[holder.adapterPosition]
        holder.bind(game)

        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(game) }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    private var onItemClickListener: ((Game) -> Unit)? = null
    fun setOnItemClickListener(listener: (Game) -> Unit) {
        onItemClickListener = listener
    }
}

class BookmarkedGamesViewHolder(private val binding: ItemGameBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(game: Game) {
        with(binding) {
            imvThumbnail.loadImage(game.backgroundImage)
            tvTitle.text = game.name
            val releaseDate = "Released date ${game.released}"
            tvReleaseDate.text = releaseDate
            tvRating.text = game.rating.toString()
        }
    }
}