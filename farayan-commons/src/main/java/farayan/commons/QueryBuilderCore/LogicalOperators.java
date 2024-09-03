package farayan.commons.QueryBuilderCore;

/**
 * رابطه بین شرط‌ها
 */
public enum LogicalOperators {
	And(" and "),
	Or(" or ");

	public final String Operator;

	LogicalOperators(String o) {
		Operator = o;
	}
}
