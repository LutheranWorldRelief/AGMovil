package com.cacaomovil.app.common;

import android.content.Context;
import android.graphics.Typeface;

/**
 * This class provides ...
 */

public final class TypefaceUtil {
    private TypefaceUtil() {
        throw new RuntimeException("TypefaceUtil constructor is not allowed");
    }


    public static Typeface getBold(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/RobotoBold.ttf");
    }

    public static Typeface getLight(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/RobotoLight.ttf");
    }
    public static Typeface getRegular(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/RobotoRegular.ttf");
    }


}