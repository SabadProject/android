package farayan.commons.QueryBuilderCore;

public interface IEntity {
    int getID();

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
	boolean NeedsRefresh();
}