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
import androidx.work.WorkerParameters
import app.kitsu.mercerecetas.database.Recipe
import app.kitsu.mercerecetas.database.RecipeDatabase
import app.kitsu.mercerecetas.utils.RECIPE_DATA_FILENAME
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader

import kotlinx.coroutines.coroutineScope

class RecipeDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {



    override suspend fun doWork(): Result = coroutineScope {
        try {
            applicationContext.assets.open(RECIPE_DATA_FILENAME).use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val recipeType = object : TypeToken<List<Recipe>>() {}.type
                    val recipeList: List<Recipe> = Gson().fromJson(jsonReader, recipeType)

                    val database = RecipeDatabase.getInstance(applicationContext)
                    database.recipeDatabaseDao.insertAll(recipeList)

                    Result.success()
                }
            }
        } catch (ex: Exception) {
            Log.e(WORK_NAME, "Error seeding database", ex)
            Result.failure()
        }
    }

    companion object {
        val WORK_NAME = RecipeDatabaseWorker::class.java.simpleName
    }
}