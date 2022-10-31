package com.test.perqaratest.domain.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.test.perqaratest.data.models.GameItem
import com.test.perqaratest.databinding.ItemGameBinding
import com.test.perqaratest.domain.utils.loadImage

class GamesAdapter: RecyclerView.Adapter<GamesViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<GameItem>() {
        override fun areItemsTheSame(oldItem: GameItem, newItem: GameItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GameItem, newItem: GameItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GamesViewHolder =
        GamesViewHolder(ItemGameBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: GamesViewHolder, position: Int) {
        val game = differ.currentList[holder.adapterPosition]
        holder.bind(game)

        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(game) }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    private var onItemClickListener: ((GameItem) -> Unit)? = null
    fun setOnItemClickListener(listener: (GameItem) -> Unit) {
        onItemClickListener = listener
    }
}

class GamesViewHolder(private val binding: ItemGameBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(game: GameItem) {
        with(binding) {
            imvThumbnail.loadImage(game.backgroundImage)
            tvTitle.text = game.name
            val releaseDate = "Released date ${game.released}"
            tvReleaseDate.text = releaseDate
            tvRating.text = game.rating.toString()
        }
    }
}