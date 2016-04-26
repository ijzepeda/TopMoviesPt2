package com.ijzepeda.topmoviespt2;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.widget.ImageView;

import com.squareup.picasso.Transformation;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Original source, and credits:
 * https://github.com/copolii/picasso_demo/blob/master/demo/src/main/java/ca/mahram/demo/picasso/xform/PaletteGeneratorTransformation.java
 */
public final class PaletteGeneratorTransformation implements Transformation {

    private static final Map<Bitmap, Palette> CACHE = new WeakHashMap<>();
    private final int numColors;

    public PaletteGeneratorTransformation() {
        this(0);
    }

    public PaletteGeneratorTransformation(final int c) {
        numColors = c;
    }

    @Override
    public Bitmap transform(final Bitmap source) {
        if (!CACHE.containsKey(source)) {
            final Palette palette = numColors > 0
                    ? Palette.generate(source, numColors)
                    : Palette.generate(source);
            CACHE.put(source, palette);
        }

        return source;
    }

    @Override
    public String key() {
        return getClass().getCanonicalName() + ":" + numColors;
    }

    public static abstract class Callback
            implements com.squareup.picasso.Callback {
        private final ImageView target;

        public Callback(final ImageView t) {
            target = t;
        }

        @Override
        public void onSuccess() {
            onPalette(CACHE.get(((BitmapDrawable) target.getDrawable()).getBitmap()));
        }

        @Override
        public void onError() {
            onPalette(null);
        }

        public abstract void onPalette(final Palette palette);
    }
}

/**
 * Use with:
 * Picasso.with(context)
 * .load (getPhotoUri())
 * .transform (new PaletteGeneratorTransformation (DEFAULT_NUM_COLORS))
 * .into (photo, new PaletteGeneratorTransformation.Callback (photo) {
 *
 * @Override public void onPalette (final Palette palette) {
 * themeWithPalette (palette);
 * }
 * });
 */
