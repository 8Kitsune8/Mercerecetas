/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.kitsu.mercerecetas.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import app.kitsu.mercerecetas.database.Ingredient
import app.kitsu.mercerecetas.database.Recipe
import app.kitsu.mercerecetas.database.RecipeDatabase
import app.kitsu.mercerecetas.database.RecipeIngredientQuantity
import app.kitsu.mercerecetas.utils.INGREDIENT_DATA_FILENAME
import app.kitsu.mercerecetas.utils.RECIPE_DATA_FILENAME
import app.kitsu.mercerecetas.utils.REC_INGR_QTTY_DATA_FILENAME
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class IngredientDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {



    override suspend fun doWork(): Result = coroutineScope {
        try {
            //loadJson(RECIPE_DATA_FILENAME)
            loadJson(INGREDIENT_DATA_FILENAME)
            //loadJson(REC_INGR_QTTY_DATA_FILENAME)
        } catch (ex: Exception) {
            Log.e(WORK_NAME, "Error seeding database", ex)
            Result.failure()
        }
    }

    suspend fun loadJson(filename: String) : ListenableWorker.Result{
        applicationContext.assets.open(filename).use { inputStream ->
            JsonReader(inputStream.reader()).use { jsonReader ->
                val database = RecipeDatabase.getInstance(applicationContext)
                when(filename) {
                    RECIPE_DATA_FILENAME -> {
                        val type = object : TypeToken<List<Recipe>>() {}.type
                        val list: List<Recipe> = Gson().fromJson(jsonReader, type)

                        withContext(Dispatchers.IO) {
                            database.recipeDatabaseDao.insertAll(list)
                        }
                    }
                    INGREDIENT_DATA_FILENAME -> {
                        val type = object : TypeToken<List<Ingredient>>() {}.type
                        val list: List<Ingredient> = Gson().fromJson(jsonReader, type)
                        withContext(Dispatchers.IO) {
                            database.ingredientDao.insertAll(list)
                        }
                    }

                    REC_INGR_QTTY_DATA_FILENAME -> {
                        val type = object : TypeToken<List<RecipeIngredientQuantity>>() {}.type
                        val list: List<RecipeIngredientQuantity> = Gson().fromJson(jsonReader, type)
                        withContext(Dispatchers.IO) {
                            database.recipeIngredientQttyDao.insertAll(list)
                        }
                    }
                }
               return Result.success()
            }
        }
    }

    companion object {
        val WORK_NAME = IngredientDatabaseWorker::class.java.simpleName
    }
}