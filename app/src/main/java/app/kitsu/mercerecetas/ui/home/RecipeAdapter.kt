package app.kitsu.mercerecetas.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.kitsu.mercerecetas.R
import app.kitsu.mercerecetas.database.Recipe
import app.kitsu.mercerecetas.databinding.ListItemRecipeBinding

class RecipeAdapter : ListAdapter<Recipe, RecipeAdapter.ViewHolder>(RecipeDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.list_item_recipe, parent, false))
    }

    class ViewHolder constructor(val binding: ListItemRecipeBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Recipe) {
            binding.recipe = item

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemRecipeBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}


class RecipeDiffCallback : DiffUtil.ItemCallback<Recipe>() {

    override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem.recipeId == newItem.recipeId
    }


    override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem == newItem
    }
}