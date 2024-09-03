package farayan.commons.QueryBuilderCore;

import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import org.apache.commons.lang3.ArrayUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import farayan.commons.FarayanUtility;

public abstract class BaseParams<TEntity extends IEntity> /*implements IFilter, ISubSelectFilter*/
{
	public String FilterTitle;
	public LogicalOperators LogicalOperator;
	public final List<SortConfig> Sorts = new ArrayList<>();
	public ComparableFilter<Integer> ID;
	public ComparableFilter<UUID> AlwaysID;

	/**
	 * به صورت پیش‌فرض اگر فیلتر خالی باشد، هیچ نتیجه‌ای برنمی‌گرداند
	 * اما در صورتی که این متغیر «مثبت» باشد، همه نتایج برگردانده می‌شود
	 */
	protected final EmptyParamReturns emptyParamReturns;

	private List<PropertyFilter> m_filters;

	/**
	 * یک نسخه جدید از ابزار جستجو را ایجاد می‌کند
	 */
	protected BaseParams() {
		this.LogicalOperator = LogicalOperators.And;
		emptyParamReturns = EmptyParamReturns.Nothing;
	}

	/**
	 * یک نسخه جدید از ابزار جستجو را ایجاد می‌کند
	 *
	 * @param operator نوع ارتباط بین شرط‌‌های مختلف را مشخص می‌کند
	 */
	public BaseParams(LogicalOperators operator) {
		this.LogicalOperator = operator;
		emptyParamReturns = EmptyParamReturns.Nothing;
	}

	/**
	 * یک نسخه جدید از ابزار جستجو را ایجاد می‌کند
	 *
	 * @param operator          نوع ارتباط بین شرط‌‌های مختلف را مشخص می‌کند
	 * @param emptyParamReturns مشخص می‌کند در صورت خالی بودن فیلتر، چه‌کار باید بکند
	 */
	public BaseParams(LogicalOperators operator, EmptyParamReturns emptyParamReturns) {
		this.LogicalOperator = operator;
		this.emptyParamReturns = emptyParamReturns;
	}

	/**
	 * یک نسخه جدید از ابزار جستجو را ایجاد می‌کند
	 *
	 * @param operator          نوع ارتباط بین شرط‌‌های مختلف را مشخص می‌کند
	 * @param emptyParamReturns مشخص می‌کند در صورت خالی بودن فیلتر، چه‌کار باید بکند
	 * @param sortColumn        اگر مشخص شود، موارد برگشتی براساس اطلاعات مشخص شده در این ستون، مرتب خواهند شد. اگر مشخص نشود، نول یا خالی باشد، موارد برگشتی بدون ترتیب خواهند شود. جهت مرتب سازی از کمتر به بیشتر است
	 */
	public BaseParams(LogicalOperators operator, EmptyParamReturns emptyParamReturns, String sortColumn) {
		this.LogicalOperator = operator;
		this.emptyParamReturns = emptyParamReturns;
		this.Sorts.add(new SortConfig(sortColumn, SortDirections.Ascending));
	}

	/**
	 * یک نسخه جدید از ابزار جستجو را ایجاد می‌کند
	 *
	 * @param operator          نوع ارتباط بین شرط‌‌های مختلف را مشخص می‌کند
	 * @param emptyParamReturns اگر مثبت باشد و شرط هم خالی باشد، هیچ نتیجه‌ای را برنمی‌گرداند (حالت پیش‌فرض). اما اگر منفی باشد در صورتی که شرط خالی باشد، همه موارد را برمی‌گرداند
	 * @param sortColumn        اگر مشخص شود، موارد برگشتی براساس اطلاعات مشخص شده در این ستون، مرتب خواهند شد. اگر مشخص نشود، نول یا خالی باشد، موارد برگشتی بدون ترتیب خواهند شود.
	 * @param sortDirection     جهت مرتب‌سازی را مشخص می‌کند
	 */
	public BaseParams(LogicalOperators operator, EmptyParamReturns emptyParamReturns, String sortColumn, SortDirections sortDirection) {
		this.LogicalOperator = operator;
		this.emptyParamReturns = emptyParamReturns;
		this.Sorts.add(new SortConfig(sortColumn, sortDirection));
	}

	//@Deprecated
	public BaseParams<TEntity> Order(boolean clearSorts, String column, SortDirections sortDirection) {
		if (clearSorts)
			this.Sorts.clear();
		this.Sorts.add(new SortConfig(column, sortDirection));
		return this;
	}

	protected RuntimeExceptionDao<TEntity, Integer> DAO() {
		throw new RuntimeException("This method is deprecated. Use IRepo instead");
	}

