package app.kitsu.mercerecetas.database


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "ingredient_table")
data class Ingredient (

    @PrimaryKey @ColumnInfo(name = "i_id")  var name: String = "",


    @ColumnInfo(name = "group")
    var group: String = "",

    @ColumnInfo(name = "type")
    var type: String = ""
    )
