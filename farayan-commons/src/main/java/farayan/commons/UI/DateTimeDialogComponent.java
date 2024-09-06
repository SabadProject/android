package farayan.commons.UI;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import farayan.commons.PersianDateTime;
import farayan.commons.UI.Commons.TextStyle;
import farayan.commons.UI.Core.IGeneralEvent;
import farayan.commons.UI.Core.IGeneralEventProvider;

/**
 * این ابزار، احتمالا باید پنهان باشد و سپس توسط توسعه‌دهنده، نمایش داده شود
 */
public class DateTimeDialogComponent extends DateTimeDialogComponentParent implements IGeneralEventProvider<Void, DateTimePickerComponent.Events, PersianDateTime> {
    private Dialog dialog;

    public DateTimeDialogComponent(Context context) {
        super(context);
    }

    public DateTimeDialogComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DateTimeDialogComponent(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public DateTimeDialogComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private IGeneralEvent<Void, DateTimePickerComponent.Events, PersianDateTime> TheEvent;

    public DateTimeDialogComponent(Activity context) {
        super(context);
    }

    public DateTimeDialogComponent(
            Context context,
            DatePickerConfig datePickerConfig,
            DateTimeDialogConfig dialogConfig,
            IGeneralEvent<Void, DateTimePickerComponent.Events, PersianDateTime> eventHandler
    ) {
        super(context);
        Picker().InitValues(datePickerConfig);
        Picker().SetEventHandler(TheEvent);
        TopContainer().setBackground(dialogConfig.Background);
        HeaderTextView().setTypeface(datePickerConfig.defaultTypeface);
        SelectButton().setTypeface(datePickerConfig.defaultTypeface);
        setHeaderText(dialogConfig.dialogHeaderText);
        TextStyle.Apply(HeaderTextView(), dialogConfig.dialogHeaderStyle);
        setPickCommandText(dialogConfig.dialogButtonText);
        TextStyle.Apply(SelectButton(), dialogConfig.dialogButtonStyle);
        SetEventHandler(eventHandler);
    }

    public void setHeaderText(String dialogHeaderText) {
        HeaderTextView().setText(dialogHeaderText);
    }

    public void setPickCommandText(String dialogButtonText) {
        SelectButton().setText(dialogButtonText);
    }

    @Override
    protected void InitializeLayout() {
        SelectButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TheEvent != null)
                    TheEvent.OnEvent(DateTimePickerComponent.Events.Changed, Picker().getSelectedPersianCalendar());
                if (dialog != null)
                    dialog.dismiss();
            }
        });
        super.InitializeLayout();
    }

    @Override
    public void SetEventHandler(IGeneralEvent<Void, DateTimePickerComponent.Events, PersianDateTime> iEvent) {
        TheEvent = iEvent;
    }

    public void ShowDialog() {
        dialog = new Dialog(getContext());
        dialog.setContentView(this);
        dialog.show();
    }
}
