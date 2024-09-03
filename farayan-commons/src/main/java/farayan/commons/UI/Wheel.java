package farayan.commons.UI;

import android.content.Context;
import android.util.AttributeSet;

import com.github.florent37.singledateandtimepicker.widget.WheelPicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import farayan.commons.FarayanUtility;
import farayan.commons.UI.Commons.NumberLanguages;

public class Wheel extends WheelPicker {
    //private ToTextFormatter Formatter;
    private Adapter adapter;

    public Wheel(Context context) {
        super(context);
    }

    public Wheel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onItemSelected(int position, Object value) {
    }

    @Override
    protected void onItemCurrentScroll(int position, Object value) {
    }

    @Override
    protected String getFormattedValue(Object o) {
        return o.toString();
    }

    @Override
    public int getDefaultItemPosition() {
        return 0;
    }

    public void setRangeValues(int min, int max, boolean leadingZero, NumberLanguages language) {
        setRangeValues(min, max, leadingZero, language, 1);
    }

    public void setRangeValues(int min, int max, boolean leadingZero, NumberLanguages language, int step) {
        if (step < 1)
            step = 1;
        CustomAdapter adapter = new CustomAdapter(leadingZero, language);
        List<Integer> data = new ArrayList<>(max - min);
        int start = min, end = max - min;
        for (int index = 0; index <= end; index += step) {
            data.add(min + index);
        }
        adapter.setData(data);
        setAdapter(adapter);
    }

    public void setValue(Object value) {
        if (adapter == null || adapter.getItemCount() == 0)
            return;
        for (int position = 0; position < adapter.getItemCount(); position++) {
            if (value.equals(adapter.getItem(position))) {
                setSelectedItemPosition(position);
                return;
            }
        }
    }

    public void setDisplayedValues(NumberLanguages language, List<String> values) {
        CustomAdapter adapter = new CustomAdapter(false, language, values);
        setAdapter(adapter);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        super.setAdapter(adapter);
    }

    class CustomAdapter extends Adapter {
        private final boolean leadingZero;
        private final NumberLanguages language;

        public CustomAdapter(boolean leadingZero, NumberLanguages language) {
            this.leadingZero = leadingZero;
            this.language = language;
        }

        public CustomAdapter(boolean leadingZero, NumberLanguages language, List data) {
            super(data);
            this.leadingZero = leadingZero;
            this.language = language;
        }

        @Override
        public String getItemText(int position) {
            String text = "";
            if (leadingZero) {
                Object value = super.getItem(position);
                if (value instanceof Integer)
                    text = FarayanUtility.LeadingZero((Integer) value, 2);
            } else {
                text = super.getItemText(position);
            }

            if (language == null)
                return text;

            switch (language) {
                case None:
                    return text;
                case English:
                    return FarayanUtility.ConvertNumbersToAscii(text);
                case Persian:
                    return FarayanUtility.ConvertNumbersToPersian(text);
                case Arabic:
                    return FarayanUtility.ConvertNumbersToAscii(text);
                default:
                    throw new RuntimeException("Language is not supported");
            }
        }

    }

    public Object getValue() {
        return adapter.getItem(getCurrentItemPosition());
    }


    public void setDisplayedValues(NumberLanguages numberLanguage, String[] strings) {
        CustomAdapter adapter = new CustomAdapter(false, numberLanguage, Arrays.asList(strings));
        setAdapter(adapter);
    }
}