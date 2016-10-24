package com.flycode.transporttrackeryerevan.util;

import android.content.Context;
import android.graphics.Point;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created on 7/28/16 __ acerkinght .
 */
public class DeviceUtil {

    public static float getPxForDp(Context context, float dp) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics() ;
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm);
    }
}
