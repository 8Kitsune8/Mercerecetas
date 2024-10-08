package app.kitsu.mercerecetas.ui.home


import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import app.kitsu.mercerecetas.R
import app.kitsu.mercerecetas.database.RecipeDatabase
import app.kitsu.mercerecetas.database.RecipeFilter
import app.kitsu.mercerecetas.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: RecipeAdapter
    private lateinit var homeViewModel: HomeViewModel
    private var tracker: SelectionTracker<Long>? = null
    private var actionMode: ActionMode? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

      //  var currRecipes = listOf<Recipe>()
            // Get a reference to the binding object and inflate the fragment views.
        binding = FragmentHomeBinding.inflate(inflater,container, false)

        val application = requireNotNull(this.activity).application

        if(savedInstanceState != null)
            tracker?.onRestoreInstanceState(savedInstanceState)

        // Create an instance of the ViewModel Factory.
        val dataSource = RecipeDatabase.getInstance(application).recipeDatabaseDao
        val viewModelFactory = HomeViewModelFactory(dataSource, application)

        // Get a reference to the ViewModel associated with this fragment.
         homeViewModel =
            ViewModelProvider(
                this, viewModelFactory).get(HomeViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.homeViewModel = homeViewModel

        //initialize RecyclerView adapter
        adapter = RecipeAdapter(RecipeAdapter.OnClickListener{
           homeViewModel.displayRecipeDetails(it)
           homeViewModel.displayRecipeDetailsComplete()
       })

        binding.recipeList.adapter = adapter

        homeViewModel.navigateToSelectedRecipe.observe(viewLifecycleOwner, Observer {
            it?.let { this.findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToDetailFragment(it))
                homeViewModel.displayRecipeDetailsComplete()
            }
        })


        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.setLifecycleOwner(this)

        val manager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recipeList.layoutManager = manager

        tracker = SelectionTracker.Builder<Long>(
            "selection-1",
            binding.recipeList,
            StableIdKeyProvider(binding.recipeList),
            MyLookup(binding.recipeList),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        adapter.setTracker(tracker)
       tracker?.addObserver(
            object: SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    var title: String
                    val nItems:Int? = tracker?.selection?.size()

                    if(nItems!=null && nItems > 0) {
                        actionMode = activity?.startActionMode(actionModeCallbacks)
                    /*    title = "$nItems items seleccionados"

                        // Change title and color of action bar
                       // (activity as MainActivity).supportActionBar?.title = title
                        (activity as MainActivity).supportActionBar?.setBackgroundDrawable(
                            context?.let { ContextCompat.getColor(it,R.color.secondaryDarkColor) }?.let {
                                ColorDrawable(
                                    it
                                )
                            })*/
                    } else {

                        // Reset color and title to default values

                  /*      title = "Recetas"
                        (activity as MainActivity).supportActionBar?.setBackgroundDrawable(
                            context?.let { ContextCompat.getColor(it,R.color.primaryColor) }?.let {
                                ColorDrawable(
                                    it
                                )
                            })*/
                    }
                  //  (activity as MainActivity).supportActionBar?.title = title
                }
            })


        setHasOptionsMenu(true)

        //registerForContextMenu(binding.recipeList)

   /*     binding.recipeList.setOnLongClickListener { view ->
            // Called when the user long-clicks on someView
            when (actionMode) {
                null -> {
                    // Start the CAB using the ActionMode.Callback defined above

                    view.isSelected = true
                    true
                }
                else -> false
            }
        }*/



        return binding.root
    }

    private val actionModeCallbacks: ActionMode.Callback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            menu?.add("Generar Lista")
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            val selectedItems =  adapter.selectedItems
            val recipeIds: LongArray = selectedItems.flatMap { (rId) -> listOf(rId)}.toLongArray()
            this@HomeFragment.findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToShoppingFragment(recipeIds))
            mode?.finish()
            return true
        }

        override fun onDestroyActionMode(mode: ActionMode?) {}
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.search_menu, menu)

        val searchItem: MenuItem? = menu?.findItem(R.id.app_bar_search)
        val searchView: SearchView? = searchItem?.actionView as SearchView

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                adapter?.filter?.filter(newText)
                return false

            }

            override fun onQueryTextSubmit(query: String): Boolean {

                return false
            }

        })

       super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

         if (item.itemId != R.id.overflowMenu) {
            homeViewModel.updateRecipeList(
                    when (item.itemId) {
                        R.id.show_fish -> RecipeFilter.SHOW_FISH
                        R.id.show_meat -> RecipeFilter.SHOW_MEAT
                        R.id.show_pasta -> RecipeFilter.SHOW_PASTA
                        R.id.show_rice -> RecipeFilter.SHOW_RICE
                        R.id.show_vegetables -> RecipeFilter.SHOW_VEGETABLES

                        else -> RecipeFilter.SHOW_ALL
                    }
                )
         }
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(outState != null)
            tracker?.onSaveInstanceState(outState)
    }


/*    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = (activity as MainActivity).menuInflater
        inflater.inflate(R.menu.create_shopping_list_menu, menu)

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        return when (item.itemId) {
            R.id.create_shopping_list -> {
             //   this.findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToShoppingFragment(recipes))
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }*/

}
