package util;

import android.app.Fragment;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.Queue;

import static android.util.TypedValue.COMPLEX_UNIT_PX;

/**
 * Created by Bryce on 2018-03-08.
 */

public class Resizer {

    private float scale;

    public Resizer(float scale){
        this.scale = scale;
    }

    public void adjust(TextView textView) {
        textView.setTextSize(COMPLEX_UNIT_PX, textView.getTextSize() * scale);
    }


    public void adjust(View view) {
        ViewGroup.LayoutParams par = view.getLayoutParams();
        par.width *= scale;
        par.height *= scale;
        view.setLayoutParams(par);
    }

    public static void set(View view, int canvasHeight, int canvasWidth) {
        ViewGroup.LayoutParams par = view.getLayoutParams();
        par.width = canvasWidth;
        par.height = canvasHeight;
        view.setLayoutParams(par);
    }

    public void adjustAll(ViewGroup layout) {
        Queue<View> queue = new LinkedList<>();
        queue.offer(layout);
        while(!queue.isEmpty()){
            View view = queue.poll();
            if (view instanceof ViewGroup){
                if(!view.equals(layout))
                    adjust(view);
                ViewGroup viewGroup = (ViewGroup) view;
                int count = viewGroup.getChildCount();
                for (int i = 0; i < count; i++) {
                    View child = viewGroup.getChildAt(i);
                    queue.offer(child);
                }
            }else if (view instanceof TextView)
                adjust((TextView) view);
            else {
                adjust(view);
            }
        }
    }

    public static void resizeAlert(ViewGroup alert, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int deviceHeight = displayMetrics.heightPixels;
        int deviceWidth = displayMetrics.widthPixels;

        float scale = (float) (Math.sqrt(deviceHeight / 592 * deviceWidth / 360) / displayMetrics.density);

        Resizer resizer = new Resizer(scale);
        resizer.adjustAll(alert);
    }

}
