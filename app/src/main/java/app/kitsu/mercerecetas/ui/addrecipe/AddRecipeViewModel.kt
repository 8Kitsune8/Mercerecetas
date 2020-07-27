package app.kitsu.mercerecetas.ui.addrecipe

import android.app.Application
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.kitsu.mercerecetas.R
import app.kitsu.mercerecetas.database.Recipe
import app.kitsu.mercerecetas.database.RecipeDatabaseDao
import kotlinx.android.synthetic.main.fragment_add_recipe.view.*
import kotlinx.coroutines.*

class AddRecipeViewModel(
    dataSource: RecipeDatabaseDao,
    application: Application
) : ViewModel() {

    /**
     * Hold a reference to RecipeDatabase via RecipeIngredientQttyDao.
     */
    val database = dataSource

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


    private val _text = MutableLiveData<String>().apply {
        value = "This is add recipe Fragment"
    }
    val text: LiveData<String> = _text


    fun createRecipe(view: View){
        var recipe : Recipe = Recipe()
        view?.let { recipe.name = view.textInputNewRecipeTitle.text.toString()
            val typeId = view.recipeTypeRadioGroup as RadioGroup

            recipe.type = when(typeId.checkedRadioButtonId) {
                R.id.radioButtonMeat -> "meat"
                R.id.radioButtonFish -> "fish"
                R.id.radioButtonRice -> "rice"
                R.id.radioButtonPasta -> "pasta"
                R.id.radioButtonVeget -> "vegetables"

                else -> "meat"

            }
            val modeId = view.recipeModeRadioGroup as RadioGroup
            recipe.cookMode = when(modeId.checkedRadioButtonId) {
                R.id.radioButtonSteam -> "Vapor"
                R.id.radioButtonCasserole -> "Cazuela"
                R.id.radioButtonPottage -> "Potaje"
                R.id.radioBTypeRice -> "Rice"
                R.id.radioButtonCake -> "Pastel"

                else -> "Vapor"

            }
            recipe.time = view.editTextTime.text.toString().toInt()

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