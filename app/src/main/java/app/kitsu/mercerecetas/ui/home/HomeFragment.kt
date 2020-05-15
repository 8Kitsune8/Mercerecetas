package app.kitsu.mercerecetas.ui.home

import android.app.SearchManager
import android.app.Service
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Filter
import android.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import app.kitsu.mercerecetas.MainActivity
import app.kitsu.mercerecetas.R
import app.kitsu.mercerecetas.database.Recipe
import app.kitsu.mercerecetas.database.RecipeDatabase
import app.kitsu.mercerecetas.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val adapter = RecipeAdapter()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater,container, false)

        // Get a reference to the binding object and inflate the fragment views.
      /*  val binding: FragmentHomeBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false)*/

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


       //val adapter = RecipeAdapter()
        binding.recipeList.adapter = adapter
        homeViewModel.recipes.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
            }
        })

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.setLifecycleOwner(this)

        val manager = GridLayoutManager(activity, 2)
        binding.recipeList.layoutManager = manager

 /*       binding.toolbar.setTitleTextColor(ContextCompat.getColor(application,R.color.colorPrimaryDark))
        (this.activity)?.setActionBar(toolbar)
        binding.toolbar.title = "testing"*/
        setHasOptionsMenu(true)



        return binding.root
    }
/*
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.main_menu, menu)
        val item = menu.findItem(R.id.app_bar_search)
        val searchView = SearchView(
            (mContext as MainActivity).supportActionBar!!.themedContext
        )
        // MenuItemCompat.setShowAsAction(item, //MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | //MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        //  MenuItemCompat.setActionView(item, searchView);
        // These lines are deprecated in API 26 use instead
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
        item.actionView = searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        searchView.setOnClickListener(object : OnClickListener() {
            fun onClick(v: View?) {}
        }
        )
    }*/

/*    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)


        val searchItem = menu.findItem(R.id.app_bar_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Search View Hint"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                // task HERE
                return false
            }

        })

    }*/




    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.main_menu, menu)

        val searchItem: MenuItem? = menu?.findItem(R.id.app_bar_search)
       // val searchManager = context?.let { ContextCompat.getSystemService(it,Service.SEARCH_SERVICE::class.java) } as SearchManager

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

/*        searchItem?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                TODO("Not yet implemented")
            }

        })*/
        super.onCreateOptionsMenu(menu, inflater)
    }
}
