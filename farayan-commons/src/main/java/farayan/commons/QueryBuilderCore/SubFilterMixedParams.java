package farayan.commons.QueryBuilderCore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import farayan.commons.FarayanUtility;

/**
 * اجازه می‌دهد در
 * @param <EntityType>
 * @param <ParamType>
 */
public abstract class SubFilterMixedParams<EntityType extends IEntity, ParamType extends BaseParams<EntityType>>
        extends MixedBaseParams<EntityType, ParamType>
        implements ISubSelectFilter
{
    /**
     * یک نسخه جدید از ابزار جستجو را ایجاد می‌کند
     */
    public SubFilterMixedParams() {
        this(LogicalOperators.And, EmptyParamReturns.Nothing, "", SortDirections.Ascending);
    }

    /**
     * یک نسخه جدید از ابزار جستجو را ایجاد می‌کند
     */
    public SubFilterMixedParams(ParamType... filters) {
        this(LogicalOperators.And, EmptyParamReturns.Nothing, "", null, filters);
    }

    /**
     * یک نسخه جدید از ابزار جستجو را ایجاد می‌کند
     *
     * @param operator نوع ارتباط بین شرط‌‌های مختلف را مشخص می‌کند
     */
    public SubFilterMixedParams(LogicalOperators operator, ParamType... filters) {
        this(operator, EmptyParamReturns.Nothing, null, null, filters);
    }

    /**
     * یک نسخه جدید از ابزار جستجو را ایجاد می‌کند
     *
     * @param operator          نوع ارتباط بین شرط‌‌های مختلف را مشخص می‌کند
     * @param emptyParamReturns مشخص می‌کند در صورت خالی بودن فیلتر، چه‌کار باید بکند
     */
    public SubFilterMixedParams(LogicalOperators operator, EmptyParamReturns emptyParamReturns, ParamType... filters) {
        this(operator, emptyParamReturns, null, null, filters);
    }

    /**
     * یک نسخه جدید از ابزار جستجو را ایجاد می‌کند
     *
     * @param operator          نوع ارتباط بین شرط‌‌های مختلف را مشخص می‌کند
     * @param emptyParamReturns مشخص می‌کند در صورت خالی بودن فیلتر، چه‌کار باید بکند
     * @param sortColumn        اگر مشخص شود، موارد برگشتی براساس اطلاعات مشخص شده در این ستون، مرتب خواهند شد. اگر مشخص نشود، نول یا خالی باشد، موارد برگشتی بدون ترتیب خواهند شود. جهت مرتب سازی از کمتر به بیشتر است
     */
    public SubFilterMixedParams(LogicalOperators operator, EmptyParamReturns emptyParamReturns, String sortColumn, ParamType... filters) {
        this(operator, emptyParamReturns, sortColumn, null, filters);
    }

    /**
     * یک نسخه جدید از ابزار جستجو را ایجاد می‌کند
     *
     * @param operator          نوع ارتباط بین شرط‌‌های مختلف را مشخص می‌کند
     * @param emptyParamReturns اگر مثبت باشد و شرط هم خالی باشد، هیچ نتیجه‌ای را برنمی‌گرداند (حالت پیش‌فرض). اما اگر منفی باشد در صورتی که شرط خالی باشد، همه موارد را برمی‌گرداند
     * @param sortColumn        اگر مشخص شود، موارد برگشتی براساس اطلاعات مشخص شده در این ستون، مرتب خواهند شد. اگر مشخص نشود، نول یا خالی باشد، موارد برگشتی بدون ترتیب خواهند شود.
     * @param sortDirection     جهت مرتب‌سازی را مشخص می‌کند
     */
    public SubFilterMixedParams(LogicalOperators operator, EmptyParamReturns emptyParamReturns, String sortColumn, SortDirections sortDirection, ParamType... filters) {
        super(operator, emptyParamReturns, sortColumn, sortDirection);
        if (filters != null && filters.length > 0) {
            this.Filters.addAll(Arrays.asList(filters));
        }
    }

    public final List<ParamType> Filters = new ArrayList<>();

    @Override
    public MixedCondition CreateCondition(MixedCondition mixed, String prefix) {
        if (mixed == null)
            mixed = new MixedCondition(LogicalOperator);
        for (ParamType tp : Filters) {
            MixedCondition condition = tp.CreateCondition(null, "");
            if (condition != null && FarayanUtility.IsUsable(condition.Conditions))
                mixed.Conditions.add(condition);
        }

        return mixed;
    }

    @Override
    public void Condition(String name, MixedCondition mixed, String subSelectPropertyName) {
        if (IsUsable()) {
            mixed.Conditions.add(new PropertyCondition(name, RelationalOperators.In, SubSelectQueryBuilder(subSelectParentIDColumn())));
        }
    }

    /**
     * اسم ستونی در جدول کودک که به شناسه‌ی جدول والد، ارجاع می‌دهد
     * مثلا اگر جدول زیر را داشته باشیم:
     * Parent
     *  - ID
     *  - Name
     *  و جدول کودک زیر:
     *  Child:
     *  - ID
     *  - ParentID
     *  - ChildName
     *  این تابع باید عبارت
     *  ProductID
     *  را برگرداند
     * @return
     */
    protected abstract String subSelectParentIDColumn();

    @Override
    public boolean IsUsable() {
        if (FarayanUtility.IsNullOrEmpty(Filters))
            return false;
        for (ParamType filter : Filters) {
            if (ParamType.IsUsable(filter))
                return true;
        }
        return false;
    }
}
