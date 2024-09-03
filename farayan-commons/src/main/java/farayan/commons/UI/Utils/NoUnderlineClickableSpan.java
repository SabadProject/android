package farayan.commons.UI.Utils;

import android.text.TextPaint;
import android.text.style.ClickableSpan;

public abstract class NoUnderlineClickableSpan extends ClickableSpan {

	@Override
	public void updateDrawState(TextPaint ds) {
		super.updateDrawState(ds);
		ds.setUnderlineText(false);
	}
}