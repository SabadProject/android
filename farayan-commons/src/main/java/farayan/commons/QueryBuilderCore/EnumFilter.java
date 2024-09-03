package farayan.commons.QueryBuilderCore;

import java.util.ArrayList;
import java.util.List;

import farayan.commons.FarayanUtility;

public class EnumFilter<T> implements IFilter {
	public EnumFilter() {
	}

	public EnumFilter(T exact) {
		this.Exact = exact;
	}

	public EnumFilter(T... any) {
		if (any == null)
			return;
		for (T item : any) {
			if (item == null)
				continue;
			this.Any.add(item);
		}
		if (this.Any.size() == 1) {
			this.Exact = this.Any.get(0);
			this.Any.clear();
		}
	}

	public EnumFilter(T exact, PropertyValueConditionModes mode, RelationalOperators op) {
		this.Exact = exact;
		this.Mode = mode;
		this.Operator = op;
	}

	public T Exact;

	public final List<T> Any = new ArrayList<T>();

	public final List<T> None = new ArrayList<>();

	public final List<T> All = new ArrayList<>();

	public PropertyValueConditionModes Mode = PropertyValueConditionModes.ProvidedValueOrIgnore;

	public RelationalOperators Operator = RelationalOperators.Equal;

	public MinMaxFilterEqualityTypes EqualityType;

	public EnumFilter<T> AnyOf(T... any) {
		this.Any.clear();
		for (int i = 0; i < any.length; i++)
			this.Any.add(any[i]);
		return this;
	}

	public EnumFilter<T> NoneOf(T... any) {
		this.None.clear();
		for (int i = 0; i < any.length; i++)
			this.None.add(any[i]);
		return this;
	}

	public EnumFilter<T> AllOf(T... any) {
		this.All.clear();
		for (int i = 0; i < any.length; i++)
			this.All.add(any[i]);
		return this;
	}

	public void Condition(String propertyName, MixedCondition mixed) {
		if (this.Exact != null)
			mixed.Conditions.add(new PropertyCondition(propertyName, this.Operator, this.Exact));

		if (this.Exact == null && this.Mode == PropertyValueConditionModes.ProvidedValueOrHavingValue)
			mixed.Conditions.add(new PropertyCondition(propertyName, RelationalOperators.IsNotNull, null));

		if (this.Exact == null && this.Mode == PropertyValueConditionModes.ProvidedValueOrNull)
			mixed.Conditions.add(new PropertyCondition(propertyName, RelationalOperators.IsNull, null));

		if (FarayanUtility.IsUsable(this.Any))
			mixed.Conditions.add(new PropertyCondition(propertyName, RelationalOperators.In, this.Any));
	}

	public boolean IsUsable() {
		if (Exact != null)
			return true;
		if (FarayanUtility.IsUsable(Any))
			return true;

		return false;
	}

	public static boolean IsUsable(EnumFilter<?> filter) {
		return filter != null && filter.IsUsable();
	}
}
