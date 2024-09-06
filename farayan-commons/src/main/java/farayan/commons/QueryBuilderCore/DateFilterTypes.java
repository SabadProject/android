package farayan.commons.QueryBuilderCore;

public enum DateFilterTypes {
	Skip(0),
	Today(1),
	Yesterday(2),
	SpecificDay(3),
	SpecificDays(4),
	CurrentWeek(5),
	PreviousWeek(6),
	CurrentMonth(7),
	PreviousMonth(8),
	CurrentSeason(9),
	PreviousSeason(10),
	CurrentYear(11),
	PreviousYear(12);

	public final int ID;

	DateFilterTypes(int id) {
		ID = id;
	}

	public static DateFilterTypes FromID(int id) {
		for (DateFilterTypes type : values()) {
			if (type.ID == id)
				return type;
		}
		return null;
	}
}
