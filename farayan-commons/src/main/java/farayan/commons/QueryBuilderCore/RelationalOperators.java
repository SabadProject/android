package farayan.commons.QueryBuilderCore;

public enum RelationalOperators {
	Equal("=", true),
	NotEqual("<>", true),
	GreaterThan(">", true),
	GreaterThanOrEqual(">=", true),
	LessThan("<", true),
	LessThanOrEqual("<=", true),
	Like(" like ", true),
	IsNull(" is null", false),
	IsNotNull("is not null", false),
	In("in", true),
	NotIn("not in", true);

	public final String Query;
	public final boolean NeedsValue;

	private RelationalOperators(String q, boolean needsValue) {
		Query = q;
		NeedsValue = needsValue;
	}
}
