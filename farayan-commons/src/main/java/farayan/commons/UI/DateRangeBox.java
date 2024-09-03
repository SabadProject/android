package farayan.commons.UI;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;

import farayan.commons.FarayanUtility;
import farayan.commons.PersianCalendar;
import farayan.commons.PersianPeriod;
import farayan.commons.R;
import farayan.commons.SimpleEnumBoxTextViewComponent;
import farayan.commons.UI.Core.ICommandEvent;
import farayan.commons.UI.Core.ICommandEventProvider;
import farayan.commons.QueryBuilderCore.DateFilterTypes;

@Deprecated
public class DateRangeBox extends Spinner implements ICommandEventProvider<String>
{
    String Hint;

    private PersianCalendar SelectedDay;
    private PersianCalendar SelectedDaysStart, SelectedDaysFinish;

    private PersianCalendar SelectedStart = null;
    private PersianCalendar SelectedFinish = null;

    PersianPeriod selectedRange;
    private boolean IgnoreDateFilterTypeChangesOnce;
    private ColorStateList TextColor;

    public DateRangeBox(Context context) {
        super(context);
        InitializeLayout();
    }

    public DateRangeBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.DateRangeBox, 0, 0);
        try {
            Hint = attributes.getString(R.styleable.DateRangeBox_hint);
            TextColor = attributes.getColorStateList(R.styleable.DateRangeBox_textColor);
        } finally {
            attributes.recycle();
        }
        InitializeLayout();
    }

    public PersianPeriod getRange() {
        if (selectedRange != null)
            return selectedRange;
        if (SelectedStart == null)
            SelectedStart = CalculateStart();
        if (SelectedFinish == null)
            SelectedFinish = CalculateFinish();
        if (SelectedStart == null || SelectedFinish == null)
            return null;
        selectedRange = new PersianPeriod(SelectedStart, SelectedFinish);

        return selectedRange;
    }

    public void setRange(PersianPeriod range) {
        selectedRange = range;
        long sentStart = range == null || range.getStart() == null ? 0 : range.getStart().getTimeInMillis();
        long sentFinish = range == null || range.getFinish() == null ? 0 : range.getFinish().getTimeInMillis();

        long currentStart = SelectedDaysStart == null ? 0 : SelectedDaysStart.getTimeInMillis();
        long currentFinish = SelectedDaysFinish == null ? 0 : SelectedDaysFinish.getTimeInMillis();

        boolean changed = sentStart != currentStart || sentFinish != currentFinish;
        if (changed) {
            SelectedDaysStart = range == null ? null : range.getStart();
            SelectedDaysFinish = range == null ? null : range.getFinish();
            Reset();
        }
    }

    private void Reset() {
        if (SelectedDaysStart == null)
            return;
        if (SelectedDaysFinish == null)
            return;
        IgnoreDateFilterTypeChangesOnce = true;
        Log.i("DefaultPeriod", "Reset to " + DateFilterTypes.SpecificDays.ID);
        setSelection(DateFilterTypes.SpecificDays.ID);
        Reload();
    }

    private PersianCalendar CalculateStart() {

        DateFilterTypes type = DateFilterTypes.FromID(getSelectedItemPosition());

        switch (type) {
            case Skip:
                return null;
            case Today:
                return FarayanUtility.Today();
            case Yesterday:
                return FarayanUtility.Yesterday();
            case CurrentWeek:
                return FarayanUtility.WeekStart();
            case PreviousWeek:
                PersianCalendar previousWeekStart = FarayanUtility.WeekStart();
                previousWeekStart.addDays(-7);
                return previousWeekStart;
            case CurrentMonth:
                return FarayanUtility.MonthStart();
            case PreviousMonth:
                PersianCalendar previousMonthStart = FarayanUtility.MonthStart();
                previousMonthStart.addPersianMonths(-1);
                return previousMonthStart;
            case CurrentSeason:
                return FarayanUtility.SeasonStart();
            case PreviousSeason:
                PersianCalendar previousSeasonStart = FarayanUtility.SeasonStart();
                previousSeasonStart.addPersianMonths(-3);
                return previousSeasonStart;
            case CurrentYear:
                return FarayanUtility.YearStart();
            case PreviousYear:
                PersianCalendar previousYearStart = FarayanUtility.YearStart();
                previousYearStart.addPersianYear(-1);
                return previousYearStart;
            case SpecificDay:
                return SelectedDay;
            case SpecificDays:
                return SelectedDaysStart;
            default:
                return null;
        }
    }

    public PersianCalendar getFinish() {
        if (SelectedFinish == null)
            SelectedFinish = CalculateFinish();
        return SelectedFinish;
    }



    public Long getFinishEpoch() {
        return getFinish() == null ? null : getFinish().getEpoch();
    }

    private PersianCalendar CalculateFinish() {
        DateFilterTypes type = DateFilterTypes.FromID(getSelectedItemPosition());

        switch (type) {
            case Skip:
                return null;
            case Today:
                PersianCalendar tomorrow = FarayanUtility.Tomorrow();
                tomorrow.addMilliseconds(-1);
                return tomorrow;
            case Yesterday:
                PersianCalendar today = FarayanUtility.Today();
                today.addMilliseconds(-1);
                return today;
            case CurrentWeek:
                return new PersianCalendar();
            case PreviousWeek:
                PersianCalendar previousWeekFinish = FarayanUtility.WeekStart();
                previousWeekFinish.addMilliseconds(-1);
                return previousWeekFinish;
            case CurrentMonth:
                return new PersianCalendar();
            case PreviousMonth:
                PersianCalendar previousMonthFinish = FarayanUtility.MonthStart();
                previousMonthFinish.addMilliseconds(-1);
                return previousMonthFinish;
            case CurrentSeason:
                return new PersianCalendar();
            case PreviousSeason:
                PersianCalendar previousSeasonFinish = FarayanUtility.SeasonStart();
                previousSeasonFinish.addMilliseconds(-1);
                return previousSeasonFinish;
            case CurrentYear:
                return new PersianCalendar();
            case PreviousYear:
                PersianCalendar previousYearFinish = FarayanUtility.YearStart();
                previousYearFinish.addMilliseconds(-1);
                return previousYearFinish;
            case SpecificDay:
                PersianCalendar selected1 = new PersianCalendar();
                selected1.setTimeInMillis(SelectedDay.getTimeInMillis());
                selected1.addDays(1);
                selected1.addMilliseconds(-1);
                return selected1;
            case SpecificDays:
                PersianCalendar selected2 = new PersianCalendar();
                selected2.setTimeInMillis(SelectedDaysFinish.getTimeInMillis());
                selected2.addDays(1);
                selected2.addMilliseconds(-1);
                return selected2;
            default:
                return null;
        }
    }

    private OnItemSelectedListener DateSpinner_ItemSelected = new OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> adapter, View view, int position, long id) {
            if (IgnoreDateFilterTypeChangesOnce) {
                IgnoreDateFilterTypeChangesOnce = false;
                return;
            }
            Log.i("onItemSelected", "Started");
            if (getTag() != null) {
                int previousSelected = (Integer) getTag();
                if (position == previousSelected)
                    return;
            }

            selectedRange = null;
            SelectedStart = SelectedFinish = null;

            setTag(position);

            DateFilterTypes type = DateFilterTypes.FromID(getSelectedItemPosition());

            if (type == DateFilterTypes.SpecificDay) {
                final DatePickerDialog pcc = new DatePickerDialog(getContext());
                pcc.setHeader(R.string.DateRangeBoxComponent_SelectDate);
                pcc.SelectDateButton().setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        SelectedDay = pcc.DatePicker().getSelectedPersianCalendar();
                        pcc.dismiss();
                        Reload();
                        FireEventHandler("");
                    }
                });
                Log.i("onItemSelected", "Dialog is shown");
                pcc.show();
            } else if (type == DateFilterTypes.SpecificDays) {
                final DatePickerDialog pcc = new DatePickerDialog(getContext());
                pcc.setHeader(R.string.DateRangeBoxComponentSelectPeriodStart);
                pcc.SelectDateButton().setOnClickListener(new OnClickListener() {

                    int capturedDate = 0;

                    @Override
                    public void onClick(View v) {
                        if (capturedDate == 0) {
                            SelectedDaysStart = pcc.DatePicker().getSelectedPersianCalendar();
                            pcc.setHeader(R.string.DateRangeBoxComponentSelectPeriodFinish);
                            FarayanUtility.ShowToast("ابتدای دوره را انتخاب کردید، حالا انتهای دوره را انتخاب کنید");
                            capturedDate++;
                        } else {
                            SelectedDaysFinish = pcc.DatePicker().getSelectedPersianCalendar();
                            pcc.dismiss();
                            Reload();
                            FireEventHandler("");
                        }
                    }
                });
                pcc.show();
            } else {
                FireEventHandler(null);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapter) {
        }
    };

    protected void InitializeLayout() {
        Reload();
        setOnItemSelectedListener(DateSpinner_ItemSelected);
    }

    private void Reload() {
        Log.i("onItemSelected", "changing = true");
        int selectedIndex = getSelectedItemPosition();
        Log.i("onItemSelected", "selected_background index=" + selectedIndex);
        setAdapter(new DateTimeAdapter());
        if (selectedIndex >= 0) {
            Log.i("DefaultPeriod", "Reset to " + DateFilterTypes.SpecificDays.ID);
            setSelection(selectedIndex);
        }
    }

    class DateTimeAdapter extends BaseAdapter {

        public DateTimeAdapter() {
        }

        @Override
        public int getCount() {
            return DateFilterTypes.values().length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null)
                convertView = new SimpleEnumBoxTextViewComponent(getContext());
            SimpleEnumBoxTextViewComponent textview = (SimpleEnumBoxTextViewComponent) convertView;

            DateFilterTypes type = DateFilterTypes.FromID(position);

            String text = "";
            switch (type) {
                case Skip:
                    text = FarayanUtility.GetValueOrDefault(Hint, getResources().getString(R.string.DateRangeBoxComponentComeOn));
                    break;
                case Today:
                    PersianCalendar today = FarayanUtility.Today();
                    text = getResources().getString(R.string.DateRangeBoxComponentToday) ;
                    break;
                case Yesterday:
                    PersianCalendar yesterday = FarayanUtility.Yesterday();
                    text = getResources().getString(R.string.DateRangeBoxComponentYesterday) ;
                    break;
                case CurrentWeek:
                    text = getResources().getString(R.string.DateRangeBoxComponentCurrentWeek);
                    break;
                case PreviousWeek:
                    PersianCalendar previousWeekStart = FarayanUtility.WeekStart();
                    previousWeekStart.addDays(-7);

                    PersianCalendar previousWeekFinish = FarayanUtility.WeekStart();
                    previousWeekFinish.addMilliseconds(-1);

                    text = getResources().getString(R.string.DateRangeBoxComponentPreviousWeek);
                    break;
                case CurrentMonth:
                    text = getResources().getString(R.string.DateRangeBoxComponentCurrentMonth);
                    break;
                case PreviousMonth:
                    PersianCalendar previousMonthStart = FarayanUtility.MonthStart();
                    previousMonthStart.addPersianMonths(-1);

                    PersianCalendar previousMonthFinish = FarayanUtility.MonthStart();
                    previousMonthFinish.addMilliseconds(-1);
                    text = getResources().getString(R.string.DateRangeBoxComponentPreviousMonth);
                    break;
                case CurrentSeason:
                    text = getResources().getString(R.string.DateRangeBoxComponentCurrentSeason);
                    break;
                case PreviousSeason:
                    PersianCalendar previousSeasonStart = FarayanUtility.SeasonStart();
                    previousSeasonStart.addPersianMonths(-3);

                    PersianCalendar previousSeasonFinish = FarayanUtility.SeasonStart();
                    previousSeasonFinish.addMilliseconds(-1);
                    text = getResources().getString(R.string.DateRangeBoxComponentPreviousSeason);
                    break;
                case CurrentYear:
                    text = getResources().getString(R.string.DateRangeBoxComponentCurrentYear);
                    break;
                case PreviousYear:
                    PersianCalendar previousYearStart = FarayanUtility.YearStart();
                    previousYearStart.addPersianYear(-1);

                    PersianCalendar previousYearFinish = FarayanUtility.YearStart();
                    previousYearFinish.addMilliseconds(-1);

                    text = getResources().getString(R.string.DateRangeBoxComponentPreviousYear);
                    break;
                case SpecificDay:
                    text = getResources().getString(R.string.DateRangeBoxComponentSpecificDay);
                    if (SelectedDay != null)
                        text += " " + SelectedDay.getPersianDateSentence(true);
                    break;
                case SpecificDays:
                    text = getResources().getString(R.string.DateRangeBoxComponentSpecificDays);
                    if (SelectedDaysStart != null && SelectedDaysFinish != null)
                        text += getResources().getString(R.string.DateRangeBoxComponentFrom) + SelectedDaysStart.getPersianDateSentence(true) + getResources().getString(R.string.DateRangeBoxComponentTo) + SelectedDaysFinish.getPersianDateSentence(true);
                    break;

                default:
                    break;
            }
            if (TextColor != null)
                textview.setTextColor(TextColor);
            textview.setGravity(Gravity.CENTER);
            textview.setHint(text);

            return textview;
        }
    }

    private void FireEventHandler(String string) {
        if (EventHandler != null)
            EventHandler.OnEvent("", "");
    }

    ICommandEvent<String> EventHandler;

    @Override
    public void SetEventHandler(ICommandEvent<String> iCommandEvent) {
        EventHandler = iCommandEvent;
    }

    public PersianCalendar getStart() {
        if (SelectedStart == null)
            SelectedStart = CalculateStart();
        return SelectedStart;
    }

    public Long getStartEpoch() {
        return getStart() == null ? null : getStart().getEpoch();
    }

    public void setSelectedPeriod(DateFilterTypes period) {
        setSelection(period.ID);
    }
}
