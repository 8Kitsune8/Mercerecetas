package app.kitsu.mercerecetas.ui.shopping

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import app.kitsu.mercerecetas.database.RecipeIngredientQttyDao
import app.kitsu.mercerecetas.database.RecipeIngredientQuantity

class ShoppingViewModel(recipes: LongArray, dataSource: RecipeIngredientQttyDao, application: Application) : AndroidViewModel(application) {

    /**
     * Hold a reference to RecipeDatabase via RecipeDatabaseDao.
     */
    val database = dataSource

   var ingredients: LiveData<List<RecipeIngredientQuantity>>


    init {
        ingredients = getFilteredRecipesIngredients(recipes)
    }

    private  fun getFilteredRecipesIngredients(recipesId: LongArray): LiveData<List<RecipeIngredientQuantity>> {

           return database.getFilteredByRecipe(recipesId.toList())

    }
}
