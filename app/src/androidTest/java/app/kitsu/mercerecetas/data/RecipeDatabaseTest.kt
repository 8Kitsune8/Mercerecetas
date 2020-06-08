package app.kitsu.mercerecetas.data

import android.database.sqlite.SQLiteConstraintException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.room.CoroutinesRoom
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import app.kitsu.mercerecetas.database.*
import app.kitsu.mercerecetas.utils.OneTimeObserver
import kotlinx.coroutines.runBlocking

import org.junit.*
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RecipeDatabaseTest {

    private lateinit var recipeDao: RecipeDatabaseDao
    private lateinit var ingredientDao: IngredientDao
    private lateinit var recipeIngredientQttyDao: RecipeIngredientQttyDao
    private lateinit var db: RecipeDatabase

    private val recipeA = Recipe(1,"tortilla", "",true,"6 huevos")
    private val recipeB = Recipe(2,"spagetti", "",true,"400g")
    private val recipeC = Recipe(3,"fabada", "",true,"1 chorizo")

    private val ingredientA = Ingredient("Huevo","huevo","u")
    private val ingredientB = Ingredient("Patata","hortaliza","u")
    private val ingredientC = Ingredient("Alubias","legumbre","gr")

    private val recIngQttyA = RecipeIngredientQuantity(1, 1,6,ingredientA)
    private val recIngQttyB= RecipeIngredientQuantity(2, 1,4,ingredientB)
    private val recIngQttyC = RecipeIngredientQuantity(3, 2,1,ingredientA)
    private val recIngQttyD = RecipeIngredientQuantity(4, 3,300, ingredientC)

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
        ingredientDao = db.ingredientDao
        recipeIngredientQttyDao = db.recipeIngredientQttyDao
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
    fun addAndGetRecipetList(){
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

    @Test(expected = SQLiteConstraintException::class)
    fun checkForeingKeyConstraintEx(){
        runBlocking {
            recipeDao.insertAll((listOf(recipeA, recipeB, recipeC)))
            recipeIngredientQttyDao.insertAll(
                listOf(
                    recIngQttyA,
                    recIngQttyB,
                    recIngQttyC,
                    recIngQttyD
                )
            )

        }
    }

    @Test
    fun addAndGetIngredient() {
        runBlocking {
            recipeDao.insertAll((listOf(recipeA, recipeB, recipeC)))
            ingredientDao.insertAll(listOf(ingredientA, ingredientB, ingredientC))
        }
        ingredientDao.getAllIngredients().observeOnce { listIngr ->
            Assert.assertEquals(listOf<Ingredient>(ingredientA, ingredientB, ingredientC),listIngr)
        }
    }

    @Test
    fun addAndGetIngredientsQttiesFromRecipes(){
        runBlocking {
            recipeDao.insertAll((listOf(recipeA,recipeB,recipeC)))
            ingredientDao.insertAll(listOf(ingredientA,ingredientB,ingredientC))
            recipeIngredientQttyDao.insertAll(listOf(recIngQttyA,recIngQttyB,recIngQttyC,recIngQttyD))

            recipeIngredientQttyDao.getFilteredByRecipe(listOf(1,2,3)).observeOnce { listQtty ->
                Assert.assertEquals("Huevo", listQtty[1].ingredient?.name)
                Assert.assertEquals(7, listQtty[1].ingredientQtty)
                Assert.assertEquals("u", listQtty[1].ingredient?.type)
                Assert.assertEquals("Patata", listQtty[0].ingredient?.name)
                Assert.assertEquals(4, listQtty[0].ingredientQtty)
                Assert.assertEquals("gr", listQtty[2].ingredient?.type)
            }
        }
    }
}