package farayan.commons.UI.Commons;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.widget.TextView;

public class TextStyle {
    public Integer TextColor;
    public Integer TextGravity;
    public Integer TextSize;
    public Drawable Background;

    public static void Apply(TextView textView, TextStyle style) {
        if (textView == null)
            return;
        if (style == null)
            return;

        if (style.TextColor != null)
            textView.setTextColor(style.TextColor);
        if (style.TextGravity != null)
            textView.setGravity(style.TextGravity);
        if (style.TextSize != null)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, style.TextSize);
        if (style.Background != null)
            textView.setBackground(style.Background);
    }
}
