package app.kitsu.mercerecetas.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(indices = arrayOf(Index(value = ["recipeId"])), tableName = "recipe_table")
data class Recipe (
    @PrimaryKey(autoGenerate = true)
    var recipeId: Long = 0L,

    @ColumnInfo(name = "title")
    var name: String = "",

    @ColumnInfo(name = "img_uri")
    var imgUri: String = "",

    @ColumnInfo(name = "is_favorite")
    var favorite: Boolean = false,

    @ColumnInfo(name = "recipe_tips")
    var note: String = "",

    @ColumnInfo(name = "cook_mode")
    var cookMode: String = "",

    @ColumnInfo(name = "type")
    var type: String = "",

    @ColumnInfo(name = "time")
    var time: Int = 15

) : Parcelable