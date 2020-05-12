package app.kitsu.mercerecetas.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.room.CoroutinesRoom
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import app.kitsu.mercerecetas.database.Recipe
import app.kitsu.mercerecetas.database.RecipeDatabase
import app.kitsu.mercerecetas.database.RecipeDatabaseDao
import app.kitsu.mercerecetas.utils.OneTimeObserver
import kotlinx.coroutines.runBlocking

import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RecipeDatabaseTest {

    private lateinit var recipeDao: RecipeDatabaseDao
    private lateinit var db: RecipeDatabase

    private val recipeA = Recipe(1,"tortilla", "",true,"6 huevos")
    private val recipeB = Recipe(2,"spagetti", "",true,"400g")
    private val recipeC = Recipe(3,"fabada", "",true,"1 chorizo")

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, RecipeDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        recipeDao = db.recipeDatabaseDao
    }


    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetAndUpdateAndGetByKey() {
        val recipe = Recipe()
        recipe.note = "note"
        val recipeByKey: Recipe
        recipeDao.insert(recipe)
        val recipe2 = recipeDao.get(1)
        Assert.assertEquals("note", recipe?.note)

        recipe2?.note = "mod note"
        recipeDao.update(recipe2!!)
        recipeByKey = recipeDao.get(recipe2!!.recipeId)!!
        Assert.assertEquals("mod note", recipeByKey?.note)
    }



    @Test
    @Throws(Exception::class)
    fun addAndGetNightList(){
        runBlocking {
            recipeDao.insertAll((listOf(recipeA,recipeB,recipeC)))
            val recipe = recipeDao.get(2)
            Assert.assertEquals(recipeB,recipe)
        }

        val nightList = recipeDao.getAllRecipes().observeOnce {
            Assert.assertEquals(3, it?.size)
        }
    }

    fun <T> LiveData<T>.observeOnce(onChangeHandler: (T) -> Unit) {
        val observer = OneTimeObserver(handler = onChangeHandler)
        observe(observer, observer)
    }
}