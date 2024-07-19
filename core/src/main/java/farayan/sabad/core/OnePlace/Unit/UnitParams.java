package farayan.sabad.core.OnePlace.Unit;

import org.apache.commons.lang3.ArrayUtils;

import farayan.commons.QueryBuilderCore.EnumFilter;
import farayan.commons.QueryBuilderCore.PropertyFilter;
import farayan.commons.QueryBuilderCore.TextFilter;
import farayan.sabad.core.base.SabadParamsBase;
import farayan.sabad.core.commons.UnitVariations;
import farayan.sabad.core.model.unit.UnitEntity;

public class UnitParams extends SabadParamsBase<UnitEntity> {
    public TextFilter QueryableName;
    public TextFilter DisplayableName;
    public TextFilter Category;
    public EnumFilter<UnitVariations> variation;

    @Override
    public PropertyFilter[] Filters() {
        return ArrayUtils.addAll(
                super.Filters(),
                new PropertyFilter(UnitSchema.QueryableName, () -> QueryableName),
                new PropertyFilter(UnitSchema.DisplayableName, () -> DisplayableName),
                new PropertyFilter(UnitSchema.Category, () -> Category),
                new PropertyFilter(UnitSchema.Variation, () -> variation)
        );
    }
}
