package farayan.sabad;

import com.google.zxing.client.android.BeepManager;

import farayan.sabad.constants.BeepVibrateTypes;

public interface SabadConfigs
{
	static BeepVibrateTypes BeepGroup() {
		return BeepVibrateTypes.BeepAndVibrate;
	}

	static void Notify(BeepManager beepManager) {
		switch (BeepGroup()){
			case Beep:
				beepManager.playBeepSound();
				break;
			case BeepAndVibrate:
				beepManager.playBeepSoundAndVibrate();
				break;
			case Vibrate:
			case None:

				break;
		}
	}
}
