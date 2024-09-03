package farayan.commons;

import junit.framework.TestCase;

public class PersianDateTimeTest extends TestCase
{

	public void testGetPersianDateStandard() {
		long diff = 4104019644240000L - 1616744640000L;
		PersianDateTime persianDateTime = new PersianDateTime(4104019644240000L - 4102402899600000L);
		//String persianDateTimeSentence = persianDateTime.getPersianDateTimeSentence(true);
		//assertEquals("آدینه ۶ فروردین ۱۴۰۰ ساعت ۱۲:۱۴:۰۰", persianDateTimeSentence);
	}
}