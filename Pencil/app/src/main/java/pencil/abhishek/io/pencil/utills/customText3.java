package pencil.abhishek.io.pencil.utills;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by abhis on 6/29/2016.
 */
public class customText3 extends TextView {
    public static Typeface FONT_NAME;

    public customText3(Context context) {
        super(context);

        if(FONT_NAME == null)
            FONT_NAME = Typeface.createFromAsset(context.getAssets(), "FRINCO.ttf");


        this.setTypeface(FONT_NAME);
    }

    public customText3(Context context, AttributeSet attrs) {
        super(context, attrs);

        if(FONT_NAME == null)
            FONT_NAME = Typeface.createFromAsset(context.getAssets(), "FRINCO.ttf");


        this.setTypeface(FONT_NAME);
    }

    public customText3(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if(FONT_NAME == null)
            FONT_NAME = Typeface.createFromAsset(context.getAssets(), "FRINCO.ttf");


        this.setTypeface(FONT_NAME);
    }
}
