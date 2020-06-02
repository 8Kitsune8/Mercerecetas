package app.kitsu.mercerecetas.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "recipe_ingredient_quantity_table",
        foreignKeys = arrayOf(ForeignKey(entity = Recipe::class, parentColumns = ["recipeId"], childColumns = ["recipe_id"]),
            ForeignKey(entity = Ingredient::class, parentColumns = ["i_id"], childColumns = ["ingredient_name"])))
data class RecipeIngredientQuantity (

    @PrimaryKey(autoGenerate = true)
    var recIngQttyId: Long = 0L,


    @ColumnInfo(name = "recipe_id")
    var recipeId: Long = 0,

    @ColumnInfo(name = "ingredient_name")
    var ingredientName: String = "",

    @ColumnInfo(name = "ingredient_quantity")
    var ingredientQtty: Int = 0
    )