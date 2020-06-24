package app.kitsu.mercerecetas.ui.home


import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.core.content.ContextCompat.getColor
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.kitsu.mercerecetas.R
import app.kitsu.mercerecetas.database.Recipe
import app.kitsu.mercerecetas.databinding.ListItemRecipeBinding


class RecipeAdapter(private val onClickListener: OnClickListener) : ListAdapter<Recipe,RecipeAdapter.ViewHolder>(RecipeDiffCallback()), Filterable {

    private var recycleFilter: RecycleFilter? = null
    private var fullList : List<Recipe>? = null
    private var tracker: SelectionTracker<Long>? = null

    var selectedItems : ArrayList<Recipe> = ArrayList()


    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).recipeId
        //return position.toLong()
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

          fun getItemDetails() : ItemDetailsLookup.ItemDetails<Long> =
              object : ItemDetailsLookup.ItemDetails<Long>() {
                  override fun getSelectionKey(): Long? = itemId

                  override fun getPosition(): Int = adapterPosition

              }

         companion object {
             fun from(parent: ViewGroup): ViewHolder {
                 val layoutInflater = LayoutInflater.from(parent.context)
                 val binding = ListItemRecipeBinding.inflate(layoutInflater, parent, false)
                 return ViewHolder(binding)
             }
         }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item)
        val parent = holder.itemView as RelativeLayout

        if(tracker!!.isSelected(getItem(position).recipeId)) {
            parent.background = ColorDrawable(
               // Color.parseColor("#80deea")
                getColor(parent.context,R.color.primaryColor)
            )
            selectedItems.add(item)
        } else {
            // Reset color to background if not selected
            parent.background = ColorDrawable(getColor(parent.context,R.color.secondaryColor))
            selectedItems.remove(item)
        }

        holder.itemView.setOnClickListener{
            onClickListener.onClick(item,tracker)
        }

    }
    /*private val actionModeCallbacks: ActionMode.Callback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            multiSelect = true
            menu?.add("Generar Lista")
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            (parent.context as AppCompatActivity).findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToShoppingFragment(selectedItems)
        }

        override fun onDestroyActionMode(mode: ActionMode?) {}
    }
*/
    override fun getFilter(): Filter {

        if(recycleFilter == null){
            recycleFilter = RecycleFilter()
        }
       return recycleFilter as RecycleFilter
    }

    fun setCurrentFullList(fullCurList: List<Recipe>){
        fullList = fullCurList
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
            val res = results?.values as ArrayList<Recipe>
            submitList(res)
        }

    }
    class OnClickListener(val clickListener: (recipe : Recipe) -> Unit ) {
        fun onClick(recipe : Recipe,tracker: SelectionTracker<Long>?){
            if (!tracker!!.hasSelection()){
                clickListener(recipe)
            }
        }
    }

    fun setTracker(tracker: SelectionTracker<Long>?) {
        this.tracker = tracker
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