package app.kitsu.mercerecetas.database

import androidx.lifecycle.LiveData
import androidx.room.*

enum class RecipeFilter(val value: String) {
    SHOW_MEAT("meat"),
    SHOW_FISH("fish"),
    SHOW_RICE("rice"),
    SHOW_PASTA("pasta"),
    SHOW_VEGETABLES("vegetables"),
    SHOW_ALL("all")
}

@Dao
interface RecipeDatabaseDao {

    @Insert
    fun insert(recipe: Recipe)

    @Update
    fun update(recipe: Recipe)

    @Query("SELECT * from recipe_table WHERE recipeId = :key")
    fun get(key: Long): Recipe?

    @Query("SELECT * FROM recipe_table ORDER BY recipeId DESC" )
    fun getAllRecipes(): LiveData<List<Recipe>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(recipes: List<Recipe>)

    @Query("SELECT * FROM recipe_table WHERE type = :filter")
    fun getFiltered(filter: String): List<Recipe>

}