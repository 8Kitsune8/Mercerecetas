package app.kitsu.mercerecetas.ui.home


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.kitsu.mercerecetas.R
import app.kitsu.mercerecetas.database.Recipe
import app.kitsu.mercerecetas.databinding.ListItemRecipeBinding

class RecipeAdapter() : RecyclerView.Adapter<RecipeAdapter.ViewHolder>(), Filterable {




    private var controlFirstTime: Boolean = true
    private var recycleFilter: RecycleFilter? = null
    private var fullList : List<Recipe>? = null

    var data = listOf<Recipe>()
    set(value){
        field = value
        if(controlFirstTime) {
            fullList = value
            controlFirstTime = false
        }
        notifyDataSetChanged()
    }

/*
Podria ser util. con esto quizas se puede usar el ListAdapter (uno simple que ya tiene RecyclerView) en vez del RecyclerView sin mas,
ya que ahora lo uso para poder setear y usar la lista de datos y filtrarla y con esto ya lo seteariamos,
permitiendo eliminar el set value actual y activar el callback de nuevo

    constructor(context: Context, list: ArrayList<Recipe>): this() {
        this.listFull = list
        this.list = list
        this.context = context
    }*/



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

    override fun getItemCount(): Int {
       return data.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getFilter(): Filter {

        if(recycleFilter == null){
            recycleFilter = RecycleFilter()
        }
       return recycleFilter as RecycleFilter
    }

    inner class RecycleFilter: Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var results: FilterResults = FilterResults()

            if(constraint != null && constraint.isNotEmpty()){
                var localList: ArrayList<Recipe> = ArrayList<Recipe>()

                for (i: Int in 0..fullList?.size?.minus(1) as Int) {
                    if(fullList?.get(i)?.name?.toLowerCase()?.contains(constraint.toString().toLowerCase()) as Boolean){
                        localList.add(fullList?.get(i) as Recipe)
                    }
                }
                results.values = localList
                results.count = localList?.size
            } else {
                results.values = fullList
                results.count = fullList?.size as Int
            }
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            data = results?.values as ArrayList<Recipe>
            //notifyDataSetChanged()
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