package util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
}
