package app.kitsu.mercerecetas.ui.addrecipe

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService

import androidx.fragment.app.Fragment

import androidx.lifecycle.ViewModelProvider
import app.kitsu.mercerecetas.R

import app.kitsu.mercerecetas.database.RecipeDatabase
import app.kitsu.mercerecetas.databinding.FragmentAddRecipeBinding
import kotlinx.android.synthetic.main.fragment_add_recipe.view.*

class AddRecipeFragment : Fragment() {

    private lateinit var addRecipeViewModel: AddRecipeViewModel
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

        addRecipeViewModel =
               ViewModelProvider(this, dashboardViewModelFactory).get(AddRecipeViewModel::class.java)

        binding.addRecipeViewModel = addRecipeViewModel

        binding.createButton.setOnClickListener {

                addRecipeViewModel.createRecipe(binding.root)

        }

        return binding.root;
    }
}
