package farayan.commons.QueryBuilderCore;

import com.j256.ormlite.field.DatabaseField;

import farayan.commons.FarayanUtility;
import farayan.commons.UI.Core.IBoxEntity;

public abstract class BoxBaseEntity<EntityType extends IEntity> extends BaseEntity<EntityType> implements IBoxEntity {
	@DatabaseField(columnName = BoxBaseSchema.Title)
	public String Title;
	@DatabaseField(columnName = BoxBaseSchema.OrderIndex)
	public int OrderIndex;

	@Override
	public String getTitle() {
		return Title;
	}

	@Override
	public void setTitle(String title) {
		Title = title;
	}

	public int getOrderIndex() {
		return OrderIndex;
	}

	public void setOrderIndex(int orderIndex) {
		OrderIndex = orderIndex;
	}

	@Override
	public boolean isFilterMatched(CharSequence criteria) {
		if (FarayanUtility.IsNullOrEmpty(getTitle()))
			return false;
		if (FarayanUtility.IsNullOrEmpty(criteria))
			return false;
		return FarayanUtility.NormalizePersian(getTitle()).toLowerCase().contains(FarayanUtility.NormalizePersian(criteria.toString().toLowerCase()));
	}

	/**
	 * مشخص می‌کند آیا موجودیت از بانک اطلاعاتی خوانده شده یا نه
	 * باید نول بودن یا پر بودن یک متغیر الزامی را بررسی کند
	 * مثلا اگر موجودیت شما، حتما نام دارد ولی نام، نول هست، باید بازخوانی شود و این تابع باید مقدار مثبت برگرداند
	 * این تابع لازم نیست وجود شناسه را بررسی کند
	 * بلکه تنها در صورتی فراخوانده می‌شود که شناسه مقدار داشته باشد
	 * اگر شناسه، صفر باشد، این تابع فراخوانده نمی‌شود
	 *
	 * @return
	 */
	@Override
	public boolean NeedsRefresh() {
		return Title == null;
	}
}
