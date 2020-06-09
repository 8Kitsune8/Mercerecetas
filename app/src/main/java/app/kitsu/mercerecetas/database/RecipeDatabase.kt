package app.kitsu.mercerecetas.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

import app.kitsu.mercerecetas.utils.DATABASE_NAME
import app.kitsu.mercerecetas.workers.IngredientDatabaseWorker
import app.kitsu.mercerecetas.workers.RecipeDatabaseWorker
import app.kitsu.mercerecetas.workers.RecipeIngrQttyDatabaseWorker


@Database(entities = [Recipe::class, Ingredient::class, RecipeIngredientQuantity::class], version = 1, exportSchema = true)
abstract class RecipeDatabase : RoomDatabase() {

    abstract val recipeDatabaseDao: RecipeDatabaseDao
    abstract val ingredientDao: IngredientDao
    abstract val recipeIngredientQttyDao: RecipeIngredientQttyDao

    companion object {

        @Volatile
        private var INSTANCE: RecipeDatabase? = null

        fun getInstance(context: Context): RecipeDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = buildDatabase(context)
                    INSTANCE = instance
                }
                return instance
            }
        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): RecipeDatabase {
            return Room.databaseBuilder(context, RecipeDatabase::class.java, DATABASE_NAME)
                .createFromAsset(DATABASE_NAME)
                .build()

             /*  return Room.databaseBuilder(context, RecipeDatabase::class.java, DATABASE_NAME)
               .addCallback(object : RoomDatabase.Callback() {
                   override fun onCreate(db: SupportSQLiteDatabase) {
                       super.onCreate(db)
                   val request = OneTimeWorkRequestBuilder<RecipeDatabaseWorker>().build()
                       WorkManager.getInstance(context).enqueue(request)
                       val requestIngr = OneTimeWorkRequestBuilder<IngredientDatabaseWorker>().build()
                       WorkManager.getInstance(context).enqueue(requestIngr)
                       val requestQtty = OneTimeWorkRequestBuilder<RecipeIngrQttyDatabaseWorker>().build()
                       WorkManager.getInstance(context).enqueue(requestQtty)
                    }
                })
                .build()*/
        }
    }
}