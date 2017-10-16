package demo.wattpad.lab.com.wattpadfeeddemo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestListener;

import demo.wattpad.lab.com.wattpadfeeddemo.R;

/**
 * Created by lab on 2017-10-16.
 */

public class GlideLoader {
    public enum Shape {
        Rect,
        RoundedRect,
        Cirlcle
    }

    public static GlideLoader with(ImageView imageView, String url) {
        return new GlideLoader(imageView, url, 0);
    }

    public static GlideLoader with(ImageView imageView, String url, @DrawableRes int defaultDrawableId) {
        return new GlideLoader(imageView, url, defaultDrawableId);
    }

    public static GlideLoader with(ImageView imageView, @DrawableRes int drawableId) {
        return new GlideLoader(imageView, drawableId);
    }

    public static GlideLoader with(Context context, String url, int width, int height) {
        return new GlideLoader(context, url, width, height);
    }

    private DrawableTypeRequest bitmapReqeust;
    private String url;
    private @DrawableRes int drawableResId = 0;
    private ImageView imageView;
    private Context context;
    private int width;
    private int height;
    private Shape shape = Shape.Rect;

    private GlideLoader(ImageView imageView, String url, @DrawableRes int defaultDrawableId) {
        this.imageView = imageView;
        this.context = imageView.getContext();
        this.url = url;

        if(defaultDrawableId==0) {
            defaultDrawableId = R.drawable.empty;
        }

        RequestManager requestManager = Glide.with(imageView.getContext());
        bitmapReqeust = url!=null&&url.length()>0 ? requestManager.load(url) : requestManager.load(defaultDrawableId);
        bitmapReqeust.crossFade();
        bitmapReqeust.error(defaultDrawableId);
    }

    private GlideLoader(ImageView imageView, @DrawableRes int drawableResId) {
        this.imageView = imageView;
        this.context = imageView.getContext();
        this.drawableResId = drawableResId;

        bitmapReqeust = Glide.with(imageView.getContext()).load(drawableResId);
        bitmapReqeust.crossFade();
    }

    private GlideLoader(Context context, String url, int width, int height) {
        this.url = url;
        this.context = context;
        this.width = width;
        this.height = height;

        bitmapReqeust = Glide.with(context).load(url);
    }

    public GlideLoader diskCacheStrategy(DiskCacheStrategy cacheStrategy) {
        this.bitmapReqeust.diskCacheStrategy(cacheStrategy);
        return this;
    }

    public GlideLoader shape(Shape shape) {
        this.shape = shape;

        return this;
    }

    public GlideLoader listener(RequestListener requestListener) {
        bitmapReqeust.listener(requestListener);
        return this;
    }

    public void load() {
        if(shape==Shape.Cirlcle) {
            if(context!=null) {
                bitmapReqeust.transform(new CenterCrop(context), new CircleTransform(context));
            }
        }else {
            bitmapReqeust.centerCrop();
        }

        if(imageView!=null) {
            if(imageView.getWidth()>0 && imageView.getHeight()>0) {
                bitmapReqeust.into(imageView);
            }else {
                bitmapReqeust.into(imageView);
            }
        }else if(width>0 && height>0){
            bitmapReqeust.into(width, height);
        }
    }

    public static class CircleTransform extends BitmapTransformation {
        public CircleTransform(Context context) {
            super(context);
        }

        @Override protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            // TODO this could be acquired from the pool too
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override public String getId() {
            return getClass().getName();
        }
    }
}
