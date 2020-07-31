package app.kitsu.mercerecetas.ui.addrecipe

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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

        binding.RadioGroupMode.setOnClickListener {
            if(it != null) {
                closeKeyBoard()
            }
        }

        binding.recipeTypeRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            closeKeyBoard()
        }


        binding.recipeModeRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            closeKeyBoard()
        }

        binding.editTextTime.setOnEditorActionListener { v, actionId, event ->
            if (actionId == KeyEvent.KEYCODE_ENDCALL) {
                closeKeyBoard()
                true
            }
           else false
        }

        binding.createButton.setOnClickListener {
            addRecipeViewModel.createRecipe(binding.root)
            addRecipeViewModel.onRecipeCreated()
        }

        addRecipeViewModel.navigateToHome.observe(viewLifecycleOwner, Observer {
            if (it){ this.findNavController().navigate(AddRecipeFragmentDirections.actionNavigationAddrecipeToNavigationHome())
                addRecipeViewModel.displayHomeComplete()
            }
        })

        return binding.root;
    }


    private fun closeKeyBoard() {
        val view = this.activity?.currentFocus
        if (view != null) {
            val imm = this.activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
