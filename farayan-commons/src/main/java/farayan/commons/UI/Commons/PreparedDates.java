package farayan.commons.UI.Commons;

import farayan.commons.FarayanUtility;
import farayan.commons.PersianDateTime;

public enum PreparedDates {
	NextYearStart(0, new IPreparedDateTime() {
		@Override
		public String TextTemplate() {
			return "آغاز سال آینده: %s";
		}

		@Override
		public PersianDateTime Value() {
			return FarayanUtility.YearStart().addPersianYear(1);
		}
	}),
	NextSeasonStart(1, new IPreparedDateTime() {
		@Override
		public String TextTemplate() {
			return "آغاز فصل آینده: %s";
		}

		@Override
		public PersianDateTime Value() {
			return FarayanUtility.SeasonStart().addPersianMonths(3);
		}
	}),
	NextMonthStart(2, new IPreparedDateTime() {
		@Override
		public String TextTemplate() {
			return "آغاز ماه آینده: %s";
		}

		@Override
		public PersianDateTime Value() {
			return FarayanUtility.MonthStart().addPersianMonths(1);
		}
	}),
	NextWeekStart(3, new IPreparedDateTime() {
		@Override
		public String TextTemplate() {
			return "آغاز هفته آینده: %s";
		}

		@Override
		public PersianDateTime Value() {
			return FarayanUtility.WeekStart().addDays(7);
		}
	}),
	TomorrowFinish(4, new IPreparedDateTime() {
		@Override
		public String TextTemplate() {
			return "فردا آخر وقت: %s";
		}

		@Override
		public PersianDateTime Value() {
			return FarayanUtility.Tomorrow().addHours(17);
		}
	}),
	TomorrowStart(5, new IPreparedDateTime() {
		@Override
		public String TextTemplate() {
			return "فردا اول وقت: %s";
		}

		@Override
		public PersianDateTime Value() {
			return FarayanUtility.Tomorrow().addHours(8);
		}
	}),
	Now(6, new IPreparedDateTime() {
		@Override
		public String TextTemplate() {
			return "اکنون";
		}

		@Override
		public PersianDateTime Value() {
			return new PersianDateTime();
		}
	}),
	TodayStart(7, new IPreparedDateTime() {
		@Override
		public String TextTemplate() {
			return "امروز اول وقت: %s";
		}

		@Override
		public PersianDateTime Value() {
			return FarayanUtility.Today().addHours(8);
		}
	}),
	YesterdayStart(8, new IPreparedDateTime() {
		@Override
		public String TextTemplate() {
			return "دیروز اول وقت: %s";
		}

		@Override
		public PersianDateTime Value() {
			return FarayanUtility.Yesterday().addHours(8);
		}
	}),
	CurrentWeekStart(9, new IPreparedDateTime() {
		@Override
		public String TextTemplate() {
			return "آغاز هفته کنونی: %s";
		}

		@Override
		public PersianDateTime Value() {
			return FarayanUtility.WeekStart();
		}
	}),
	CurrentMonthStart(10, new IPreparedDateTime() {
		@Override
		public String TextTemplate() {
			return "آغاز ماه کنونی: %s";
		}

		@Override
		public PersianDateTime Value() {
			return FarayanUtility.MonthStart();
		}
	}),
	CurrentSeasonStart(11, new IPreparedDateTime() {
		@Override
		public String TextTemplate() {
			return "آغاز فصل کنونی: %s";
		}

		@Override
		public PersianDateTime Value() {
			return FarayanUtility.SeasonStart();
		}
	}),
	CurrentYearStart(12, new IPreparedDateTime() {
		@Override
		public String TextTemplate() {
			return "آغاز امسال: %s";
		}

		@Override
		public PersianDateTime Value() {
			return FarayanUtility.YearStart();
		}
	}),
	PreviousWeekStart(13, new IPreparedDateTime() {
		@Override
		public String TextTemplate() {
			return "آغاز هفته قبل: %s";
		}

		@Override
		public PersianDateTime Value() {
			return FarayanUtility.WeekStart().addDays(-7);
		}
	}),
	PreviousMonthStart(14, new IPreparedDateTime() {
		@Override
		public String TextTemplate() {
			return "آغاز ماه قبل: %s";
		}

		@Override
		public PersianDateTime Value() {
			return FarayanUtility.MonthStart().addPersianMonths(-1);
		}
	}),
	PreviousSeasonStart(15, new IPreparedDateTime() {
		@Override
		public String TextTemplate() {
			return "آغاز فصل قبل: %s";
		}

		@Override
		public PersianDateTime Value() {
			return FarayanUtility.SeasonStart().addPersianMonths(-3);
		}
	}),
	PreviousYearStart(16, new IPreparedDateTime() {
		@Override
		public String TextTemplate() {
			return "آغاز پارسال: %s";
		}

		@Override
		public PersianDateTime Value() {
			return FarayanUtility.YearStart().addPersianYear(-1);
		}
	}),;
	public final int Position;
	public final IPreparedDateTime Provider;

	PreparedDates(int position, IPreparedDateTime provider) {
		Position = position;
		Provider = provider;
	}

	public static PreparedDates ByPosition(int position) {
		for (PreparedDates pd : values()) {
			if (position == pd.Position)
				return pd;
		}
		return null;
	}

	public String Displayable() {
		return String.format(Provider.TextTemplate(), Provider.Value());
	}
}
