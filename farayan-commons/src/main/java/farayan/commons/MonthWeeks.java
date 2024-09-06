package farayan.commons;

public enum MonthWeeks {
    /**
     * در صورتی که روز اول ماه، غیر از شنبه باشد، روزهای اولی ماه تا قبل از اولین شنبه به عنوان هفته صفرم در نظر گرفته می‌شود
     */
    Week0(0, "هفته صفرم", "صفرم"),
    Week1(1, "هفته اول", "یکم"),
    Week2(2, "هفته دوم", "دوم"),
    Week3(3, "هفته سوم", "سوم"),
    Week4(4, "هفته چهارم", "چهارم"),
    Week5(5, "هفته پنجم", "پنجم"),;

    public final int ServerCode;
    public final String PersianNamePrefixed;
    public final String PersianName;

    MonthWeeks(int serverCode, String persianNamePrefixed, String persianName) {
        ServerCode = serverCode;
        PersianNamePrefixed = persianNamePrefixed;
        PersianName = persianName;
    }

    public static MonthWeeks ByIndex(int monthWeek) {
        return ByServerCode(monthWeek);
    }

    public static MonthWeeks ByServerCode(int serverCode) {
        for (MonthWeeks week : values()) {
            if (week.ServerCode == serverCode)
                return week;
        }
        return null;
    }
}
