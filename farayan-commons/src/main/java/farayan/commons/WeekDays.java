package farayan.commons;

public enum WeekDays {
	Saturday(1, 5, "شنبه"),
	Sunday(2, 6, "یکشنبه"),
	Monday(3, 0, "دوشنبه"),
	Tuesday(4, 1, "سه‌شنبه"),
	Wednesday(5, 2, "چهارشنبه"),
	Thursday(6, 3, "پنج‌شنبه"),
	Friday(7, 4, "آدینه"),;

	public final int PersianDayIndex;
	public final int GregorianDayIndex;
	public final String PersianDayName;

	WeekDays(int persianDayIndex, int gregorianDayIndex, String persianDayName) {
		PersianDayIndex = persianDayIndex;
		GregorianDayIndex = gregorianDayIndex;
		PersianDayName = persianDayName;
	}

	public static WeekDays ByPersianWeekDayIndex(int dayIndex) {
		for (WeekDays weekDay : values()) {
			if (weekDay.PersianDayIndex == dayIndex)
				return weekDay;
		}
		return null;
	}

	public static WeekDays ByGregorianWeekDayIndex(int dayIndex) {
		for (WeekDays weekDay : values()) {
			if (weekDay.GregorianDayIndex == dayIndex)
				return weekDay;
		}
		return null;
	}

	public static String[] PersianDayNames() {
		return new String[]{
				Saturday.PersianDayName,
				Sunday.PersianDayName,
				Monday.PersianDayName,
				Tuesday.PersianDayName,
				Wednesday.PersianDayName,
				Thursday.PersianDayName,
				Friday.PersianDayName,
		};
	}
}
