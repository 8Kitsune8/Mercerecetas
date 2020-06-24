package app.kitsu.mercerecetas.ui.shopping

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.kitsu.mercerecetas.database.Ingredient
import app.kitsu.mercerecetas.database.IngredientDao
import app.kitsu.mercerecetas.database.RecipeIngredientQttyDao
import app.kitsu.mercerecetas.database.RecipeIngredientQuantity

class ShoppingViewModel(recipes: LongArray, dataSource: RecipeIngredientQttyDao, application: Application) : AndroidViewModel(application) {


    var _ingredients = MutableLiveData<List<RecipeIngredientQuantity>>()
    val ingredients: LiveData<List<RecipeIngredientQuantity>>
        get() = _ingredients

    init {
        val recipesId  = recipes
    }
}
