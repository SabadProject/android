package farayan.commons.QueryBuilderCore;

/**
 * Holds a group by ad its optional having condition
 * for example imagine below query
 * select FormPosition, count(FormPosition) from Melaks group by FormPosition Having count(FormPosition)>2
 * if we want to create advanced query, 1 {@link SqlAggAndHave} instance is required to describe `count(FormPosition)` and its (Optional)
 * Having section
 */
public class SqlAggAndHave
{
	/**
	 * full value of expression like `SUM(col)`
	 */
	public final String AggregateExpression;

	/**
	 * Optional condition for group by column
	 * like `COUNT(ID)>2`
	 */
	public final String HavingCondition;

	/**
	 * @param expression      Just name of aggregate function, like `count` or `sum`
	 * @param havingCondition Optional condition for group by column like `>2`
	 */
	public SqlAggAndHave(String expression, String havingCondition) {
		AggregateExpression = expression;
		HavingCondition = havingCondition;
	}


	public SqlAggAndHave(String function, String column, String havingCondition) {
		AggregateExpression = String.format("%s(%s)", function, column);
		HavingCondition = havingCondition;
	}

	public SqlAggAndHave(SqlAggMethods function, String column, String havingCondition) {
		AggregateExpression = String.format("%s(%s)", function, column);
		HavingCondition = havingCondition;
	}
}
