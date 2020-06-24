package app.kitsu.mercerecetas.ui.shopping

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.kitsu.mercerecetas.database.Ingredient
import app.kitsu.mercerecetas.database.IngredientDao
import app.kitsu.mercerecetas.database.Recipe
import app.kitsu.mercerecetas.database.RecipeIngredientQttyDao

class ShoppingViewModelFactory (
        private val recipes: LongArray,
        private val dataSource: RecipeIngredientQttyDao ,
        private val application: Application
    ) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ShoppingViewModel::class.java)) {
                return ShoppingViewModel(recipes,dataSource, application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }


