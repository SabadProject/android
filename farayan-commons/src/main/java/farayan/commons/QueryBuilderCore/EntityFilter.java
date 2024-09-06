package farayan.commons.QueryBuilderCore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import farayan.commons.FarayanUtility;

public class EntityFilter<EntityT extends IEntity> implements IFilter {
    public EntityFilter(EntityT... vals) {
        if (vals != null && vals.length > 0) {
            if (vals.length == 1)
                this.Value = vals[0];
            if (vals.length > 1) {
                for (EntityT v : vals)
                    this.Any.add(v);
            }
        }
    }

    public EntityFilter(List<? extends EntityT> vals) {
        if (FarayanUtility.IsUsable(vals)) {
            if (vals.size() == 1)
                this.Value = vals.get(0);
            if (vals.size() > 1) {
                for (EntityT v : vals)
                    this.Any.add(v);
            }
        }
    }

    public EntityFilter(Collection<? extends EntityT> vals) {
        if (FarayanUtility.IsUsable(vals)) {
            Iterator<? extends EntityT> iterator = vals.iterator();
            do {
                if (vals.size() == 1) {
                    this.Value = iterator.next();
                    break;
                } else {
                    this.Any.add(iterator.next());
                }
            } while (iterator.hasNext());
        }
    }

    public EntityFilter(EntityT value, RelationalOperators operator, PropertyValueConditionModes mode) {
        this.Value = value;
        this.Operator = operator;
        this.Mode = mode;
    }

    public EntityFilter(PropertyValueConditionModes mode) {
        this.Mode = mode;
    }

    public EntityT Value;

    public final List<EntityT> Any = new ArrayList<EntityT>();

    public final List<EntityT> None = new ArrayList<EntityT>();

    public RelationalOperators Operator = RelationalOperators.Equal;

    public PropertyValueConditionModes Mode = PropertyValueConditionModes.ProvidedValueOrIgnore;

    public EntityFilter<EntityT> Not(List<EntityT> items) {
        for (EntityT v : items)
            this.None.add(v);
        return this;
    }

    public boolean IsUsable() {
        if (Value != null)
            return true;
        if (FarayanUtility.IsUsable(Any))
            return true;
        if (FarayanUtility.IsUsable(None))
            return true;
        if (Mode != PropertyValueConditionModes.ProvidedValueOrIgnore)
            return true;
        return false;
    }

    public void Condition(String property, MixedCondition mixed) {
        if (mixed == null)
            mixed = new MixedCondition(LogicalOperators.Or);
        switch (this.Mode) {
            case ProvidedValueOrIgnore:
                if (this.Value != null)
                    mixed.Conditions.add(new PropertyCondition(property, Operator, Value));
                break;
            case ProvidedValueOrNull:
                if (Value == null)
                    mixed.Conditions.add(new PropertyCondition(property, RelationalOperators.IsNull, Value));
                else
                    mixed.Conditions.add(new PropertyCondition(property, Operator, Value));
                break;
            case ProvidedValueOrHavingValue:
                if (Value == null)
                    mixed.Conditions.add(new PropertyCondition(property, RelationalOperators.IsNotNull, Value));
                else
                    mixed.Conditions.add(new PropertyCondition(property, Operator, Value));
                break;
            default:
                break;
        }
        if (FarayanUtility.IsUsable(Any)) {
            List<Integer> anyIDs = new ArrayList<Integer>(Any.size());
            for (int index = 0; index < Any.size(); index++) {
                anyIDs.add(Any.get(index).getID());
            }

            List<Integer> uniques = new ArrayList<>();
            for (Integer id : anyIDs) {
                if (uniques.contains(id))
                    continue;
                uniques.add(id);
            }
            if (uniques.size() == 1) {
                mixed.Conditions.add(new PropertyCondition(property, RelationalOperators.Equal, uniques.get(0)));
            } else {
                mixed.Conditions.add(new PropertyCondition(property, RelationalOperators.In, uniques));
            }
        }
        if (FarayanUtility.IsUsable(None)) {
            int[] anyIDs = new int[Any.size()];
            for (int index = 0; index < Any.size(); index++) {
                anyIDs[index] = Any.get(index).getID();
            }
            mixed.Conditions.add(new PropertyCondition(property, RelationalOperators.NotIn, anyIDs));
        }
    }

    public static boolean IsUsable(EntityFilter<?> filter) {
        return filter != null && filter.IsUsable();
    }
}
