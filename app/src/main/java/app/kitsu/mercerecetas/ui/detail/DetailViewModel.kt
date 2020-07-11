package app.kitsu.mercerecetas.ui.detail

import android.app.Application
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import app.kitsu.mercerecetas.R
import app.kitsu.mercerecetas.database.Recipe
import app.kitsu.mercerecetas.database.RecipeIngredientQttyDao
import app.kitsu.mercerecetas.database.RecipeIngredientQuantity
import kotlinx.coroutines.*

class DetailViewModel(recipe: Recipe,dataSource: RecipeIngredientQttyDao, app: Application) : AndroidViewModel(app) {

    /**
     * Hold a reference to RecipeDatabase via RecipeIngredientQttyDao.
     */
    val database = dataSource

    private val _selectedRecipe = MutableLiveData<Recipe>()
    val selectedRecipe: LiveData<Recipe>
        get() = _selectedRecipe

    private val _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title

    var ingredients: LiveData<List<RecipeIngredientQuantity>>
 /*   private val _ingredients = MutableLiveData<List<RecipeIngredientQuantity>>()
    val ingredients: LiveData<List<RecipeIngredientQuantity>>
    get() = _ingredients*/



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


    init {
        _selectedRecipe.value = recipe
       // uiScope.launch {
         ingredients = getIngredients(listOf(recipe.recipeId))
        //}
    }




    @RequiresApi(Build.VERSION_CODES.N)
    val displayRecipeMode = Transformations.map(selectedRecipe) { recipe ->
        val escapedCookMode: String = TextUtils.htmlEncode(recipe.cookMode)

        val text= app.applicationContext.getString(R.string.display_recipe_mode, escapedCookMode)
        Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)

    }


    private fun getIngredients(recipeIds: List<Long>): LiveData<List<RecipeIngredientQuantity>> {
      //  withContext(Dispatchers.IO){
            //ingredients =
              return  database.getFilteredByRecipe(recipeIds)
        //}


    }

    fun updateActionBarTitle(title: String?) = _title.postValue(title)

}