package app.kitsu.mercerecetas.ui.home

import android.app.Application
import androidx.lifecycle.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQueryBuilder
import app.kitsu.mercerecetas.database.*
import kotlinx.coroutines.*


class HomeViewModel(
    dataSource: RecipeDatabaseDao,
    application: Application
) : ViewModel() {


    /**
     * Hold a reference to RecipeDatabase via RecipeDatabaseDao.
     */
    val database = dataSource


    lateinit var recipes : LiveData<List<Recipe>>

    private var _filteredRecipes = MutableLiveData<List<Recipe>>()
    val filteredrecipes : LiveData<List<Recipe>>
    get() = _filteredRecipes


    /** Coroutine variables */

    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private var viewModelJob = Job()
    var liveDataMerger: MediatorLiveData<List<Recipe>> = MediatorLiveData<List<Recipe>>()
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
       recipes = getAllRecipes()

       liveDataMerger.addSource(recipes,  Observer { value ->
           liveDataMerger.setValue(value) } )

       liveDataMerger.addSource(filteredrecipes,  Observer { value ->
           liveDataMerger.setValue(value) } )

       uiScope.launch {
           generateBackup()
       }
   }


    private var filterList : ArrayList<Recipe>? = null

    private val _navigateToSelectedRecipe = MutableLiveData<Recipe>()
    val navigateToSelectedRecipe: LiveData<Recipe>
    get() = _navigateToSelectedRecipe


    fun displayRecipeDetails(recipe: Recipe) {
        _navigateToSelectedRecipe.value = recipe
    }


    fun displayRecipeDetailsComplete() {
        _navigateToSelectedRecipe.value = null
    }



    fun updateRecipeList(filter: RecipeFilter){

       if(filter == RecipeFilter.SHOW_ALL) {
           liveDataMerger.setValue(recipes.value)
       } else {
           uiScope.launch {
           _filteredRecipes.value =  getFilteredRecipes(filter)
           }
       }
    }

    private fun getAllRecipes(): LiveData<List<Recipe>> {
        return database.getAllRecipes()
    }

    private suspend fun getFilteredRecipes(filter: RecipeFilter): List<Recipe> {
        return withContext(Dispatchers.IO) {
            database.getFiltered(filter.value)
        }
    }

    private suspend fun generateBackup() {
        withContext(Dispatchers.IO){
            val query = SimpleSQLiteQuery("pragma wal_checkpoint(full)")
            database.generateBackup(query)
        }
    }
}