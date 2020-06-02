package app.kitsu.mercerecetas.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface IngredientDao {

    @Insert
    fun insert(ingredient: Ingredient)

    @Update
    fun update(ingredient: Ingredient)

    @Query("SELECT * from ingredient_table WHERE i_id = :key")
    fun get(key: String): Ingredient?

    @Query("SELECT * FROM ingredient_table" )
    fun getAllIngredients(): LiveData<List<Ingredient>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(ingredients: List<Ingredient>)

    @Query("SELECT * FROM ingredient_table WHERE type = :filter")
    fun getFiltered(filter: String): List<Ingredient>
}