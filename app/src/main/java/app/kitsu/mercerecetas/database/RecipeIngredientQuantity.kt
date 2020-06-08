package app.kitsu.mercerecetas.database

import androidx.room.*


@Entity(tableName = "recipe_ingredient_quantity_table",
        foreignKeys = arrayOf(ForeignKey(entity = Recipe::class, parentColumns = ["recipeId"], childColumns = ["recipe_id"])))
data class RecipeIngredientQuantity (

    @PrimaryKey(autoGenerate = true)
    var recIngQttyId: Long = 0L,


    @ColumnInfo(name = "recipe_id")
    var recipeId: Long = 0,


    @ColumnInfo(name = "ingredient_quantity")
    var ingredientQtty: Int = 0,

    @Embedded(prefix = "ing_") val ingredient: Ingredient?


)