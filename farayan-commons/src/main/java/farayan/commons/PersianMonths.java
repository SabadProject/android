package farayan.commons;

public enum PersianMonths
{
	Farvardin(R.string.MonthName01Farvardin, 1, 31),
	Ordibehesht(R.string.MonthName02Ordibehesht, 2, 31),
	Khordad(R.string.MonthName03Khordad, 3, 31),
	Tir(R.string.MonthName04Tir, 4, 31),
	Mordad(R.string.MonthName05Mordad, 5, 31),
	Shahrivar(R.string.MonthName06Shahrivar, 6, 31),
	Mehr(R.string.MonthName07Mehr, 7, 30),
	Aban(R.string.MonthName08Aban, 8, 30),
	Azar(R.string.MonthName09Azar, 9, 30),
	Dey(R.string.MonthName10Day, 10, 30),
	Bahman(R.string.MonthName11Bahman, 11, 30),
	Esfand(R.string.MonthName12Esfand, 12, 29);

	private static String[] m_indexNames;
	public final int NameResourceID;
	public final int Index;
	public final int CommonDaysCount;

	PersianMonths(int name, int index, int commonDaysCount) {
		this.NameResourceID = name;
		this.Index = index;
		CommonDaysCount = commonDaysCount;
	}

	public static String[] Names() {
		if (m_indexNames == null) {
			m_indexNames = new String[]{
					FarayanUtility.GlobalContext.getString(Farvardin.NameResourceID),
					FarayanUtility.GlobalContext.getString(Ordibehesht.NameResourceID),
					FarayanUtility.GlobalContext.getString(Khordad.NameResourceID),
					FarayanUtility.GlobalContext.getString(Tir.NameResourceID),
					FarayanUtility.GlobalContext.getString(Mordad.NameResourceID),
					FarayanUtility.GlobalContext.getString(Shahrivar.NameResourceID),
					FarayanUtility.GlobalContext.getString(Mehr.NameResourceID),
					FarayanUtility.GlobalContext.getString(Aban.NameResourceID),
					FarayanUtility.GlobalContext.getString(Azar.NameResourceID),
					FarayanUtility.GlobalContext.getString(Dey.NameResourceID),
					FarayanUtility.GlobalContext.getString(Bahman.NameResourceID),
					FarayanUtility.GlobalContext.getString(Esfand.NameResourceID),
			};
		}
		return m_indexNames;
	}

	public static PersianMonths ByIndex(int persianMonthIndex) {
		if (persianMonthIndex <= 0 || persianMonthIndex > 12)
			throw new RuntimeException(String.format("Index %s is not acceptable. Only indexes between 1 to 12 are valid", persianMonthIndex));
		for (PersianMonths month : values())
			if (month.Index == persianMonthIndex)
				return month;
		return null;
	}

	public static PersianMonths ByName(String name) {
		if (FarayanUtility.IsNullOrEmpty(name))
			return null;
		name = FarayanUtility.NormalizePersian(name);

		for (PersianMonths persianMonth : values()) {
			String monthName = FarayanUtility.GlobalContext.getResources().getString(persianMonth.NameResourceID);
			monthName = FarayanUtility.NormalizePersian(monthName);
			if (monthName.equalsIgnoreCase(name))
				return persianMonth;
		}
		return null;
	}

	public int DaysCount(PersianCalendar date) {
		switch (this) {
			case Esfand:
				return date.IsPersianLeapYear() ? 30 : 29;
			default:
				return CommonDaysCount;
		}
	}

	public static String[] TemplateNames(String template) {
		return new String[]{
				Farvardin.GenerateTemplate(template),
				Ordibehesht.GenerateTemplate(template),
				Khordad.GenerateTemplate(template),
				Tir.GenerateTemplate(template),
				Mordad.GenerateTemplate(template),
				Shahrivar.GenerateTemplate(template),
				Mehr.GenerateTemplate(template),
				Aban.GenerateTemplate(template),
				Azar.GenerateTemplate(template),
				Dey.GenerateTemplate(template),
				Bahman.GenerateTemplate(template),
				Bahman.GenerateTemplate(template),
				Esfand.GenerateTemplate(template),
		};
	}

	public final static String IndexPlaceHolder = "{index}";
	public final static String LeadingZeroIndexPlaceHolder = "{zeroLeadingIndex}";
	public final static String NamePlaceHolder = "{name}";

	/**
	 * @param template
	 * @return
	 */
	private String GenerateTemplate(String template) {
		if (FarayanUtility.IsNullOrEmpty(template))
			return "";
		template = template.replace(IndexPlaceHolder, this.Index + "");
		template = template.replace(LeadingZeroIndexPlaceHolder, FarayanUtility.LeadingZero(this.Index, 2));
		template = template.replace(NamePlaceHolder, FarayanUtility.GlobalContext.getString(NameResourceID));

		return template;
	}
}
