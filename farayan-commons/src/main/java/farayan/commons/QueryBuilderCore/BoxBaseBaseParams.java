package farayan.commons.QueryBuilderCore;

import org.apache.commons.lang3.ArrayUtils;

import farayan.commons.UI.Core.IBoxEntity;

public abstract class BoxBaseBaseParams<T extends IEntity & IBoxEntity> extends BaseParams<T>
{
	public ComparableFilter<Integer> OrderIndex;
	public TextFilter Title;

	@Override
	public PropertyFilter[] Filters() {
		return ArrayUtils.addAll(
				super.Filters(),
				new PropertyFilter(BoxBaseSchema.OrderIndex, () -> OrderIndex),
				new PropertyFilter(BoxBaseSchema.Title, () ->Title)
		);
	}


	/**
	 * یک نسخه جدید از ابزار جستجو را ایجاد می‌کند
	 */
	public BoxBaseBaseParams() {
	}

	/**
	 * یک نسخه جدید از ابزار جستجو را ایجاد می‌کند
	 *
	 * @param operator نوع ارتباط بین شرط‌‌های مختلف را مشخص می‌کند
	 */
	public BoxBaseBaseParams(LogicalOperators operator) {
		super(operator);
	}

	/**
	 * یک نسخه جدید از ابزار جستجو را ایجاد می‌کند
	 *
	 * @param operator          نوع ارتباط بین شرط‌‌های مختلف را مشخص می‌کند
	 * @param emptyParamReturns مشخص می‌کند در صورت خالی بودن فیلتر، چه‌کار باید بکند
	 */
	public BoxBaseBaseParams(LogicalOperators operator, EmptyParamReturns emptyParamReturns) {
		super(operator, emptyParamReturns);
	}

	/**
	 * یک نسخه جدید از ابزار جستجو را ایجاد می‌کند
	 *
	 * @param operator          نوع ارتباط بین شرط‌‌های مختلف را مشخص می‌کند
	 * @param emptyParamReturns مشخص می‌کند در صورت خالی بودن فیلتر، چه‌کار باید بکند
	 * @param sortColumn        اگر مشخص شود، موارد برگشتی براساس اطلاعات مشخص شده در این ستون، مرتب خواهند شد. اگر مشخص نشود، نول یا خالی باشد، موارد برگشتی بدون ترتیب خواهند شود. جهت مرتب سازی از کمتر به بیشتر است
	 */
	public BoxBaseBaseParams(LogicalOperators operator, EmptyParamReturns emptyParamReturns, String sortColumn) {
		super(operator, emptyParamReturns, sortColumn);
	}

	/**
	 * یک نسخه جدید از ابزار جستجو را ایجاد می‌کند
	 *
	 * @param operator          نوع ارتباط بین شرط‌‌های مختلف را مشخص می‌کند
	 * @param emptyParamReturns اگر مثبت باشد و شرط هم خالی باشد، هیچ نتیجه‌ای را برنمی‌گرداند (حالت پیش‌فرض). اما اگر منفی باشد در صورتی که شرط خالی باشد، همه موارد را برمی‌گرداند
	 * @param sortColumn        اگر مشخص شود، موارد برگشتی براساس اطلاعات مشخص شده در این ستون، مرتب خواهند شد. اگر مشخص نشود، نول یا خالی باشد، موارد برگشتی بدون ترتیب خواهند شود.
	 * @param sortDirection     جهت مرتب‌سازی را مشخص می‌کند
	 */
	public BoxBaseBaseParams(LogicalOperators operator, EmptyParamReturns emptyParamReturns, String sortColumn, SortDirections sortDirection) {
		super(operator, emptyParamReturns, sortColumn, sortDirection);
	}
}
