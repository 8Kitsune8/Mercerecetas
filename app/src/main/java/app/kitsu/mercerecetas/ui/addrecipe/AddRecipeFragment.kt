package app.kitsu.mercerecetas.ui.addrecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment

import androidx.lifecycle.ViewModelProvider

import app.kitsu.mercerecetas.database.RecipeDatabase
import app.kitsu.mercerecetas.databinding.FragmentAddRecipeBinding

class AddRecipeFragment : Fragment() {

    private lateinit var dashboardViewModel: AddRecipeViewModel
    private lateinit var binding: FragmentAddRecipeBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        binding = FragmentAddRecipeBinding.inflate(inflater,container, false)

        val application = requireNotNull(this.activity).application

        // Create an instance of the ViewModel Factory.
        val dataSource = RecipeDatabase.getInstance(application).recipeDatabaseDao

        val dashboardViewModelFactory = AddRecipeViewModelFactory(dataSource,application)

        dashboardViewModel =
               ViewModelProvider(this, dashboardViewModelFactory).get(AddRecipeViewModel::class.java)

        binding.dashboardViewModel = dashboardViewModel


        return binding.root;
    }
}
