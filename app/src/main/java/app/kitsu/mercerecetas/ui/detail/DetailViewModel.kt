package app.kitsu.mercerecetas.ui.detail

import android.app.Application
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import app.kitsu.mercerecetas.R
import app.kitsu.mercerecetas.database.Recipe

class DetailViewModel(recipe: Recipe, app: Application) : AndroidViewModel(app) {

    private val _selectedRecipe = MutableLiveData<Recipe>()
    val selectedRecipe: LiveData<Recipe>
        get() = _selectedRecipe

    private val _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title

    init {
        _selectedRecipe.value = recipe
    }

    @RequiresApi(Build.VERSION_CODES.N)
    val displayRecipeMode = Transformations.map(selectedRecipe) { recipe ->
        val escapedCookMode: String = TextUtils.htmlEncode(recipe.cookMode)

        val text= app.applicationContext.getString(R.string.display_recipe_mode, escapedCookMode)
        Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)

    }

    fun updateActionBarTitle(title: String?) = _title.postValue(title)
}