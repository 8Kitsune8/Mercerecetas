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

class IngredientsQttyAdapter() : RecyclerView.Adapter<IngredientsQttyAdapter.IngredientQttyViewHolder>() {

    var data = listOf<RecipeIngredientQuantity>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IngredientQttyViewHolder {
        return IngredientQttyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: IngredientQttyViewHolder, position: Int) {

        val recIngrQtty = data[position]

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

    class IngredientQttyViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(recIngrQtty: RecipeIngredientQuantity){
            itemView.ingredient_name.text = recIngrQtty.ingredient?.name.toString()
            itemView.ingredient_qtty.text = "${recIngrQtty.ingredientQtty} ${recIngrQtty.ingredient?.type}"
        }

        val name: TextView = itemView.findViewById(R.id.ingredient_name)
        val qtty: TextView = itemView.findViewById(R.id.ingredient_qtty)

        companion object {
         fun from(parent: ViewGroup): IngredientQttyViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            return IngredientQttyViewHolder(
                layoutInflater.inflate(
                    R.layout.list_ingredients_qtty,
                    parent, false
                )
            )
        }
    }
}

    override fun getItemCount(): Int {
       return data.size
    }
}