package app.kitsu.mercerecetas.utils

import android.app.Activity
import android.content.res.Resources
import androidx.work.ListenableWorker
import app.kitsu.mercerecetas.R
import app.kitsu.mercerecetas.database.Ingredient
import app.kitsu.mercerecetas.database.Recipe
import app.kitsu.mercerecetas.database.RecipeDatabase
import app.kitsu.mercerecetas.database.RecipeIngredientQuantity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

private val ONE_HOUR_MIN = TimeUnit.MINUTES.convert(1, TimeUnit.HOURS)

fun convertDurationToFormatted(time: Int, res: Resources): String {
    var duration = time
    var totalTimeString = StringBuilder()

    if (duration >= ONE_HOUR_MIN){
        val hours = TimeUnit.HOURS.convert(duration.toLong(), TimeUnit.MINUTES)

        duration -= (hours*ONE_HOUR_MIN).toInt()
        totalTimeString.append(" ").append(res.getString(R.string.hours_length, hours))
    }
    if( duration != 0) {
            totalTimeString.append(" ").append(res.getString(R.string.minutes_length, duration))
        }
    return totalTimeString.toString()
}
