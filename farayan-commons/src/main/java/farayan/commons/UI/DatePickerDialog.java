package farayan.commons.UI;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import farayan.commons.PersianCalendar;
import farayan.commons.UI.Core.ICommandEvent;
import farayan.commons.UI.Core.ICommandEventProvider;

@Deprecated
public class DatePickerDialog extends DatePickerDialogParent implements ICommandEventProvider<PersianCalendar>
{
    private String ButtonText = "";
    private String Header = "";

    public DatePickerDialog(Context context) {
        super((AppCompatActivity) context);
    }

    @Override
    protected void InitializeLayout() {
        HeaderTextView().setText(getHeader());
        SelectDateButton().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TheEvent != null)
                    TheEvent.OnEvent("", DatePicker().getSelectedPersianCalendar());
            }
        });
    }

    public String getHeader() {
        return Header;
    }

    public void setHeader(String header) {
        this.Header = header;
        if (HeaderTextView() != null)
            HeaderTextView().setText(header);
    }

    public void setHeader(int header) {
        this.setHeader(getContext().getString(header));
    }

    public String getButtonText() {
        return ButtonText;
    }

    public void setButtonText(String buttonText) {
        this.ButtonText = buttonText;
        if (SelectDateButton() != null)
            SelectDateButton().setText(ButtonText);
    }

    public void setButtonText(int buttonTextResID) {
        this.setButtonText(getContext().getString(buttonTextResID));
    }

    ICommandEvent<PersianCalendar> TheEvent;

    @Override
    public void SetEventHandler(ICommandEvent<PersianCalendar> iCommandEvent) {
        TheEvent = iCommandEvent;
    }

    public PersianCalendar getValue() {
        if (DatePicker() == null)
            return null;
        return DatePicker().getSelectedPersianCalendar();
    }

    public void setValue(PersianCalendar value) {
        if (DatePicker() == null)
            return;
        if (value != null)
            DatePicker().setSelectedPersianCalendar(value);
    }

    public void InitValues(DatePickerConfig datePickerConfig) {
        DatePicker().InitValues(datePickerConfig);
    }
}
