package farayan.commons.QueryBuilderCore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import farayan.commons.FarayanUtility;


public class ComparableFilter<T extends Comparable<T>> implements IFilter
{
	public ComparableFilter() {
	}

	public ComparableFilter(T exact) {
		this.Exact = exact;
	}

	public ComparableFilter(PropertyValueConditionModes mode) {
		this.Mode = mode;
	}

	public ComparableFilter(RelationalOperators op) {
		this.Operator = op;
	}

	public ComparableFilter(T exact, PropertyValueConditionModes mode, RelationalOperators op) {
		this.Exact = exact;
		this.Mode = mode;
		this.Operator = op;
	}

	public ComparableFilter(T exact, RelationalOperators op) {
		this.Exact = exact;
		this.Operator = op;
	}

	public ComparableFilter(T min, T max) {
		this.Min = min;
		this.Max = max;
	}

	public ComparableFilter(T min, T max, T exact) {
		this.Min = min;
		this.Max = max;
		this.Exact = exact;
	}

	public T Min;

	public T Max;

	public T Exact;

	public final List<T> Any = new ArrayList<T>();

	public final List<T> None = new ArrayList<>();

	public final List<T> All = new ArrayList<>();

	public PropertyValueConditionModes Mode = PropertyValueConditionModes.ProvidedValueOrIgnore;

	public RelationalOperators Operator = RelationalOperators.Equal;

	public MinMaxFilterEqualityTypes EqualityType = MinMaxFilterEqualityTypes.Both;

	public ComparableFilter<T> AnyOf(T... any) {
		this.Any.clear();
		this.Any.addAll(Arrays.asList(any));
		return this;
	}

	public ComparableFilter<T> NoneOf(T... any) {
		this.None.clear();
		this.None.addAll(Arrays.asList(any));
		return this;
	}

	public ComparableFilter<T> AllOf(T... any) {
		this.All.clear();
		this.All.addAll(Arrays.asList(any));
		return this;
	}

	public void Condition(String propertyName, MixedCondition mixed) {
		if (this.Min != null && this.Max != null && ((Comparable) this.Min).compareTo(this.Max) > 0) {
			MixedCondition or = new MixedCondition(LogicalOperators.Or);

			RelationalOperators op1 = RelationalOperators.GreaterThan;
			if (this.EqualityType == MinMaxFilterEqualityTypes.Both || this.EqualityType == MinMaxFilterEqualityTypes.Min)
				op1 = RelationalOperators.GreaterThanOrEqual;
			or.Conditions.add(new PropertyCondition(propertyName, op1, this.Min));

			RelationalOperators op2 = RelationalOperators.LessThan;
			if (this.EqualityType == MinMaxFilterEqualityTypes.Both || this.EqualityType == MinMaxFilterEqualityTypes.Max)
				op2 = RelationalOperators.LessThanOrEqual;
			or.Conditions.add(new PropertyCondition(propertyName, op2, this.Max));

			mixed.Conditions.add(or);
		} else {
			if (this.Min != null) {
				RelationalOperators op = RelationalOperators.GreaterThan;
				if (this.EqualityType == MinMaxFilterEqualityTypes.Both || this.EqualityType == MinMaxFilterEqualityTypes.Min)
					op = RelationalOperators.GreaterThanOrEqual;
				mixed.Conditions.add(new PropertyCondition(propertyName, op, this.Min));
			}
			if (this.Max != null) {
				RelationalOperators op = RelationalOperators.LessThan;
				if (this.EqualityType == MinMaxFilterEqualityTypes.Both || this.EqualityType == MinMaxFilterEqualityTypes.Max)
					op = RelationalOperators.LessThanOrEqual;
				mixed.Conditions.add(new PropertyCondition(propertyName, op, this.Max));
			}
		}

		if (this.Exact != null)
			mixed.Conditions.add(new PropertyCondition(propertyName, this.Operator, this.Exact));

		if (this.Exact == null && !this.Operator.NeedsValue)
			mixed.Conditions.add(new PropertyCondition(propertyName, RelationalOperators.IsNull, null));

		if (this.Exact == null && this.Mode == PropertyValueConditionModes.ProvidedValueOrHavingValue)
			mixed.Conditions.add(new PropertyCondition(propertyName, RelationalOperators.IsNotNull, null));

		if (this.Exact == null && this.Mode == PropertyValueConditionModes.ProvidedValueOrNull)
			mixed.Conditions.add(new PropertyCondition(propertyName, RelationalOperators.IsNull, null));

		if (FarayanUtility.IsUsable(this.Any)) {
			List<T> uniques = new ArrayList<>();
			for (T item : Any) {
				if (uniques.contains(item))
					continue;
				uniques.add(item);
			}
			if (uniques.size() == 1) {
				mixed.Conditions.add(new PropertyCondition(propertyName, RelationalOperators.Equal, uniques.get(0)));
			} else {
				mixed.Conditions.add(new PropertyCondition(propertyName, RelationalOperators.In, uniques));
			}
		}
	}

	public boolean IsUsable() {
		if (Min != null)
			return true;
		if (Max != null)
			return true;
		if (Exact != null)
			return true;
		if (!Operator.NeedsValue)
			return true;
		if (Mode != PropertyValueConditionModes.ProvidedValueOrIgnore)
			return true;
		if (FarayanUtility.IsUsable(Any))
			return true;

		return false;
	}

	public static boolean IsUsable(ComparableFilter<?> filter) {
		return filter != null && filter.IsUsable();
	}
}
