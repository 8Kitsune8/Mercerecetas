package app.kitsu.mercerecetas.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.kitsu.mercerecetas.database.Recipe

class DetailViewModel(recipe: Recipe, app: Application) : AndroidViewModel(app) {

    private val _selectedRecipe = MutableLiveData<Recipe>()
    val selectedRecipe: LiveData<Recipe>
        get() = _selectedRecipe

    init {
        _selectedRecipe.value = recipe
    }

}