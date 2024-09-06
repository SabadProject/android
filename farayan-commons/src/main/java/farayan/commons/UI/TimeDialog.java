package farayan.commons.UI;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import farayan.commons.Time;
import farayan.commons.UI.Core.ICommandEvent;
import farayan.commons.UI.Core.ICommandEventProvider;

/**
 * Created by Homayoun on 30/05/2017.
 */

public class TimeDialog extends TimeDialogParent implements ICommandEventProvider<Time>
{
    private Time value;
    private ICommandEvent<Time> TheEvent;
    void Select () {
            if (TheEvent != null)
                TheEvent.OnEvent("", Picker().getValue());
            dismiss();
    }

    public TimeDialog(Context context) {
        super((AppCompatActivity) context);
    }

    public void setValue(Time value) {
        Picker().setValue(value);
    }

    public void setHeader(String header) {
        HeaderTextView().setText(header);
    }

    @Override
    public void SetEventHandler(ICommandEvent<Time> iCommandEvent) {
        TheEvent = iCommandEvent;
    }

    @Override
    protected void InitializeLayout() {
        SelectButton().setOnClickListener(v -> Select());
        super.InitializeLayout();
    }
}
