package farayan.commons.UI;

import android.content.Context;

import farayan.commons.Commons.Rial;
import farayan.commons.UI.Core.IDisplay;

public class RialItemComponent extends RialItemComponentParent implements IDisplay<Rial.Coefficients>
{
	public static class Config
	{
		public final int gravity;

		public Config(int gravity) {
			this.gravity = gravity;
		}
	}

	public RialItemComponent(Context context, Config config) {
		super(context);
		setGravity(config.gravity);
	}

	@Override
	public void Display(Rial.Coefficients entity) {
		NameTextView().setText(entity.NameResID);
	}
}
