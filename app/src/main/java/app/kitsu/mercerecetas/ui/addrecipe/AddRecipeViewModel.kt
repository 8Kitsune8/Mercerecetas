package app.kitsu.mercerecetas.ui.addrecipe

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.kitsu.mercerecetas.database.RecipeDatabaseDao

class AddRecipeViewModel(
    dataSource: RecipeDatabaseDao,
    application: Application
) : ViewModel() {


    private val _text = MutableLiveData<String>().apply {
        value = "This is add recipe Fragment"
    }
    val text: LiveData<String> = _text
}