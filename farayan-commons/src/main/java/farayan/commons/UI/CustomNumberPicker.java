package farayan.commons.UI;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import farayan.commons.FarayanBaseApp;
import farayan.commons.FarayanUtility;

public class CustomNumberPicker extends NumberPicker {
    public CustomNumberPicker(Context context) {
        super(context);
    }

    public CustomNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomNumberPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void PrepareView(View child) {
        if (child instanceof EditText) {
            ((EditText) child).setTypeface(FarayanBaseApp.getFont());
            if (Color > 0)
                ((EditText) child).setTextColor(Color);
        }
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        PrepareView(child);
    }

    @Override
    public void addView(View child, int index) {
        super.addView(child, index);
        PrepareView(child);
    }

    @Override
    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        PrepareView(child);
    }

    @Override
    public void addView(View child, int width, int height) {
        super.addView(child, width, height);
        PrepareView(child);
    }

    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);
        PrepareView(child);
    }

    int Color = 0;

    public void setColor(int color) {
        this.Color = color;
    }

    public void setTypeface(Typeface font) {
        FarayanUtility.OverrideFonts(this, font);
    }

    public void setRange(int min, int max) {
        setMinValue(min);
        setMaxValue(max);
    }
}
