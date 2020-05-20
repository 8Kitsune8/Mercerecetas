package app.kitsu.mercerecetas.ui.home


import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import app.kitsu.mercerecetas.R
import app.kitsu.mercerecetas.database.RecipeDatabase
import app.kitsu.mercerecetas.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: RecipeAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Get a reference to the binding object and inflate the fragment views.
        binding = FragmentHomeBinding.inflate(inflater,container, false)

        val application = requireNotNull(this.activity).application

        // Create an instance of the ViewModel Factory.
        val dataSource = RecipeDatabase.getInstance(application).recipeDatabaseDao
        val viewModelFactory = HomeViewModelFactory(dataSource, application)

        // Get a reference to the ViewModel associated with this fragment.
        val homeViewModel =
            ViewModelProvider(
                this, viewModelFactory).get(HomeViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.homeViewModel = homeViewModel

        //initialize RecyclerView adapter
       val adapter = RecipeAdapter(RecipeAdapter.OnClickListener{
           homeViewModel.displayRecipeDetails(it)
           homeViewModel.displayRecipeDetailsComplete()
       })

        binding.recipeList.adapter = adapter

        homeViewModel.recipes.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
            }
        })

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


        setHasOptionsMenu(true)

        return binding.root
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

       // super.onCreateOptionsMenu(menu, inflater)
    }
}
