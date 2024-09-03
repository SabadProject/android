package farayan.commons.UI.Commons;

public enum DatePickerTimeModes {
	DayStart(1),
	Now(2),

	;

	public final int Code;

	DatePickerTimeModes(int code) {
		Code = code;
	}

	public static DatePickerTimeModes ByCode(int code) {
		for (DatePickerTimeModes mode : values()) {
			if (mode.Code == code)
				return mode;
		}
		return null;
	}
}
