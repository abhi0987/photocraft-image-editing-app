package pencil.abhishek.io.pencil.utills;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Abhishek on 5/12/2016.
 */
public class customtext extends TextView{
    public static Typeface FONT_NAME;

    public customtext(Context context) {
        super(context);

        if(FONT_NAME == null)
            FONT_NAME = Typeface.createFromAsset(context.getAssets(), "AvenirLTStd-Book.otf");


        this.setTypeface(FONT_NAME);
    }

    public customtext(Context context, AttributeSet attrs) {
        super(context, attrs);

        if(FONT_NAME == null)
            FONT_NAME = Typeface.createFromAsset(context.getAssets(), "AvenirLTStd-Book.otf");


        this.setTypeface(FONT_NAME);
    }

    public customtext(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if(FONT_NAME == null)
            FONT_NAME = Typeface.createFromAsset(context.getAssets(), "AvenirLTStd-Book.otf");


        this.setTypeface(FONT_NAME);
    }
}
