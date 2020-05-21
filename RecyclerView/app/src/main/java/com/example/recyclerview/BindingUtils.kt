package com.example.recyclerview

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("setImageByUrl")
fun setImageByUrl(imageView: ImageView, imageUrl: String?) {
    imageUrl?.let {
        var imageUri = imageUrl.toUri().buildUpon().scheme("https").build()

        Glide.with(imageView.context)
            .load(imageUri)
            .apply(
                RequestOptions().placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(imageView)

    }
}
@BindingAdapter("setListOfStudent")
fun setListOfStudent(recyclerView: RecyclerView, list: List<Student>) {
    val adapter = recyclerView.adapter as? StudentAdapter
    adapter?.submitList(list)
}