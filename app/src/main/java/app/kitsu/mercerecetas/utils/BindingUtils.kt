package app.kitsu.mercerecetas.utils

import android.os.Build
import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import app.kitsu.mercerecetas.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.io.File




@BindingAdapter("imageFromUri")
fun bindImageFromUrl(view: ImageView, imageUri: String?) {
    if (!imageUri.isNullOrEmpty()) {
        Glide.with(view.context)
            .load(
                if(imageUri.contains("ic_")){
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
fun TextView.setSleepDurationFormatted(item: Int) {
    item?.let {
        val formText = context.resources.getString(
        R.string.display_recipe_time,
        convertDurationToFormatted(it, context.resources)
    )
        text = Html.fromHtml(formText, Html.FROM_HTML_MODE_LEGACY)
    }
}


    //using binding adapter to initialise RecipeAdapter with the list of Recipe objects.
// This will make automatically observe the LiveData for this list in RecyclerView
/*@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, properties: List<Recipe>?){
    val adapter = recyclerView.adapter as RecipeAdapter
    adapter.submitList(properties)
}*/



