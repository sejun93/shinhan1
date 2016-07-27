package util;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by cmcm1 on 2016-05-19.
 */
public class utils {

    final static public int COMPANY=1,SERVICE=-1;
    static public int TYPE=SERVICE;

    public static void setGlobalFont(Context context, View view) {
        if (view != null) {
            if (view instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) view;
                int len = vg.getChildCount();
                for (int i = 0; i < len; i++) {
                    View v = vg.getChildAt(i);
                    if (v instanceof TextView) {
                        ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "namsan.ttf"));
                    }
                    setGlobalFont(context, v);
                }
            }
        } else {
        }
    }
}
