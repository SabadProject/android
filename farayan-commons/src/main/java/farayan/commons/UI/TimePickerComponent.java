package farayan.commons.UI;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.NumberPicker;

import farayan.commons.FarayanUtility;
import farayan.commons.PersianCalendar;
import farayan.commons.Time;

public class TimePickerComponent extends TimePickerComponentParent {
    private Time m_value;

    public TimePickerComponent(Context context) {
        super(context);
    }

    public TimePickerComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimePickerComponent(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TimePickerComponent(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void InitializeLayout() {
        NumberPicker.Formatter formatter = new NumberPicker.Formatter() {

            @Override
            public String format(int value) {
                return FarayanUtility.ConvertNumbersToPersian(FarayanUtility.LeadingZero(value, 2));
            }
        };
        hourNumberPicker().setMinValue(0);
        hourNumberPicker().setMaxValue(23);
        hourNumberPicker().setFormatter(formatter);

        minuteNumberPicker().setMinValue(0);
        minuteNumberPicker().setMaxValue(59);
        minuteNumberPicker().setFormatter(formatter);

        secondNumberPicker().setMinValue(0);
        secondNumberPicker().setMaxValue(59);
        secondNumberPicker().setFormatter(formatter);

        super.InitializeLayout();
    }

    @Override
    protected void InitializeLayout(AttributeSet attrs) {
        InitializeLayout();
        super.InitializeLayout(attrs);
    }

    @Override
    protected void InitializeLayout(AttributeSet attrs, int defStyleAttr) {
        InitializeLayout();
        super.InitializeLayout(attrs, defStyleAttr);
    }

    @Override
    protected void InitializeLayout(@Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        InitializeLayout();
        super.InitializeLayout(attrs, defStyleAttr, defStyleRes);
    }

    public Time getValue() {
        if (m_value == null) {
            m_value = new Time();
            m_value.setHour(hourNumberPicker().getValue());
            m_value.setMinute(minuteNumberPicker().getValue());
            m_value.setSecond(secondNumberPicker().getValue());
        }
        return m_value;
    }

    public void setValue(Time value) {
        m_value = value;
        PersianCalendar now = new PersianCalendar();
        hourNumberPicker().setValue(value == null ? now.getHour() : value.getHour());
        minuteNumberPicker().setValue(value == null ? now.getMinute() : value.getMinute());
        secondNumberPicker().setValue(value == null ? now.getSecond() : value.getSecond());
    }
}
