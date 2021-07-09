package dev.myanes.marvelheroes.presentation.screens.herolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.myanes.marvelheroes.databinding.ItemListHeroBinding
import dev.myanes.marvelheroes.domain.models.Hero

class HeroListAdapter(
    private val listener: HeroListListener
) : ListAdapter<Hero, HeroListAdapter.ItemViewHolder>(DiffUtilCallback()) {

    interface HeroListListener {
        fun onHeroItemClick(hero: Hero)
    }


    inner class ItemViewHolder(
        private val itemBinding: ItemListHeroBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(hero: Hero) {
            with(itemBinding) {
                tvName.text = hero.name
            }
            itemView.setOnClickListener {
                listener.onHeroItemClick(hero)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemListHeroBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun updateData(newList: List<Hero>) {
        submitList(newList)
    }
}

private class DiffUtilCallback : DiffUtil.ItemCallback<Hero>() {

    override fun areItemsTheSame(oldItem: Hero, newItem: Hero): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Hero, newItem: Hero): Boolean = oldItem == newItem

}