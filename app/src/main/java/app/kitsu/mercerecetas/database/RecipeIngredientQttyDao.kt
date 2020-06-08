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

    @Query("SELECT recIngQttyId, recipe_id, SUM(ingredient_quantity) as ingredient_quantity, ing_i_id, ing_group , ing_type FROM recipe_ingredient_quantity_table " +
//            "INNER JOIN ingredient_table ON ingredient_table.i_id = recipe_ingredient_quantity_table.ingredient_name " +
            "WHERE recipe_id IN (:filter) " +
            "GROUP BY ing_i_id " +
            "ORDER BY recIngQttyId ASC")
    fun getFilteredByRecipe(filter: List<Long>): LiveData<List<RecipeIngredientQuantity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(recipeIngredientQuantity: List<RecipeIngredientQuantity>)

}