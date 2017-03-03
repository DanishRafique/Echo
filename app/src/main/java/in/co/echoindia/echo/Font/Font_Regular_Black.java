package in.co.echoindia.echo.Font;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Danish Rafique on 26-02-2017.
 */

public class Font_Regular_Black extends TextView {

    public static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";

    public Font_Regular_Black(Context context) {
        super(context);
        applyCustomFont(context, null);

    }

    public Font_Regular_Black(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context, attrs);
    }

    public Font_Regular_Black(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context, attrs);
    }


    private void applyCustomFont(Context context, AttributeSet attrs) {
        int textStyle = attrs.getAttributeIntValue(ANDROID_SCHEMA, "textStyle", Typeface.NORMAL);

        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "Font/Slider Regular.ttf");
        setTypeface(customFont);
        setTextColor(Color.parseColor("#000000"));
    }
}