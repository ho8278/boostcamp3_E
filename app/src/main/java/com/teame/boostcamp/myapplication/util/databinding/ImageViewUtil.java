package com.teame.boostcamp.myapplication.util.databinding;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

public class ImageViewUtil {

    @BindingAdapter({"imgUrl", "error"})
    public static void loadImage(ImageView imageView, String uri, Drawable error) {
        RequestOptions requestOptions = new RequestOptions().placeholder(error).error(error);

        Glide.with(imageView)
                .load(uri)
                .apply(requestOptions)
                .into(imageView);
    }

    @BindingAdapter({"imgUrl", "error", "rounded"})
    public static void loadImage(ImageView imageView,
                                 String uri,
                                 Drawable error,
                                 int rounded) {
        RequestOptions requestOptions = new RequestOptions().placeholder(error).error(error);

        Glide.with(imageView)
                .load(uri)
                .apply(requestOptions)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(rounded)))
                .into(imageView);
    }


    @BindingAdapter({"imgUrl","imgCustomUrl", "error", "rounded"})
    public static void loadImage(ImageView imageView,
                                 String uri,
                                 String url,
                                 Drawable error,
                                 int rounded) {
        RequestOptions requestOptions = new RequestOptions().placeholder(error).error(error);


        if(uri != null){
            Glide.with(imageView)
                    .load(uri)
                    .apply(requestOptions)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(rounded)))
                    .into(imageView);
        }else{
            Glide.with(imageView)
                    .load(url)
                    .apply(requestOptions)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(rounded)))
                    .into(imageView);
        }

    }


    @BindingAdapter({"imgUrl", "placeHolder", "error"})
    public static void loadImage(ImageView imageView,
                                 String uri,
                                 Drawable placeholder,
                                 Drawable error) {
        RequestOptions requestOptions = new RequestOptions().placeholder(placeholder).error(error);

        Glide.with(imageView)
                .load(uri)
                .apply(requestOptions)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(4)))
                .into(imageView);
    }
}
