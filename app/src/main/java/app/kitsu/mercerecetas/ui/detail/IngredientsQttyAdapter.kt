package app.kitsu.mercerecetas.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.kitsu.mercerecetas.database.RecipeIngredientQuantity
import app.kitsu.mercerecetas.databinding.ListIngredientsQttyBinding

class IngredientsQttyAdapter : ListAdapter<RecipeIngredientQuantity,IngredientsQttyAdapter.IngredientQttyViewHolder>(IngrQttyDiffCallback) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IngredientQttyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return IngredientQttyViewHolder(ListIngredientsQttyBinding.inflate(layoutInflater))
    }

    override fun onBindViewHolder(holder: IngredientQttyViewHolder, position: Int) {
        val recIngrQtty = getItem(position)
        holder.bind(recIngrQtty)
    }

    //different way to use DiffCallback instead creating a new class:
    companion object IngrQttyDiffCallback : DiffUtil.ItemCallback<RecipeIngredientQuantity>() {

        override fun areItemsTheSame(
            oldItem: RecipeIngredientQuantity,
            newItem: RecipeIngredientQuantity
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: RecipeIngredientQuantity,
            newItem: RecipeIngredientQuantity
        ): Boolean {
            return oldItem.recIngQttyId == newItem.recIngQttyId
        }

    }

    class IngredientQttyViewHolder(private var binding: ListIngredientsQttyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recIngrQtty: RecipeIngredientQuantity){
            binding.recipeIngredientsQtty = recIngrQtty
            binding.executePendingBindings()
        }

    }
}