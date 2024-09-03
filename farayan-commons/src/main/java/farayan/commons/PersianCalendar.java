package farayan.commons;

import java.util.Calendar;
import java.util.TimeZone;

public class PersianCalendar extends PersianDateTime {

    public PersianCalendar(int year, int month, int day, boolean timeNow) {
        super(year, month, day, timeNow);
    }

    public PersianCalendar(int year, int month, int day, int hour, int minute, int second, int millisecond, TimeZone timeZone) {
        super(year, month, day, hour, minute, second, millisecond, timeZone);
    }

    public PersianCalendar(long epoch) {
        super(epoch);
    }

    public PersianCalendar() {
        super();
    }

    public long getTimeInMillis() {
        return getEpoch();
    }

    public boolean after(PersianCalendar other) {
        return this.getEpoch() > other.getEpoch();
    }

    public boolean before(PersianCalendar other) {
        return this.getEpoch() < other.getEpoch();
    }

    public boolean before(Calendar other) {
        return this.getEpoch() < other.getTimeInMillis();
    }

	/**
	 * شروع هفته‌ی کنونی تقویم خورشید جلالی
	 * @return
	 */
	public static PersianCalendar getPersianWeekStart() {
        PersianCalendar pc = new PersianCalendar();
        pc.addDays(-pc.PersianWeekDay().PersianDayIndex + 1);
        pc.setTime(0, 0, 0, 0);
        return pc;
    }

	/**
	 * شروع روز فعلی در تقویم خورشیدی
	 * @return
	 */
	public static PersianCalendar getPersianToday() {
        PersianCalendar pc = new PersianCalendar();
        pc.setTime(0, 0, 0, 0);
        return pc;
    }

	/**
	 * شروع دیروز در تقویم خورشیدی
	 * @return
	 */
	public static PersianCalendar getPersianYesterday() {
        PersianCalendar pc = getPersianToday();
        pc.addDays(-1);
        return pc;
    }

	/**
	 * شروع ماه فعلی خورشیدی
	 * @return
	 */
	public static PersianCalendar getPersianMonthStart() {
        PersianCalendar today = getPersianToday();
        PersianCalendar monthStart = new PersianCalendar(today.getPersianYear(), today.getPersianMonthIndexFrom1(), 1, false);
        return monthStart;
    }

	/**
	 * شروع فصل فعلی خورشیدی
	 * @return
	 */
	public static PersianCalendar getPersianSeasonStart() {
        PersianCalendar today = getPersianToday();
        int seasonStartMonth = getPersianSeasonStartMonth(today.getPersianMonthIndexFrom1());
        PersianCalendar monthStart = new PersianCalendar(today.getPersianYear(), seasonStartMonth, 1, false);
        return monthStart;
    }

	/**
	 * شروع فصل خورشیدی براساس ماه
	 * @param persianMonth
	 * @return
	 */
	private static int getPersianSeasonStartMonth(int persianMonth) {
        switch (persianMonth) {
            case 1:
            case 2:
            case 3:
                return 1;
            case 4:
            case 5:
            case 6:
                return 4;
            case 7:
            case 8:
            case 9:
                return 7;
            case 10:
            case 11:
            case 12:
                return 10;
            default:
                return -1;
        }
    }

	/**
	 * شروع سال فعلی خورشیدی
	 * @return
	 */
	public static PersianCalendar getPersianYearStart() {
        return new PersianCalendar(getPersianToday().getPersianYear(), 1, 1, false);
    }

	/**
	 * شروع فردا
	 * @return
	 */
	public static PersianCalendar getPersianTomorrow() {
        PersianCalendar today = getPersianToday();
        today.addDays(1);
        return today;
    }

}