	public MixedCondition CreateCondition(MixedCondition mixed, String prefix) {
		if (!IsUsable())
			return mixed;
		if (mixed == null)
			mixed = new MixedCondition(LogicalOperator);
		for (PropertyFilter propertyFilter : FiltersCached()) {
			if (propertyFilter == null)
				continue;

			IFilter filter = propertyFilter.FilterProvider == null ? propertyFilter.Filter : propertyFilter.FilterProvider.provide();
			if (filter == null)
				continue;
			if (!filter.IsUsable())
				continue;
			if (filter instanceof ISubSelectFilter)
				((ISubSelectFilter) filter).Condition(propertyFilter.Name, mixed, propertyFilter.SubSelectPropertyName);
			else
				filter.Condition(propertyFilter.Name, mixed);
		}
		return mixed;
	}

	public PropertyFilter[] Filters() {
		return new PropertyFilter[]{
				new PropertyFilter(BaseSchema.ID, () -> this.ID),
				new PropertyFilter(BaseSchema.AlwaysID, () -> this.AlwaysID)
		};
	}

	public boolean IsUsable() {
		if (FarayanUtility.IsNullOrEmpty(FiltersCached()))
			return false;
		for (PropertyFilter propertyFilter : FiltersCached()) {
			if (propertyFilter == null)
				continue;
			IFilter filter = propertyFilter.FilterProvider == null ? propertyFilter.Filter : propertyFilter.FilterProvider.provide();
			if (filter == null)
				continue;
			if (filter.IsUsable())
				return true;
		}
		return false;
	}

	public boolean IsEmpty() {
		return !IsUsable();
	}

	/**
	 * اگر فیلتری ایجاد یا حذف گردد، یا مقادیر آن تغییر کند، باید این تابع فراخوانی شود تا کش فیلترها پاک شود
	 */
	public void FiltersChanged() {
		m_filters = null;
	}

	private List<PropertyFilter> FiltersCached() {
		if (m_filters == null) {
			m_filters = new ArrayList<>();
			PropertyFilter[] filters = Filters();
			if (filters != null) {
				m_filters.addAll(Arrays.asList(filters));
			}
		}
		return m_filters;
	}

