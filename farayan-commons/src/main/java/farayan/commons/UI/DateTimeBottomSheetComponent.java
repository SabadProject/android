package farayan.commons.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import farayan.commons.PersianDateTime;
import farayan.commons.UI.Commons.TextStyle;
import farayan.commons.UI.Core.IGeneralEvent;
import farayan.commons.UI.Core.IGeneralEventProvider;

public class DateTimeBottomSheetComponent extends DateTimeBottomSheetComponentParent implements IGeneralEventProvider<Void, DateTimePickerComponent.Events, PersianDateTime> {
	private IGeneralEvent<Void, DateTimePickerComponent.Events, PersianDateTime> TheEvent;
	private BottomSheetDialog dialog;

	public DateTimeBottomSheetComponent(Context context) {
		super(context);
	}

	public DateTimeBottomSheetComponent(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DateTimeBottomSheetComponent(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public DateTimeBottomSheetComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	public DateTimeBottomSheetComponent(
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

	@Override
	protected void InitializeComponents() {
		SelectButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (TheEvent != null)
					TheEvent.OnEvent(DateTimePickerComponent.Events.Changed, Picker().getSelectedPersianCalendar());
				if (dialog != null)
					dialog.dismiss();
			}
		});
		super.InitializeComponents();
	}

	public void setHeaderText(String dialogHeaderText) {
		HeaderTextView().setText(dialogHeaderText);
	}

	public void setPickCommandText(String dialogButtonText) {
		SelectButton().setText(dialogButtonText);
	}

	@Override
	public void SetEventHandler(IGeneralEvent<Void, DateTimePickerComponent.Events, PersianDateTime> iEvent) {
		TheEvent = iEvent;
	}

	public void ShowDialog() {
		dialog = new BottomSheetDialog(getContext());
		dialog.setContentView(this);
		dialog.show();
	}
}
