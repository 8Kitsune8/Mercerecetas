package app.kitsu.mercerecetas.ui.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.kitsu.mercerecetas.database.Recipe

class DetailViewModelFactory(
    private val recipe: Recipe,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(recipe, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
