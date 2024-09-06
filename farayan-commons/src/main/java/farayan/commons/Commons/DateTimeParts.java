package farayan.commons.Commons;

import farayan.commons.FarayanUtility;

public enum DateTimeParts {
	Year(1, "y"),
	Month(2, "m"),
	Day(3, "d"),
	Hour(4, "h"),
	Minute(5, "mi"),
	Second(6, "s"),
	Millisecond(7, "ms"),

	;

	public final int Order;
	public final String Key;

	DateTimeParts(int order, String key) {
		Order = order;
		Key = key;
	}

	public static DateTimeParts ByName(String name) {
		for (DateTimeParts dateTimePrecision : values()) {
			if (name.equalsIgnoreCase(dateTimePrecision.name()))
				return dateTimePrecision;
		}
		return null;
	}

	public static DateTimeParts ByKey(String key) {
		if(FarayanUtility.IsNullOrEmpty(key))
			return null;
		for (DateTimeParts dateTimePrecision : values()) {
			if (key.equalsIgnoreCase(dateTimePrecision.Key))
				return dateTimePrecision;
		}
		return null;
	}
}
