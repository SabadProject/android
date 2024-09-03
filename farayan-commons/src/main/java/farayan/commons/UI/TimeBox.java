package farayan.commons.UI;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import farayan.commons.PersianCalendar;
import farayan.commons.R;
import farayan.commons.Time;
import farayan.commons.UI.Core.ICommandEvent;

/**
 * به صورت یک متن ساده، زمان انتخابی را نشان می‌دهد
 * در صورتی که روی زمان یا آیکنِ انتخابِ کنارِ آن کلیک شود، دیالوگ انتخاب زمان نمایش داده می‌شود
 * دو تا ابزاار دیگر هم داریم
 * اولی، دیالوگ انتخاب زمان است
 * دومی انتخابگر زمان است
 * دیالوگ انتخاب‌گر زمان در واقع همان انتخاب‌گر زمان را نشان می‌دهد
 */
public class TimeBox extends TextView {

    String defaultText = "بدون زمان";
    String label = "انتخاب زمان";

    public TimeBox(Context context) {
        super(context);
        InitializeLayout();
    }

    public TimeBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        LoadAttributes(context, attrs);
        InitializeLayout();
    }

    public TimeBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LoadAttributes(context, attrs);
        InitializeLayout();
    }

    protected void InitializeLayout() {
        setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.select), null, null, null);
        this.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final TimeDialog dialog = new TimeDialog(getContext());
                dialog.setValue(Value);
                dialog.setHeader(label);
                dialog.SetEventHandler(new ICommandEvent<Time>() {

                    @Override
                    public void OnEvent(String name, Time value) {
                        setValue(value);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        setValue(getValue());
    }

    private void LoadAttributes(Context context, AttributeSet attrs) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.DateTimeBox, 0, 0);
        try {
            defaultText = attributes.getString(R.styleable.DateTimeBox_hint);
        } finally {
            attributes.recycle();
        }
        setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
    }

    public Time getValue() {
        return Value;
    }

    public void setValue(Time value) {
        Value = value;
        setText(value == null ? defaultText : value.toString());
    }

    public void setValue(PersianCalendar calendar) {
        Value = new Time();
        Value.setHour(calendar.getHour());
        Value.setMinute(calendar.getMinute());
        Value.setSecond(calendar.getSecond());
        setValue(Value);
    }

    private Time Value;
}
