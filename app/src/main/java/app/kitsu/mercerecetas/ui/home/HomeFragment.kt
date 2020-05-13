package app.kitsu.mercerecetas.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager

import app.kitsu.mercerecetas.R
import app.kitsu.mercerecetas.database.RecipeDatabase
import app.kitsu.mercerecetas.databinding.FragmentHomeBinding
import app.kitsu.mercerecetas.databinding.ListItemRecipeBinding

class HomeFragment : Fragment() {



    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentHomeBinding.inflate(inflater,container, false)

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

       val adapter = RecipeAdapter()
        binding.recipeList.adapter = adapter
        homeViewModel.recipes.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.setLifecycleOwner(this)

        val manager = GridLayoutManager(activity, 2)
        binding.recipeList.layoutManager = manager

        return binding.root
    }
}
