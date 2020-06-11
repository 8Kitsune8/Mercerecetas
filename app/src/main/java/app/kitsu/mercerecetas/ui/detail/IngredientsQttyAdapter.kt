package app.kitsu.mercerecetas.ui.detail

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.kitsu.mercerecetas.R
import app.kitsu.mercerecetas.database.RecipeIngredientQuantity
import app.kitsu.mercerecetas.databinding.ListIngredientsQttyBinding
import app.kitsu.mercerecetas.ui.home.RecipeAdapter
import kotlinx.android.synthetic.main.list_ingredients_qtty.view.*

class IngredientsQttyAdapter() : ListAdapter<RecipeIngredientQuantity,IngredientsQttyAdapter.IngredientQttyViewHolder>(IngrQttyDiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IngredientQttyViewHolder {
        return IngredientQttyViewHolder.from(parent)
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

    class IngredientQttyViewHolder private constructor(val binding: ListIngredientsQttyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recIngrQtty: RecipeIngredientQuantity){
            binding.recipeIngredientsQtty = recIngrQtty
            binding.executePendingBindings()
        }


        companion object {
         fun from(parent: ViewGroup): IngredientQttyViewHolder {
           val layoutInflater = LayoutInflater.from(parent.context)

             // when inflating, h params ", parent, false" ARE HIGHLY IMPORTANT to maintain the layout as expected
             val binding = ListIngredientsQttyBinding.inflate(layoutInflater, parent, false)
             return  IngredientQttyViewHolder(binding)
        }
    }
}

}