	@Deprecated
	public List<TEntity> List() {
		if (emptyParamReturns == EmptyParamReturns.Nothing && !IsUsable())
			return new ArrayList<>();

		try {
			QueryBuilder<TEntity, Integer> qb = DAO().queryBuilder();
			if (IsUsable())
				qb.setWhere(CreateCondition(null, "").Where(qb.where()));
			/*if (Sort.IsUsable())
				qb.orderBy(Sort.getColumn(), Sort.getDirection().a2z);*/
			if (FarayanUtility.IsUsable(Sorts)) {
				for (SortConfig sortConfig : Sorts) {
					if (sortConfig.IsUsable())
						qb.orderBy(sortConfig.getColumn(), sortConfig.getDirection().a2z);
				}
			}

			List<TEntity> list = qb.query();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Deprecated
	public Collection<? extends TEntity> Paged(long start, long count) {
		if (emptyParamReturns == EmptyParamReturns.Nothing && !IsUsable())
			return new ArrayList<>();

		try {
			QueryBuilder<TEntity, Integer> qb = DAO().queryBuilder();
			if (IsUsable())
				qb.setWhere(CreateCondition(null, "").Where(qb.where()));
			/*if (Sort.IsUsable())
				qb.orderBy(Sort.getColumn(), Sort.getDirection().a2z);*/
			if (FarayanUtility.IsUsable(Sorts)) {
				for (SortConfig sortConfig : Sorts) {
					if (sortConfig.IsUsable())
						qb.orderBy(sortConfig.getColumn(), sortConfig.getDirection().a2z);
				}
			}
			qb = qb.offset(start).limit(count);
			List<TEntity> list = qb.query();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Deprecated
	public TEntity Load() {
		return First();
	}


	@Deprecated
	public TEntity First() {
		if (emptyParamReturns == EmptyParamReturns.Nothing && !IsUsable())
			return null;

		try {
			QueryBuilder<TEntity, Integer> qb = DAO().queryBuilder();
			if (IsUsable())
				qb.setWhere(CreateCondition(new MixedCondition(LogicalOperator), "").Where(qb.where()));
			/*if (Sort.IsUsable())
				qb.orderBy(Sort.getColumn(), Sort.getDirection().a2z);*/
			if (FarayanUtility.IsUsable(Sorts)) {
				for (SortConfig sortConfig : Sorts) {
					if (sortConfig.IsUsable())
						qb.orderBy(sortConfig.getColumn(), sortConfig.getDirection().a2z);
				}
			}
			TEntity item = qb.queryForFirst();
			return item;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public QueryBuilder<TEntity, Integer> SubSelectQueryBuilder() {
		return SubSelectQueryBuilder(BaseSchema.ID);
	}

	public QueryBuilder<TEntity, Integer> SubSelectQueryBuilder(String column) {
		QueryBuilder<TEntity, Integer> qb = DAO().queryBuilder();
		if (IsUsable())
			qb.setWhere(CreateCondition(new MixedCondition(LogicalOperator), "").Where(qb.where()));
		qb.selectColumns(column);
		return qb;
	}


	@Deprecated
	public GenericRawResults<String[]> SelectColumns(String... columns) {
		if (emptyParamReturns == EmptyParamReturns.Nothing && !IsUsable())
			return null;

		QueryBuilder<TEntity, Integer> qb = DAO().queryBuilder();
		if (IsUsable())
			qb.setWhere(CreateCondition(new MixedCondition(LogicalOperator), "").Where(qb.where()));
		qb.selectColumns(columns);
		try {
			return qb.queryRaw();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}


	@Deprecated
	public String[] FirstColumns(String... columns) {
		if (emptyParamReturns == EmptyParamReturns.Nothing && !IsUsable())
			return null;

		QueryBuilder<TEntity, Integer> qb = DAO().queryBuilder();
		if (IsUsable())
			qb.setWhere(CreateCondition(new MixedCondition(LogicalOperator), "").Where(qb.where()));
		qb.selectRaw(columns);
		try {
			return qb.queryRawFirst();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}


	@Deprecated
	public String FirstColumn(String column) {
		if (emptyParamReturns == EmptyParamReturns.Nothing && !IsUsable())
			return null;

		QueryBuilder<TEntity, Integer> qb = DAO().queryBuilder();
		if (IsUsable())
			qb.setWhere(CreateCondition(new MixedCondition(LogicalOperator), "").Where(qb.where()));
		qb.selectRaw(column);
		try {
			String[] values = qb.queryRawFirst();
			return values[0];
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static boolean IsUsable(BaseParams<?> baseParams) {
		return baseParams != null && baseParams.IsUsable();
	}


	@Deprecated
	public void Delete() {
		if (emptyParamReturns == EmptyParamReturns.Nothing && !IsUsable())
			return;
		try {
			DeleteBuilder<TEntity, Integer> deleteBuilder = DAO().deleteBuilder();
			if (IsUsable())
				deleteBuilder.setWhere(CreateCondition(new MixedCondition(LogicalOperator), "").Where(deleteBuilder.where()));
			DAO().delete(deleteBuilder.prepare());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}


	@Deprecated
	public long Count() {
		if (emptyParamReturns == EmptyParamReturns.Nothing && !IsUsable())
			return 0;
		try {
			QueryBuilder<TEntity, Integer> qb = DAO().queryBuilder();
			if (IsUsable())
				qb.setWhere(CreateCondition(new MixedCondition(LogicalOperator), "").Where(qb.where()));
			long count = qb.countOf();
			return count;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public double Sum(String column) {
		if (emptyParamReturns == EmptyParamReturns.Nothing && !IsUsable())
			return 0;
		try {
			QueryBuilder<TEntity, Integer> qb = DAO().queryBuilder();
			if (IsUsable())
				qb.setWhere(CreateCondition(new MixedCondition(LogicalOperator), "").Where(qb.where()));

			qb.selectRaw(String.format("SUM(%s)", column));
			Double sum = FarayanUtility.TryParseDouble(qb.queryRaw().getFirstResult()[0]);
			return sum == null ? 0 : sum;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * aggregate
	 *
	 * @param groupByColumn
	 * @param selectGroupByColumn
	 * @param aggregateFunctions
	 * @return
	 */
	public GenericRawResults<String[]> Aggregate(String groupByColumn, boolean selectGroupByColumn, SqlAggAndHave... aggregateFunctions) {
		if (emptyParamReturns == EmptyParamReturns.Nothing && !IsUsable())
			return null;
		try {
			QueryBuilder<TEntity, Integer> qb = DAO().queryBuilder();
			if (IsUsable())
				qb.setWhere(CreateCondition(new MixedCondition(LogicalOperator), "").Where(qb.where()));
			qb.groupBy(groupByColumn);

			String[] columns = new String[]{};
			if (selectGroupByColumn) {
				columns = ArrayUtils.add(columns, groupByColumn);
			}
			String[] preparedColumns = Arrays
					.stream(aggregateFunctions)
					.filter(x -> FarayanUtility.IsUsable(x.AggregateExpression))
					.map(x -> x.AggregateExpression)
					.collect(Collectors.toList())
					.toArray(new String[aggregateFunctions.length]);

			columns = ArrayUtils.addAll(columns, preparedColumns);
			qb.having(Arrays
					.stream(aggregateFunctions)
					.filter(x -> FarayanUtility.IsUsable(x.HavingCondition))
					.map(x -> x.HavingCondition)
					.collect(Collectors.joining(" and "))
			);

			qb.selectRaw(columns);
			GenericRawResults<String[]> rawResults = qb.queryRaw();
			return rawResults;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
