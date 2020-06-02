package app.kitsu.mercerecetas.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RecipeIngredientQttyDao {

    @Insert
    fun insert(recipeIngredientQuantity: RecipeIngredientQuantity)

    @Update
    fun update(recipeIngredientQuantity: RecipeIngredientQuantity)

    @Query("SELECT * from recipe_ingredient_quantity_table WHERE recIngQttyId = :key")
    fun get(key: Long): RecipeIngredientQuantity?

    @Query("SELECT recIngQttyId, recipe_id, ingredient_name, SUM(ingredient_quantity) as ingredient_quantity FROM recipe_ingredient_quantity_table " +
            "WHERE recipe_id IN (:filter) " +
            "GROUP BY ingredient_name" )
    fun getFilteredByRecipe(filter: List<Long>): LiveData<List<RecipeIngredientQuantity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(recipeIngredientQuantity: List<RecipeIngredientQuantity>)

}