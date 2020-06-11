package app.kitsu.mercerecetas.utils

import android.os.Build
import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import app.kitsu.mercerecetas.R
import app.kitsu.mercerecetas.database.Recipe
import app.kitsu.mercerecetas.database.RecipeIngredientQuantity
import app.kitsu.mercerecetas.ui.detail.IngredientsQttyAdapter
import app.kitsu.mercerecetas.ui.home.RecipeAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.io.File




@BindingAdapter("imageFromUri")
fun bindImageFromUrl(view: ImageView, imageUri: String?) {
    if (!imageUri.isNullOrEmpty()) {
        Glide.with(view.context)
            .load(
                if(imageUri.contains("dr_")){
                    view.context.resources.getIdentifier(imageUri,"drawable", view.context.packageName)
                }else File(imageUri)
            )
            //this is to manage different images in case of loading or broken image
                .apply(
                    RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image))
            //.transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }


}


@RequiresApi(Build.VERSION_CODES.N)
@BindingAdapter("recipeDurationFormatted")
fun TextView.setRecipeDurationFormatted(item: Int) {
    item?.let {
        val formText = context.resources.getString(
        R.string.display_recipe_time,
        convertDurationToFormatted(it, context.resources)
    )
        text = Html.fromHtml(formText, Html.FROM_HTML_MODE_LEGACY)
    }
}


@RequiresApi(Build.VERSION_CODES.N)
@BindingAdapter("ingredientQttyFormatted")
fun TextView.setIngredientQttyFormatted(recIngQtty: RecipeIngredientQuantity) {
    if (recIngQtty != null){
        text = "${recIngQtty.ingredientQtty}  ${recIngQtty.ingredient?.type}"
    }
}

//@BindingAdapter("ingredientWithDots")

    //using binding adapter to initialise RecipeAdapter with the list of Recipe objects.
// This will make automatically observe the LiveData for this list in RecyclerView
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, properties: List<Recipe>?) {
        val adapter = recyclerView.adapter as RecipeAdapter
        if (properties != null) {
            // adapter.data = properties
            adapter.submitList(properties)
            //This is to setup current full list used to filter it
            adapter.setCurrentFullList(properties)
        }
    }

@BindingAdapter("listIngredientsData")
fun bindIngrRecyclerView(recyclerView: RecyclerView, ingredients: List<RecipeIngredientQuantity>?){
    val adapter = recyclerView.adapter as IngredientsQttyAdapter
    if (ingredients != null) {
        //adapter.data = ingredients
        adapter.submitList(ingredients)
    }
}



