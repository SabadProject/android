package farayan.commons.UI;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import farayan.commons.FarayanUtility;

public class QuestionDialog extends QuestionDialogParent {

    private String message = "";
    private String title = "";
    private String startButtonText = "";
    private String middleButtonText = "";
    private String lastButtonText = "";
    private View.OnClickListener startButtonClicked;
    private View.OnClickListener middleButtonClicked;
    private View.OnClickListener lastButtonClicked;

    protected boolean dialogResizeByKeyboard() {
        return false;
    }

    protected boolean dialogFullScreen() {
        return false;
    }

    public QuestionDialog(@NonNull Activity context) {
        super(context);
    }

    public QuestionDialog(@NonNull Activity context, int themeResId) {
        super(context, themeResId);
    }

    public QuestionDialog(@NonNull Activity context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void InitializeLayout() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);

        LayoutParams layoutParams = RootLayout().getLayoutParams();
        layoutParams.width = (int) (metrics.widthPixels * 0.85);
        RootLayout().setLayoutParams(layoutParams);

        //FarayanUtility.OverrideFonts(RootLayout(), FaraBankApp.GuideFont());

        Reload();
    }

    private void Reload() {
        if (StartButton() == null)
            return;
        QuestionTextView().setText(message);
        HeaderTextView().setText(title);
        StartButton().setVisibility(FarayanUtility.IsUsable(startButtonText) ? View.VISIBLE : View.GONE);
        StartButton().setText(startButtonText);
        StartButton().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                QuestionDialog.this.dismiss();
                if (startButtonClicked != null)
                    startButtonClicked.onClick(StartButton());
            }
        });

        MiddleButton().setVisibility(FarayanUtility.IsUsable(middleButtonText) ? View.VISIBLE : View.GONE);
        MiddleButton().setText(middleButtonText);
        MiddleButton().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                QuestionDialog.this.dismiss();
                if (middleButtonClicked != null)
                    middleButtonClicked.onClick(MiddleButton());
            }
        });

        LastButton().setVisibility(FarayanUtility.IsUsable(lastButtonText) ? View.VISIBLE : View.GONE);
        LastButton().setText(lastButtonText);
        LastButton().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                QuestionDialog.this.dismiss();
                if (lastButtonClicked != null)
                    lastButtonClicked.onClick(LastButton());
            }
        });
    }

    public String getMessage() {
        return message;
    }

    public QuestionDialog setMessage(String message, Object... values) {
        if (values != null && values.length > 0)
            message = String.format(message, values);
        this.message = message;
        Reload();
        return  this;
    }

    public QuestionDialog setMessage(int message)  {
        return setMessage(getContext().getString(message));
    }

    public String getTitle() {
        return title;
    }

    public QuestionDialog setTitle(String title) {
        this.title = title;
        Reload();
        return this;
    }

    public void setTitle(int title) {
        setTitle(getContext().getString(title));
    }

    public QuestionDialog setHeader(String header) {
        this.title = header;
        Reload();
        return this;
    }
    public QuestionDialog setHeader(int header) {
        return setHeader(getContext().getString(header));
    }

    // ===================

    public QuestionDialog setFirstButton(int text, View.OnClickListener onClickListener) {
        setFirstButtonText(text);
        setFirstButtonClicked(onClickListener);
        return this;
    }

    public QuestionDialog setFirstButton(String text, View.OnClickListener onClickListener) {
        setFirstButtonText(text);
        setFirstButtonClicked(onClickListener);
        return this;
    }

    public QuestionDialog setFirstButtonClicked(View.OnClickListener positiveButtonClicked) {
        this.startButtonClicked = positiveButtonClicked;
        return this;
    }

    public String getFirstButtonText() {
        return startButtonText;
    }

    public QuestionDialog setFirstButtonText(String text) {
        this.startButtonText = text;
        Reload();
        return this;
    }

    public QuestionDialog setFirstButtonText(int text) {
        return setFirstButtonText(getContext().getString(text));
    }

    public View.OnClickListener getFirstButtonClicked() {
        return startButtonClicked;
    }

    // =================

    public QuestionDialog setMiddleButton(String text, View.OnClickListener onClickListener) {
        setMiddleButtonText(text);
        setMiddleButtonClicked(onClickListener);
        return this;
    }

    public QuestionDialog setMiddleButton(int text, View.OnClickListener onClickListener) {
        setMiddleButtonText(text);
        setMiddleButtonClicked(onClickListener);
        return this;
    }

    public QuestionDialog setMiddleButtonClicked(View.OnClickListener onClickListener) {
        middleButtonClicked = onClickListener;
        return this;
    }

    public QuestionDialog setMiddleButtonText(String text) {
        middleButtonText = text;
        Reload();
        return this;
    }

    public QuestionDialog setMiddleButtonText(int text) {
        setMiddleButtonText(getContext().getString(text));
        return this;
    }

    // ==============

    public String getLastButtonText() {
        return lastButtonText;
    }

    public QuestionDialog setLastButtonText(String text) {
        this.lastButtonText = text;
        Reload();
        return this;
    }

    public QuestionDialog setLastButtonText(int text) {
        return setLastButtonText(getContext().getString(text));
    }

    public View.OnClickListener getLastButtonClicked() {
        return lastButtonClicked;
    }

    public QuestionDialog setLastButtonClicked(View.OnClickListener negativeButtonClicked) {
        this.lastButtonClicked = negativeButtonClicked;
        return this;
    }

    public QuestionDialog setLastButton(String text, View.OnClickListener onClickListener) {
        setLastButtonText(text);
        setLastButtonClicked(onClickListener);
        return this;
    }

    public QuestionDialog setLastButton(int text, View.OnClickListener onClickListener) {
        setLastButtonText(text);
        setLastButtonClicked(onClickListener);
        return this;
    }
}
