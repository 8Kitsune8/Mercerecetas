package app.kitsu.mercerecetas.ui.addrecipe

import android.app.Application

import android.view.View

import android.widget.RadioGroup
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import app.kitsu.mercerecetas.R
import app.kitsu.mercerecetas.database.Recipe
import app.kitsu.mercerecetas.database.RecipeDatabaseDao
import app.kitsu.mercerecetas.database.RecipeFilter
import kotlinx.android.synthetic.main.fragment_add_recipe.view.*
import kotlinx.coroutines.*

class AddRecipeViewModel(
    dataSource: RecipeDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    /**
     * Hold a reference to RecipeDatabase via RecipeIngredientQttyDao.
     */
    val database = dataSource


    val app = application

    /** Coroutine variables */

    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private var viewModelJob = Job()
    // var liveDataMerger: MediatorLiveData<List<Recipe>> = MediatorLiveData<List<Recipe>>()
    /**
     * A [CoroutineScope] keeps track of all coroutines started by this ViewModel.
     *
     * Because we pass it [viewModelJob], any coroutine started in this uiScope can be cancelled
     * by calling `viewModelJob.cancel()`
     *
     * By default, all coroutines started in uiScope will launch in [Dispatchers.Main] which is
     * the main thread on Android. This is a sensible default because most coroutines started by
     * a [ViewModel] update the UI after performing some processing.
     */
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)



    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome


    fun displayHome() {
        _navigateToHome.value = true
    }


    fun displayHomeComplete() {
        _navigateToHome.value = false
    }

    fun onRecipeCreated() {
        Toast.makeText(app,"Receta Creada!", Toast.LENGTH_LONG).show()
        displayHome()
    }

    fun createRecipe(view: View) {
        var recipe : Recipe = Recipe()
        view?.let { recipe.name = it.textInputNewRecipeTitle.text.toString()
            val typeId = it.recipeTypeRadioGroup as RadioGroup

            recipe.type = when(typeId.checkedRadioButtonId) {
                R.id.radioButtonMeat -> RecipeFilter.SHOW_MEAT.value
                R.id.radioButtonFish -> RecipeFilter.SHOW_FISH.value
                R.id.radioButtonRice -> RecipeFilter.SHOW_RICE.value
                R.id.radioButtonPasta -> RecipeFilter.SHOW_PASTA.value
                R.id.radioButtonVeget -> RecipeFilter.SHOW_VEGETABLES.value

                else -> RecipeFilter.SHOW_MEAT.value

            }
            val modeId = it.recipeModeRadioGroup as RadioGroup
            recipe.cookMode = when(modeId.checkedRadioButtonId) {
                R.id.radioButtonSteam ->  app.applicationContext.resources.getString(R.string.vapor)
                R.id.radioButtonCasserole -> app.resources.getString(R.string.cazuela)
                R.id.radioButtonPottage -> app.resources.getString(R.string.potaje)
                R.id.radioBTypeRice -> app.resources.getString(R.string.arroz)
                R.id.radioButtonCake -> app.resources.getString(R.string.pastel)

                else -> R.string.vapor.toString()

            }
            recipe.time = it.editTextTime.text.toString().toInt()

            uiScope.launch {
                setRecipes(recipe)
            }
        }
    }


    private suspend fun setRecipes(recipe: Recipe) {
        return withContext(Dispatchers.IO) {
            database.insert(recipe)
        }
    }
}