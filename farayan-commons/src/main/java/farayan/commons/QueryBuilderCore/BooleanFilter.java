package farayan.commons.QueryBuilderCore;

/**
 * Created by Homayoun on 28/05/2017.
 */

public class BooleanFilter implements IFilter {
    public BooleanFilter() {
    }

    public BooleanFilter(Boolean exact) {
        this.Exact = exact;
        this.Mode = PropertyValueConditionModes.ProvidedValueOrIgnore;
        this.Operator = RelationalOperators.Equal;
    }

    public BooleanFilter(Boolean exact, PropertyValueConditionModes mode, RelationalOperators op) {
        this.Exact = exact;
        this.Mode = mode;
        this.Operator = op;
    }

    public Boolean Exact;

    public PropertyValueConditionModes Mode = PropertyValueConditionModes.ProvidedValueOrIgnore;

    public RelationalOperators Operator = RelationalOperators.Equal;

    public void Condition(String propertyName, MixedCondition mixed) {
        if (this.Exact != null)
            mixed.Conditions.add(new PropertyCondition(propertyName, this.Operator, this.Exact));

        if (this.Exact == null && this.Mode == PropertyValueConditionModes.ProvidedValueOrHavingValue)
            mixed.Conditions.add(new PropertyCondition(propertyName, RelationalOperators.IsNotNull, null));

        if (this.Exact == null && this.Mode == PropertyValueConditionModes.ProvidedValueOrNull)
            mixed.Conditions.add(new PropertyCondition(propertyName, RelationalOperators.IsNull, null));
    }

    public boolean IsUsable() {
        if (Exact != null)
            return true;

        return false;
    }

    public static boolean IsUsable(BooleanFilter filter) {
        return filter != null && filter.IsUsable();
    }
}
