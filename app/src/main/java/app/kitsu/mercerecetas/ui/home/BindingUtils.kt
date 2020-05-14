package app.kitsu.mercerecetas.ui.home

import android.widget.ImageView
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

    //using binding adapter to initialise RecipeAdapter with the list of Recipe objects.
// This will make automatically observe the LiveData for this list in RecyclerView
/*@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, properties: List<Recipe>?){
    val adapter = recyclerView.adapter as RecipeAdapter
    adapter.submitList(properties)
}*/



