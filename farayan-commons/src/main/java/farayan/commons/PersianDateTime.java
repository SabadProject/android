package farayan.commons;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Title: Calender Conversion class
 * Description: Convert Persian (Jalali), Julian, and Gregorian dates to
 * each other
 * Public Methods Summary:
 * -----------------------
 * JavaSource_Calendar();
 * JavaSource_Calendar(int year, int month, int day);
 * int getPersianYear();
 * int getPersianMonthIndexFrom1();
 * int getPersianDay();
 * int getGregorianYear();
 * int getGregorianMonth();
 * int getGregorianDay();
 * int getJulianYear();
 * int getJulianMonth();
 * int getJulianDay();
 * String getPersianDate();
 * String getGregorianDate();
 * String getJulianDate();
 * String getWeekDayStr();
 * String toString();
 * int getDayOfWeek();
 * void nextDay();
 * void nextDay(int days);
 * void previousDay();
 * void previousDay(int days);
 * void setPersianDate(int year, int month, int day);
 * void setGregorianDate(int year, int month, int day);
 * void setJulianDate(int year, int month, int day);
 */
public class PersianDateTime
{

	/**
	 * JavaSource_Calendar:
	 * The default constructor uses the current Gregorian date to initialize the
	 * other private memebers of the class (Persian and Julian dates).
	 */
	public PersianDateTime() {
		Calendar calendar = new GregorianCalendar();
		setGregorianDateTime(
				calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH) + 1,
				calendar.get(Calendar.DAY_OF_MONTH),
				calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE),
				calendar.get(Calendar.SECOND),
				calendar.get(Calendar.MILLISECOND),
				null
		);
	}

	/**
	 * JavaSource_Calendar:
	 * This constructor receives a Gregorian date and initializes the other private
	 * members of the class accordingly.
	 *
	 * @param year  int
	 * @param month int
	 * @param day   int
	 */
	public PersianDateTime(int year, int month, int day, boolean timeNow) {
		setPersianDate(year, month, day, timeNow);
	}

	/**
	 * JavaSource_Calendar:
	 * This constructor receives a Gregorian date and initializes the other private
	 * members of the class accordingly.
	 *
	 * @param year  int
	 * @param month int
	 * @param day   int
	 */
	public PersianDateTime(int year, int month, int day, int hour, int minute, int second, int millisecond, TimeZone timeZone) {
		setPersianDateTime(year, month, day, hour, minute, second, millisecond, timeZone);
	}

	public PersianDateTime(long epoch) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(epoch);
		setGregorianDateTime(
				calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH) + 1,
				calendar.get(Calendar.DAY_OF_MONTH),
				calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE),
				calendar.get(Calendar.SECOND),
				calendar.get(Calendar.MILLISECOND),
				null
		);
	}

	/**
	 * getPersianYear:
	 * Returns the 'year' part of the Persian date.
	 *
	 * @return int
	 */
	public int getPersianYear() {
		return persianYear;
	}

	/**
	 * getPersianMonthIndexFrom1:
	 * Returns the 'month' part of the Persian date.
	 *
	 * @return int
	 */
	public int getPersianMonthIndexFrom1() {
		return persianMonth;
	}

	public PersianMonths getPersianMonth() {
		return PersianMonths.ByIndex(getPersianMonthIndexFrom1());
	}

	/**
	 * getPersianDay:
	 * Returns the 'day' part of the Persian date.
	 *
	 * @return int
	 */
	public int getPersianDay() {
		return persianDay;
	}

	/**
	 * getGregorianYear:
	 * Returns the 'year' part of the Gregorian date.
	 *
	 * @return int
	 */
	public int getGregorianYear() {
		return gregorianYear;
	}

	/**
	 * getGregorianMonth:
	 * Returns the 'month' part of the Gregorian date.
	 *
	 * @return int
	 */
	public int getGregorianMonth() {
		return gregorianMonth;
	}

	/**
	 * getGregorianDay:
	 * Returns the 'day' part of the Gregorian date.
	 *
	 * @return int
	 */
	public int getGregorianDay() {
		return gregorianDay;
	}

	/**
	 * getJulianYear:
	 * Returns the 'year' part of the Julian date.
	 *
	 * @return int
	 */
	public int getJulianYear() {
		return julianYear;
	}

	/**
	 * getJulianMonth:
	 * Returns the 'month' part of the Julian date.
	 *
	 * @return int
	 */
	public int getJulianMonth() {
		return julianMonth;
	}

	/**
	 * getJulianDay()
	 * Returns the 'day' part of the Julian date.
	 *
	 * @return int
	 */
	public int getJulianDay() {
		return julianDay;
	}

	/**
	 * getPersianDate:
	 * Returns a string version of Persian date
	 *
	 * @return String
	 */
	public String getPersianDateStandard() {

		return getPersianDateStandard(true);
	}

	public String getPersianDateStandard(boolean persianNumbers, boolean withDayName) {
		return (withDayName ? (persianNumbers ? getPersianWeekDayName() : getGregorianWeekDayName()) : "") + " " + getPersianDateStandard(persianNumbers);
	}


	/**
	 * @param persianNumbers
	 * @return
	 */
	public String getPersianDateStandard(boolean persianNumbers) {
		String date = FarayanUtility.LeadingZero(getPersianYear(), 4) + "/" + FarayanUtility.LeadingZero(getPersianMonthIndexFrom1(), 2) + "/" + FarayanUtility.LeadingZero(getPersianDay(), 2);
		return persianNumbers ? FarayanUtility.ConvertNumbersToPersian(date) : date;
	}

	/**
	 * @param persianNumbers
	 * @return
	 */
	public String getPersianDateSentence(boolean persianNumbers) {
		String result = String.format("%s %s %s %s", getPersianWeekDayName(), getPersianDay(), getPersianMonthName(), getPersianYear());
		if (persianNumbers)
			result = FarayanUtility.ConvertNumbersToPersian(result);
		return result;
	}

	/**
	 * @return
	 */
	public String getPersianDateTimeStandard() {
		return getPersianDateTimeStandard(true);
	}

	/**
	 * @param persianNumbers
	 * @return
	 */
	public String getPersianDateTimeStandard(boolean persianNumbers) {
		return getPersianDateTimeStandard(persianNumbers, " ");
	}

	/**
	 * @param persianNumbers
	 * @return
	 */
	public String getPersianDateTimeStandard(boolean persianNumbers, boolean withDayName) {
		return (withDayName ? (persianNumbers ? getPersianWeekDayName() : getGregorianWeekDayName()) : "") + " " + getPersianDateTimeStandard(persianNumbers, " ");
	}

	/**
	 * @param persianNumbers
	 * @return
	 */
	public String getPersianDateTimeStandard(boolean persianNumbers, String separator) {
		return getPersianDateStandard(persianNumbers) + " " + getTimeText(persianNumbers);
	}

	/**
	 * @return
	 */
	public String getTimeText() {
		return getTimeText(true);
	}

	public String getTimeText(boolean persianNumbers) {
		String time = FarayanUtility.LeadingZero(getHour(), 2) + ":" + FarayanUtility.LeadingZero(getMinute(), 2) + ":" + FarayanUtility.LeadingZero(getSecond(), 2);
		return persianNumbers ? FarayanUtility.ConvertNumbersToPersian(time) : time;
	}

	/**
	 * @param persianNumbers
	 * @return
	 */
	public String getPersianDateTimeSentence(boolean persianNumbers) {
		/**
		 * Version 1.1 (2)
		 * - ساعت، دقیقه و ثانیه دو عددی شدند
		 */
		String result = getPersianDateSentence(false) + " ساعت " + FarayanUtility.LeadingZero(getHour(), 2) + ":" + FarayanUtility.LeadingZero(getMinute(), 2) + ":" + FarayanUtility.LeadingZero(getSecond(), 2);
		if (persianNumbers)
			result = FarayanUtility.ConvertNumbersToPersian(result);
		return result;
	}

	/**
	 * getGregorianDate:
	 * Returns a string version of Gregorian date
	 *
	 * @return String
	 */
	public String getGregorianDateStandard() {
		return (getGregorianYear() + "/" + getGregorianMonth() + "/" + getGregorianDay());
	}

	/**
	 * تاریخ میلادی را با ساعت به صورت استاندارد زیر برمی‌گرداند
	 * yyyy/mm/dd hh:mm:ss.ms
	 *
	 * @return
	 */
	public String getGregorianDateTimeStandard() {
		return (getGregorianYear() + "/" + getGregorianMonth() + "/" + getGregorianDay()) + " " + getTimeText(false);
	}

	public String getGregorianDateTimeStandard(String dateSeparator, String dateTimeSeparator, String timeSeparator, boolean includeMillisecond, String timeMillisecondSeparator, boolean localizeNumbers, boolean leadingZero) {
		String output = "";
		output += FarayanUtility.LeadingZero(getGregorianYear(), leadingZero ? 4 : 0);
		output += dateSeparator;
		output += FarayanUtility.LeadingZero(getGregorianMonth(), leadingZero ? 2 : 0);
		output += dateSeparator;
		output += FarayanUtility.LeadingZero(getGregorianDay(), leadingZero ? 2 : 0);
		output += dateTimeSeparator;
		output += FarayanUtility.LeadingZero(getHour(), leadingZero ? 2 : 0);
		output += timeSeparator;
		output += FarayanUtility.LeadingZero(getMinute(), leadingZero ? 2 : 0);
		output += timeSeparator;
		output += FarayanUtility.LeadingZero(getSecond(), leadingZero ? 2 : 0);
		if (includeMillisecond && FarayanUtility.IsUsable(timeMillisecondSeparator)) {
			output += timeMillisecondSeparator;
			output += FarayanUtility.LeadingZero(getMillisecond(), leadingZero ? 3 : 0);
		}
		if (localizeNumbers)
			output = FarayanUtility.ConvertNumbersToPersian(output);
		return output;
	}


	/**
	 * getJulianDate:
	 * Returns a string version of Julian date
	 *
	 * @return String
	 */
	public String getJulianDateStandard() {
		return (getJulianYear() + "/" + getJulianMonth() + "/" + getJulianDay());
	}

	/**
	 * getWeekDayStr:
	 * Returns the week day name.
	 *
	 * @return String
	 */
	public String getGregorianWeekDayName() {
		String weekDayStr[] = {
				"Monday",
				"Tuesday",
				"Wednesday",
				"Thursday",
				"Friday",
				"Saturday",
				"Sunday"};
		return (weekDayStr[getDayOfWeek()]);
	}

	public String getPersianWeekDayName() {
		String weekDayStr[] = {
				"دوشنبه",
				"سه‌شنبه",
				"چهارشنبه",
				"پنج‌شنبه",
				"آدینه",
				"شنبه",
				"یک‌شنبه"};
		return (weekDayStr[getDayOfWeek()]);
	}

	public String getPersianMonthName() {
		String monthNames[] = {
				"فروردین",
				"اردیبهشت",
				"خرداد",
				"تیر",
				"مرداد",
				"شهریور",
				"مهر",
				"آبان",
				"آذر",
				"دی",
				"بهمن",
				"اسفند",
		};
		return (monthNames[getPersianMonthIndexFrom1() - 1]);
	}

	/**
	 * toString:
	 * Overrides the default toString() method to return all dates.
	 *
	 * @return String
	 */
	public String toString() {
		return getPersianDateSentence(true);
	}


	/**
	 * getDayOfWeek:
	 * Returns the week day number. Monday=0..Sunday=6;
	 *
	 * @return int
	 */
	public int getDayOfWeek() {
		return (JDN % 7);
	}

	/**
	 * setPersianDate:
	 * Sets the date according to the Persian calendar and adjusts the other dates.
	 *
	 * @param year    int
	 * @param month   int
	 * @param day     int
	 * @param timeNow
	 */
	public void setPersianDate(int year, int month, int day, boolean timeNow) {
		if (timeNow) {
			int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			int minute = Calendar.getInstance().get(Calendar.MINUTE);
			int second = Calendar.getInstance().get(Calendar.SECOND);
			int millisecond = Calendar.getInstance().get(Calendar.MILLISECOND);
			setPersianDateTime(year, month, day, hour, minute, second, millisecond, null);
		} else {
			setPersianDateTime(year, month, day, 0, 0, 0, 0, null);
		}
	}

	public void setPersianDateTime(int year, int month, int day, int hour, int minute, int second, int millisecond, TimeZone timeZone) {
		persianYear = year;
		persianMonth = month;
		persianDay = day;
		JDN = PersianDateToJDN();
		JDNToPersian();
		JDNToJulian();
		JDNToGregorian();

		if (timeZone == null)
			timeZone = TimeZone.getDefault();
		ActiveTimeZone = timeZone;
		TimeHour = hour;
		TimeMinute = minute;
		TimeSecond = second;
		TimeMillisecond = millisecond;

		CalculateEpoch();
	}

	/**
	 * setGregorianDate:
	 * Sets the date according to the Gregorian calendar and adjusts the other dates.
	 *
	 * @param year  int
	 * @param month int
	 * @param day   int
	 */
	public void setGregorianDate(int year, int month, int day, boolean timeNow) {
		if (timeNow) {
			int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			int minute = Calendar.getInstance().get(Calendar.MINUTE);
			int second = Calendar.getInstance().get(Calendar.SECOND);
			int millisecond = Calendar.getInstance().get(Calendar.MILLISECOND);
			setGregorianDateTime(year, month, day, hour, minute, second, millisecond, null);
		} else {
			setGregorianDateTime(year, month, day, 0, 0, 0, 0, null);
		}
	}

	public void setGregorianDateTime(int year, int month, int day, int hour, int minute, int second, int millisecond, TimeZone timeZone) {
		gregorianYear = year;
		gregorianMonth = month;
		gregorianDay = day;

		JDN = gregorianDateToJDN(year, month, day);
		JDNToPersian();
		JDNToJulian();
		JDNToGregorian();

		if (timeZone == null)
			timeZone = TimeZone.getDefault();
		ActiveTimeZone = timeZone;
		TimeHour = hour;
		TimeMinute = minute;
		TimeSecond = second;
		TimeMillisecond = millisecond;

		CalculateEpoch();
	}

	/**
	 * setJulianDate:
	 * Sets the date according to the Julian calendar and adjusts the other dates.
	 *
	 * @param year  int
	 * @param month int
	 * @param day   int
	 */
	public void setJulianDate(int year, int month, int day) {
		julianYear = year;
		julianMonth = month;
		julianDay = day;

		JDN = julianDateToJDN(year, month, day);
		JDNToPersian();
		JDNToJulian();
		JDNToGregorian();

		CalculateEpoch();
	}

	/**
	 * PersianCalendar:
	 * This method determines if the Persian (Jalali) year is leap (366-day long)
	 * or is the common year (365 days), and finds the day in March (Gregorian
	 * Calendar)of the first day of the Persian year ('persianYear').Persian year (persianYear)
	 * ranges from (-61 to 3177).This method will set the following private data
	 * members as follows:
	 * leap: Number of years since the last leap year (0 to 4)
	 * Gy: Gregorian year of the begining of Persian year
	 * march: The March day of Farvardin the 1st (first day of jaYear)
	 */
	private void PersianCalendar() {
		// Persian years starting the 33-year rule
		int Breaks[] = {-61, 9, 38, 199, 426, 686, 756, 818, 1111, 1181, 1210, 1635, 2060, 2097, 2192, 2262, 2324, 2394, 2456, 3178};
		int jm, N, leapJ, leapG, jp, j, jump;
		gregorianYear = persianYear + 621;
		leapJ = -14;
		jp = Breaks[0];
		// Find the limiting years for the Persian year 'persianYear'
		j = 1;
		do {
			jm = Breaks[j];
			jump = jm - jp;
			if (persianYear >= jm) {
				leapJ += (jump / 33 * 8 + (jump % 33) / 4);
				jp = jm;
			}
			j++;
		} while ((j < 20) && (persianYear >= jm));
		N = persianYear - jp;
		// Find the number of leap years from AD 621 to the begining of the current
		// Persian year in the Persian (Jalali) calendar
		leapJ += (N / 33 * 8 + ((N % 33) + 3) / 4);
		if (((jump % 33) == 4) && ((jump - N) == 4))
			leapJ++;
		// And the same in the Gregorian date of Farvardin the first
		leapG = gregorianYear / 4 - ((gregorianYear / 100 + 1) * 3 / 4) - 150;
		march = 20 + leapJ - leapG;
		// Find how many years have passed since the last leap year
		if ((jump - N) < 6)
			N = N - jump + ((jump + 4) / 33 * 33);
		leap = (((N + 1) % 33) - 1) % 4;
		if (leap == -1)
			leap = 4;
	}


	/**
	 * IsLeap:
	 * This method determines if the Persian (Jalali) year is leap (366-day long)
	 * or is the common year (365 days), and finds the day in March (Gregorian
	 * Calendar)of the first day of the Persian year ('persianYear').Persian year (persianYear)
	 * ranges from (-61 to 3177).This method will set the following private data
	 * members as follows:
	 * leap: Number of years since the last leap year (0 to 4)
	 * Gy: Gregorian year of the begining of Persian year
	 * march: The March day of Farvardin the 1st (first day of jaYear)
	 */
	private boolean IsLeap(int irYear1) {
		// Persian years starting the 33-year rule
		int Breaks[] = {-61, 9, 38, 199, 426, 686, 756, 818, 1111, 1181, 1210, 1635, 2060, 2097, 2192, 2262, 2324, 2394, 2456, 3178};
		int jm, N, leapJ, leapG, jp, j, jump;
		gregorianYear = irYear1 + 621;
		leapJ = -14;
		jp = Breaks[0];
		// Find the limiting years for the Persian year 'persianYear'
		j = 1;
		do {
			jm = Breaks[j];
			jump = jm - jp;
			if (irYear1 >= jm) {
				leapJ += (jump / 33 * 8 + (jump % 33) / 4);
				jp = jm;
			}
			j++;
		} while ((j < 20) && (irYear1 >= jm));
		N = irYear1 - jp;
		// Find the number of leap years from AD 621 to the begining of the current
		// Persian year in the Persian (Jalali) calendar
		leapJ += (N / 33 * 8 + ((N % 33) + 3) / 4);
		if (((jump % 33) == 4) && ((jump - N) == 4))
			leapJ++;
		// And the same in the Gregorian date of Farvardin the first
		leapG = gregorianYear / 4 - ((gregorianYear / 100 + 1) * 3 / 4) - 150;
		march = 20 + leapJ - leapG;
		// Find how many years have passed since the last leap year
		if ((jump - N) < 6)
			N = N - jump + ((jump + 4) / 33 * 33);
		leap = (((N + 1) % 33) - 1) % 4;
		if (leap == -1)
			leap = 4;
		if (leap == 4 || leap == 0)
			return true;
		else
			return false;
	}


	/**
	 * PersianDateToJDN:
	 * Converts a date of the Persian calendar to the Julian Dey Number. It first
	 * invokes the 'PersianCalender' private method to convert the Persian date to
	 * Gregorian date and then returns the Julian Dey Number based on the Gregorian
	 * date. The Persian date is obtained from 'persianYear'(1-3100),'persianMonth'(1-12) and
	 * 'persianDay'(1-29/31).
	 *
	 * @return long (Julian Dey Number)
	 */
	private int PersianDateToJDN() {
		PersianCalendar();
		return (gregorianDateToJDN(gregorianYear, 3, march) + (persianMonth - 1) * 31 - persianMonth / 7 * (persianMonth - 7) + persianDay - 1);
	}

	/**
	 * JDNToPersian:
	 * Converts the current value of 'JDN' Julian Dey Number to a date in the
	 * Persian calendar. The caller should make sure that the current value of
	 * 'JDN' is set correctly. This method first converts the JDN to Gregorian
	 * calendar and then to Persian calendar.
	 */
	private void JDNToPersian() {
		JDNToGregorian();
		persianYear = gregorianYear - 621;
		PersianCalendar(); // This invocation will update 'leap' and 'march'
		int JDN1F = gregorianDateToJDN(gregorianYear, 3, march);
		int k = JDN - JDN1F;
		if (k >= 0) {
			if (k <= 185) {
				persianMonth = 1 + k / 31;
				persianDay = (k % 31) + 1;
				return;
			} else
				k -= 186;
		} else {
			persianYear--;
			k += 179;
			if (leap == 1)
				k++;
		}
		persianMonth = 7 + k / 30;
		persianDay = (k % 30) + 1;
	}


	/**
	 * julianDateToJDN:
	 * Calculates the julian day number (JDN) from Julian calendar dates. This
	 * integer number corresponds to the noon of the date (i.e. 12 hours of
	 * Universal Time). This method was tested to be good (valid) since 1 March,
	 * -100100 (of both calendars) up to a few millions (10^6) years into the
	 * future. The algorithm is based on D.A.Hatcher, Q.Jl.R.Astron.Soc. 25(1984),
	 * 53-55 slightly modified by K.M. Borkowski, Post.Astron. 25(1987), 275-279.
	 *
	 * @param year  int
	 * @param month int
	 * @param day   int
	 * @return int
	 */
	private int julianDateToJDN(int year, int month, int day) {
		return (year + (month - 8) / 6 + 100100) * 1461 / 4 + (153 * ((month + 9) % 12) + 2) / 5 + day - 34840408;
	}

	/**
	 * JDNToJulian:
	 * Calculates Julian calendar dates from the julian day number (JDN) for the
	 * period since JDN=-34839655 (i.e. the year -100100 of both calendars) to
	 * some millions (10^6) years ahead of the present. The algorithm is based on
	 * D.A. Hatcher, Q.Jl.R.Astron.Soc. 25(1984), 53-55 slightly modified by K.M.
	 * Borkowski, Post.Astron. 25(1987), 275-279).
	 */
	private void JDNToJulian() {
		int j = 4 * JDN + 139361631;
		int i = ((j % 1461) / 4) * 5 + 308;
		julianDay = (i % 153) / 5 + 1;
		julianMonth = ((i / 153) % 12) + 1;
		julianYear = j / 1461 - 100100 + (8 - julianMonth) / 6;
	}

	/**
	 * gergorianDateToJDN:
	 * Calculates the julian day number (JDN) from Gregorian calendar dates. This
	 * integer number corresponds to the noon of the date (i.e. 12 hours of
	 * Universal Time). This method was tested to be good (valid) since 1 March,
	 * -100100 (of both calendars) up to a few millions (10^6) years into the
	 * future. The algorithm is based on D.A.Hatcher, Q.Jl.R.Astron.Soc. 25(1984),
	 * 53-55 slightly modified by K.M. Borkowski, Post.Astron. 25(1987), 275-279.
	 *
	 * @param year  int
	 * @param month int
	 * @param day   int
	 * @return int
	 */
	private int gregorianDateToJDN(int year, int month, int day) {
		int jdn = (year + (month - 8) / 6 + 100100) * 1461 / 4 + (153 * ((month + 9) % 12) + 2) / 5 + day - 34840408;
		jdn = jdn - (year + 100100 + (month - 8) / 6) / 100 * 3 / 4 + 752;
		return (jdn);
	}

	/**
	 * JDNToGregorian:
	 * Calculates Gregorian calendar dates from the julian day number (JDN) for
	 * the period since JDN=-34839655 (i.e. the year -100100 of both calendars) to
	 * some millions (10^6) years ahead of the present. The algorithm is based on
	 * D.A. Hatcher, Q.Jl.R.Astron.Soc. 25(1984), 53-55 slightly modified by K.M.
	 * Borkowski, Post.Astron. 25(1987), 275-279).
	 */
	private void JDNToGregorian() {
		int j = 4 * JDN + 139361631;
		j = j + (((((4 * JDN + 183187720) / 146097) * 3) / 4) * 4 - 3908);
		int i = ((j % 1461) / 4) * 5 + 308;
		gregorianDay = (i % 153) / 5 + 1;
		gregorianMonth = ((i / 153) % 12) + 1;
		gregorianYear = j / 1461 - 100100 + (8 - gregorianMonth) / 6;
	}


	private int persianYear; // Year part of a Persian date
	private int persianMonth; // Month part of a Persian date
	private int persianDay; // Dey part of a Persian date
	private int gregorianYear; // Year part of a Gregorian date
	private int gregorianMonth; // Month part of a Gregorian date
	private int gregorianDay; // Dey part of a Gregorian date
	private int julianYear; // Year part of a Julian date
	private int julianMonth; // Month part of a Julian date
	private int julianDay; // Dey part of a Julian date
	private int leap; // Number of years since the last leap year (0 to 4)
	private int JDN; // Julian Dey Number
	private int march; // The march day of Farvardin the first (First day of jaYear)

	public int getHour() {
		return TimeHour;
	}

	public void setHour(int hour) {
		setHour(hour, true);
	}

	public int getMinute() {
		return TimeMinute;
	}

	public void setMinute(int minute) {
		setMinute(minute, true);
	}

	public int getSecond() {
		return TimeSecond;
	}

	public void setSecond(int second) {
		setSecond(second, true);
	}

	public int getMillisecond() {
		return TimeMillisecond;
	}

	public void setMillisecond(int millisecond) {
		setMillisecond(millisecond, true);
	}

	private int TimeHour;
	private int TimeMinute;
	private int TimeSecond;
	private int TimeMillisecond;
	private long epoch = 0;

	public java.util.TimeZone getTimeZone() {
		return ActiveTimeZone;
	}

	public void setTimeZone(java.util.TimeZone timeZone) {
		ActiveTimeZone = timeZone;
	}

	private TimeZone ActiveTimeZone;

	public long getEpoch() {
		return epoch;
	}

	public void setEpoch(long epoch) {
		this.epoch = epoch;
	}

	public PersianDateTime changeTimeZone(TimeZone zone) {

		if (ActiveTimeZone == null)
			ActiveTimeZone = TimeZone.getDefault();
		if (zone.getID().equalsIgnoreCase(ActiveTimeZone.getID()))
			return this;

		int change = zone.getRawOffset() - ActiveTimeZone.getRawOffset();
		Date date = new Date(epoch);
		if (zone.inDaylightTime(date))
			change += zone.getDSTSavings();
		if (ActiveTimeZone.inDaylightTime(date))
			change -= ActiveTimeZone.getDSTSavings();
		int newHour = TimeHour + (change / oneHourMilliseconds);
		int newMinute = TimeMinute + ((change % oneHourMilliseconds) / oneMinuteMilliseconds);
		int newSecond = TimeSecond + ((change % oneMinuteMilliseconds) / 1000);

		if (newSecond > 59) {
			newMinute += newSecond / 60;
			newSecond %= 60;
		}
		if (newSecond < 0) {
			newMinute--;
			newSecond += 60;
		}

		if (newMinute > 59) {
			newHour += newMinute / 60;
			newMinute %= 60;
		}
		if (newMinute < 0) {
			newHour--;
			newMinute += 60;
		}

		if (newHour > 23) {
			JDN += newHour / 24;
			JDNToGregorian();
			JDNToPersian();
			JDNToJulian();
			newHour %= 24;
		}

		if (newHour < 0) {
			JDN--;
			JDNToGregorian();
			JDNToPersian();
			JDNToJulian();
			newHour += 24;
		}

		TimeHour = newHour;
		TimeMinute = newMinute;
		TimeSecond = newSecond;
		ActiveTimeZone = zone;

		return this;
	}

	public void setTimeInMillis(long timeInMillis) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTimeInMillis(timeInMillis);
		setGregorianDateTime(
				gc.get(Calendar.YEAR),
				gc.get(Calendar.MONTH) + 1,
				gc.get(Calendar.DAY_OF_MONTH),
				gc.get(Calendar.HOUR_OF_DAY),
				gc.get(Calendar.MINUTE),
				gc.get(Calendar.SECOND),
				0,
				null
		);
	}

	protected void CalculateEpoch() {
		if (ActiveTimeZone == null)
			ActiveTimeZone = ActiveTimeZone.getDefault();

		GregorianCalendar gc = new GregorianCalendar();
		gc.setTimeZone(getTimeZone());
		gc.set(getGregorianYear(), getGregorianMonth() - 1, getGregorianDay(), getHour(), getMinute(), getSecond());
		gc.set(Calendar.MILLISECOND, getMillisecond());
		epoch = gc.getTimeInMillis();
	}

	public boolean IsPersianLeapYear() {
		return IsLeap(getPersianYear());
	}

	/**
	 * به تعداد معینی سال، ماه، روز، ساعت، دقیقه، ثانیه و میلی‌ثانیه به تاریخ فعلی ایرانی اضافه می‌کد
	 * هر کدام از مقادیر اضافه شده می‌تواند منفی هم باشد
	 * بنابراین برای کم کردن عدد منفی بفرستید
	 * اعداد لازم نیست در محدوده استاندارد باشند
	 * مثلا لازم نیست دقیقه حتما بین ۰ تا ۵۹ باشد
	 * بلکه می‌تواند هر مقداری باشد
	 * در صورتی که از محدوده بیشتر بشود، اضافی آن به صورت ساعت افزوده می‌شود
	 * این گزینه برای ثانیه، دقیقه و .... هم درست است
	 *
	 * @param years
	 * @param months
	 * @param days
	 * @param hours
	 * @param minutes
	 * @param seconds
	 * @param milliseconds
	 */
	public PersianDateTime addPersian(int years, int months, int days, long hours, long minutes, long seconds, long milliseconds) {
		persianYear += years;
		persianMonth += months;
		if (persianMonth > 12) {
			persianYear += persianMonth / 12;
			persianMonth %= 12;
		}
		JDN = PersianDateToJDN();
		JDNToPersian();
		JDNToJulian();
		JDNToGregorian();

		long newMilliseconds = getMillisecond() + milliseconds;
		if (newMilliseconds >= 1000) {
			seconds += (newMilliseconds / 1000);
			newMilliseconds = newMilliseconds % 1000;
		}
		if (newMilliseconds < 0) {
			seconds += ((newMilliseconds / 1000) - 1);
			newMilliseconds = Math.abs(((newMilliseconds / 1000) - 1)) * 1000 + newMilliseconds;
		}

		long newSecond = getSecond() + seconds;
		if (newSecond >= 60) {
			minutes += (newSecond / 60);
			newSecond = newSecond % 60;
		}
		if (newSecond < 0) {
			minutes += ((newSecond / 60) - 1);
			newSecond = Math.abs(((newSecond / 60) - 1)) * 60 + newSecond;
		}

		long newMinute = getMinute() + minutes;
		if (newMinute >= 60) {
			hours += (newMinute / 60);
			newMinute = newMinute % 60;
		}
		if (newMinute < 0) {
			hours += ((newMinute / 60) - 1);
			newMinute = Math.abs(((newMinute / 60) - 1)) * 60 + newMinute;
		}


		long newHour = getHour() + hours;
		if (newHour >= 24) {
			days += (newHour / 24);
			newHour = newHour % 24;
		}
		if (newHour < 0) {
			days += ((newHour / 24) - 1);
			newHour = Math.abs((newHour / 24) - 1) * 24 + newHour;
		}

		if (days != 0) {
			JDN += days;
			JDNToPersian();
			JDNToJulian();
			JDNToGregorian();
		}

		setHour((int) newHour, false);
		setMinute((int) newMinute, false);
		setSecond((int) newSecond, false);
		setMillisecond((int) newMilliseconds, false);
		CalculateEpoch();

		return this;
	}

	public PersianDateTime addPersianYear(int years) {
		addPersian(years, 0, 0, 0, 0, 0, 0);
		return this;
	}

	public PersianDateTime addPersianMonths(int months) {
		addPersian(0, months, 0, 0, 0, 0, 0);
		return this;
	}

	/**
	 * nextDay:
	 * Overload the nextDay() method to accept the number of days to go ahead and
	 * adjusts the other dates accordingly.
	 *
	 * @param days int
	 */
	public PersianDateTime addDays(int days) {
		JDN += days;
		JDNToPersian();
		JDNToJulian();
		JDNToGregorian();
		CalculateEpoch();
		return this;
	}

	public PersianDateTime addHours(long hours) {
		addPersian(0, 0, 0, hours, 0, 0, 0);
		return this;
	}

	public PersianDateTime addMinutes(long minutes) {
		addPersian(0, 0, 0, 0, minutes, 0, 0);
		return this;
	}

	public PersianDateTime addSeconds(long seconds) {
		addPersian(0, 0, 0, 0, 0, seconds, 0);
		return this;
	}

	public PersianDateTime addMilliseconds(long milliseconds) {
		addPersian(0, 0, 0, 0, 0, 0, milliseconds);
		return this;
	}

	protected PersianDateTime setHour(int hour, boolean calculate) {
		if (hour == TimeHour)
			return this;
		if (hour < 0 || hour > 23)
			throw new RuntimeException("Hour must be between 0 and 23 not " + hour);
		TimeHour = hour;
		if (calculate)
			CalculateEpoch();
		return this;
	}

	protected PersianDateTime setMinute(Integer minute, boolean calculate) {
		if (minute == TimeMinute)
			return this;
		if (minute < 0 || minute > 59)
			throw new RuntimeException("Minute must be between 0 and 59 not " + minute);
		TimeMinute = minute;
		if (calculate)
			CalculateEpoch();
		return this;
	}

	protected PersianDateTime setSecond(Integer second, boolean calculate) {
		if (second == TimeSecond)
			return this;
		if (second < 0 || second > 59)
			throw new RuntimeException("Second must be between 0 and 59 not " + second);
		TimeSecond = second;
		if (calculate)
			CalculateEpoch();
		return this;
	}

	protected PersianDateTime setMillisecond(Integer millisecond, boolean calculate) {
		if (millisecond == TimeMillisecond)
			return this;
		if (millisecond < 0 || millisecond > 999)
			throw new RuntimeException("Millisecond must be between 0 and 999 not " + millisecond);
		TimeMillisecond = millisecond;
		if (calculate)
			CalculateEpoch();
		return this;
	}

	public PersianDateTime setTime(Integer hour, Integer minute, Integer second, Integer millisecond) {
		setHour(hour, false);
		setMinute(minute, false);
		setSecond(second, false);
		setMillisecond(millisecond, false);
		CalculateEpoch();
		return this;
	}

	public WeekDays PersianWeekDay() {
		return WeekDays.ByGregorianWeekDayIndex(getDayOfWeek());
	}

	static final int oneHourMilliseconds = 3600 * 1000;
	static final int oneMinuteMilliseconds = 60 * 1000;

	public int PersianMonthWeekIndex() {
		PersianCalendar monthStart = new PersianCalendar(getPersianYear(), getPersianMonthIndexFrom1(), 1, 0, 0, 0, 0, null);
		switch (monthStart.PersianWeekDay()) {
			case Saturday:
				return ((getPersianDay() - 1) / 7) + 1;
			case Sunday:
			case Monday:
			case Tuesday:
			case Wednesday:
			case Thursday:
			case Friday:
				return ((getPersianDay() - 1) / 7) + (PersianWeekDay().PersianDayIndex < monthStart.PersianWeekDay().PersianDayIndex ? 1 : 0);
			default:
				throw new RuntimeException(String.format("Weekday %s is not supported", monthStart.PersianWeekDay()));
		}
	}

	public MonthWeeks PersianMonthWeek() {
		return MonthWeeks.ByIndex(PersianMonthWeekIndex());
	}

	public Integer getPersianYearDay() {
		if (getPersianMonthIndexFrom1() < 7)
			return (getPersianMonthIndexFrom1() - 1) * 31 + getPersianDay();
		if (getPersianMonthIndexFrom1() < 12)
			return 6 * 31 + (getPersianMonthIndexFrom1() - 1 - 6) * 30 + getPersianDay();
		return 6 * 31 + 5 * 30 + getPersianDay();
	}

	/**
	 * @param persianCalendar
	 * @param persianNumbers
	 * @return
	 */
	public static String getPersianDateSentence(PersianCalendar persianCalendar, boolean persianNumbers, String defaultText) {
		return persianCalendar == null ? defaultText : persianCalendar.getPersianDateSentence(persianNumbers);
	}

	/**
	 * @param persianCalendar
	 * @param persianNumbers
	 * @return
	 */
	public static String getPersianDateTimeSentence(PersianCalendar persianCalendar, boolean persianNumbers, String defaultText) {
		return persianCalendar == null ? defaultText : persianCalendar.getPersianDateTimeSentence(persianNumbers);
	}


	/**
	 * @return
	 */
	public static String getPersianDateStandard(PersianCalendar persianCalendar, boolean persianNumbers, String defaultText) {
		return persianCalendar == null ? defaultText : persianCalendar.getPersianDateStandard(persianNumbers);
	}

	/**
	 * @param persianCalendar
	 * @return
	 */
	public static String getPersianDateTimeStandard(PersianCalendar persianCalendar, boolean persianNumbers, String defaultText) {
		return persianCalendar == null ? defaultText : persianCalendar.getPersianDateTimeStandard(persianNumbers);
	}

	public static int PersianMonthByPersianYearDay(int yearDay) {
		if (yearDay <= 31)
			return 1;
		if (yearDay <= 6 * 31)
			return (yearDay / 31) + 1;
		if (yearDay <= (6 * 31 + 5 * 30))
			return (6 + (yearDay - 186) / 30) + 1;
		return 12;
	}

	public static int PersianDayByPersianYearDay(int yearDay) {
		if (yearDay <= 6 * 31)
			return yearDay % 31;
		if (yearDay <= (6 * 31 + 5 * 30))
			return (yearDay - 186) % 30;
		return (yearDay - 186 - 150);
	}

	public int PersianWeekStartMonthDay(MonthWeeks week) {
		return PersianWeekStartMonthDay(week.ServerCode);
	}

	/**
	 * روز شروع هفته‌ی ارسالی در ماه را برمی‌گرداند
	 * مثلا اگر روز اول ماه، شنبه باشد، روز اول هفته ۱، ۱ خواهد بود
	 * به همین ترتیب، روز اول هفته ۲، ۸م خواهد بود
	 * اگر روز اول ماه غیر از روز شنبه باشد، مثلا روز یکشنبه، روز اول هفته ۱، ۷م خواهد بود
	 * روز اول هفته‌ی ۰م، همیشه ۱م خواهد بود
	 *
	 * @param week
	 * @return
	 */
	public int PersianWeekStartMonthDay(int week) {
		if (week == 0)
			return 1;
		WeekDays monthStartWeekDay = MonthStart().PersianWeekDay();
		int weekStartInMonth = 0;
		switch (monthStartWeekDay) {
			case Saturday:
				weekStartInMonth = 1 + (week - 1) * 7;
				break;
			default:
				weekStartInMonth = (week - 1) * 7 + (7 - monthStartWeekDay.PersianDayIndex) + 2;
				break;
		}

		return weekStartInMonth;
	}

	public PersianDateTime MonthStart() {
		return new PersianDateTime(getPersianYear(), getPersianMonthIndexFrom1(), 1, 0, 0, 0, 0, null);
	}

	public long getDayTime() {
		return getEpoch() - Day().getTimeInMillis();
	}

	public PersianCalendar Day() {
		return new PersianCalendar(getPersianYear(), getPersianMonthIndexFrom1(), getPersianDay(), false);
	}

	/**
	 * شروع هفته را در ماه محاسبه می‌کند
	 * مثلا اگر ماه در روز یکشنه شروع شود، به جای رور شنبه‌ی قبلش، روز یک شنبه را که شروع هفته‌ی صفرم ماه هست، برمی‌گرداند
	 *
	 * @return
	 */
	public PersianDateTime MonthWeekStart() {
		int persianMonthWeekIndex = PersianMonthWeekIndex();
		return new PersianDateTime(
				getPersianYear(),
				getPersianMonthIndexFrom1(),
				PersianWeekStartMonthDay(persianMonthWeekIndex),
				0,
				0,
				0,
				0,
				null
		);
	}

	public PersianDateTime WeekStart() {
		return Copy().addDays(1 - PersianWeekDay().PersianDayIndex).Day();
	}

	public PersianDateTime YearStart() {
		return new PersianDateTime(getPersianYear(), 1, 1, 0, 0, 0, 0, null);
	}

	public PersianDateTime Copy() {
		return new PersianDateTime(getEpoch());
	}

	/**
	 * دو تا تاریخ را مقایسه می‌کند و تاریخ کوچکتر را برمی‌گرداند
	 * در صورتی که هر دو تاریخ، برابر باشند، نول برمی‌گرداند
	 *
	 * @param val1
	 * @param val2
	 * @return
	 */
	public static PersianDateTime Min(PersianDateTime val1, PersianDateTime val2) {
		if (val1.getEpoch() < val2.getEpoch())
			return val1;
		if (val2.getEpoch() < val1.getEpoch())
			return val2;
		return null;
	}


	/**
	 * دو تا تاریخ را مقایسه می‌کند و تاریخ کوچکتر را برمی‌گرداند
	 * در صورتی که هر دو تاریخ، برابر باشند، اولی را برمی‌گرداند
	 *
	 * @param val1
	 * @param val2
	 * @return
	 */
	public static PersianDateTime MinOrFirst(PersianDateTime val1, PersianDateTime val2) {
		if (val1.getEpoch() < val2.getEpoch())
			return val1;
		if (val2.getEpoch() < val1.getEpoch())
			return val2;
		return val1;
	}


	/**
	 * دو تا تاریخ را مقایسه می‌کند و تاریخ کوچکتر را برمی‌گرداند
	 * در صورتی که هر دو تاریخ، برابر باشند، اولی را برمی‌گرداند
	 *
	 * @param val1
	 * @param val2
	 * @return
	 */
	public static PersianDateTime Max(PersianDateTime... values) {
		if (values == null)
			return null;
		PersianDateTime max = null;
		for (PersianDateTime value : values) {
			if (value == null)
				continue;
			if (max == null || max.IsBefore(value))
				max = value;
		}
		return max;
	}

	public boolean IsBefore(PersianDateTime other) {
		return getEpoch() < other.getEpoch();
	}

	public boolean IsAfter(PersianDateTime other) {
		return getEpoch() > other.getEpoch();
	}

	public boolean IsBeforeOrEqual(PersianDateTime other) {
		return getEpoch() <= other.getEpoch();
	}

	public boolean IsAfterOrEqual(PersianDateTime other) {
		return getEpoch() >= other.getEpoch();
	}

	public boolean IsEqual(PersianDateTime other) {
		return getEpoch() == other.getEpoch();
	}

	public String getPersianDateConcatenated() {
		return FarayanUtility.LeadingZero(getPersianYear(), 4)
				+ FarayanUtility.LeadingZero(getPersianMonthIndexFrom1(), 2)
				+ FarayanUtility.LeadingZero(getPersianDay(), 2);
	}

	public String getPersianDateTimeConcatenated(String dateTimeSeparator) {
		return FarayanUtility.LeadingZero(getPersianYear(), 4)
				+ FarayanUtility.LeadingZero(getPersianMonthIndexFrom1(), 2)
				+ FarayanUtility.LeadingZero(getPersianDay(), 2)
				+ dateTimeSeparator
				+ FarayanUtility.LeadingZero(getHour(), 2)
				+ FarayanUtility.LeadingZero(getMinute(), 2)
				+ FarayanUtility.LeadingZero(getSecond(), 2);

	}
}