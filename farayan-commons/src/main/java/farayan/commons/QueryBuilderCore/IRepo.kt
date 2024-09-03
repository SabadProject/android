package farayan.commons.QueryBuilderCore

import com.j256.ormlite.dao.GenericRawResults
import com.j256.ormlite.dao.RuntimeExceptionDao
import farayan.commons.FarayanUtility
import org.apache.commons.lang3.ArrayUtils
import java.sql.SQLException
import java.util.*
import java.util.stream.Collectors

interface IRepo<TEntity : IEntity?> {
    fun EmptyParamReturning(): EmptyParamReturns {
        return EmptyParamReturns.Everything
    }

    fun ByID(id: Int): TEntity? {
        return DAO().queryForId(id)
    }

    fun ByAlwaysID(alwaysID: UUID): TEntity? {
        return DAO().queryForEq(BaseSchema.AlwaysID, alwaysID).first()
    }

    fun All(params: BaseParams<TEntity>): List<TEntity>? {
        return if (EmptyParamReturning() == EmptyParamReturns.Nothing && !IsUsable(params)) ArrayList() else try {
            val qb = DAO().queryBuilder()
            if (IsUsable(params)) qb.setWhere(params.CreateCondition(null, "").Where(qb.where()))
            if (FarayanUtility.IsUsable(params.Sorts)) {
                for (sortConfig in params.Sorts) {
                    if (sortConfig.IsUsable()) qb.orderBy(sortConfig.column, sortConfig.direction.a2z)
                }
            }
            qb.query()
        } catch (e: SQLException) {
            e.printStackTrace()
            throw RuntimeException(e)
        }
    }

    fun Paged(params: BaseParams<TEntity>, start: Long, count: Long): Collection<TEntity>? {
        return if (EmptyParamReturning() == EmptyParamReturns.Nothing && !IsUsable(params)) ArrayList() else try {
            var qb = DAO().queryBuilder()
            if (IsUsable(params)) qb.setWhere(params.CreateCondition(null, "").Where(qb.where()))
            if (FarayanUtility.IsUsable(params.Sorts)) {
                for (sortConfig in params.Sorts) {
                    if (sortConfig.IsUsable()) qb.orderBy(sortConfig.column, sortConfig.direction.a2z)
                }
            }
            qb = qb.offset(start).limit(count)
            qb.query()
        } catch (e: SQLException) {
            e.printStackTrace()
            throw RuntimeException(e)
        }
    }

    fun First(params: BaseParams<TEntity>): TEntity? {
        return if (EmptyParamReturning() == EmptyParamReturns.Nothing && !IsUsable(params)) null else try {
            val qb = DAO().queryBuilder()
            if (IsUsable(params)) qb.setWhere(params.CreateCondition(MixedCondition(params.LogicalOperator), "").Where(qb.where()))
            if (FarayanUtility.IsUsable(params.Sorts)) {
                for (sortConfig in params.Sorts) {
                    if (sortConfig.IsUsable()) qb.orderBy(sortConfig.column, sortConfig.direction.a2z)
                }
            }
            qb.queryForFirst()
        } catch (e: SQLException) {
            e.printStackTrace()
            throw RuntimeException(e)
        }
    }

    fun AllColumns(params: BaseParams<TEntity>, vararg columns: String?): GenericRawResults<Array<String?>?>? {
        if (EmptyParamReturning() == EmptyParamReturns.Nothing && !IsUsable(params)) return null
        val qb = DAO().queryBuilder()
        if (IsUsable(params)) qb.setWhere(params.CreateCondition(MixedCondition(params.LogicalOperator), "").Where(qb.where()))
        qb.selectColumns(*columns)
        return try {
            qb.queryRaw()
        } catch (e: SQLException) {
            e.printStackTrace()
            throw RuntimeException(e)
        }
    }

    fun FirstColumns(params: BaseParams<TEntity>, vararg columns: String?): Array<String?>? {
        if (EmptyParamReturning() == EmptyParamReturns.Nothing && !IsUsable(params)) return null
        val qb = DAO().queryBuilder()
        if (IsUsable(params)) qb.setWhere(params.CreateCondition(MixedCondition(params.LogicalOperator), "").Where(qb.where()))
        qb.selectRaw(*columns)
        return try {
            qb.queryRawFirst()
        } catch (e: SQLException) {
            e.printStackTrace()
            throw RuntimeException(e)
        }
    }

    fun FirstColumn(params: BaseParams<TEntity>, column: String?): String? {
        if (EmptyParamReturning() == EmptyParamReturns.Nothing && !IsUsable(params)) return null
        val qb = DAO().queryBuilder()
        if (IsUsable(params)) qb.setWhere(params.CreateCondition(MixedCondition(params.LogicalOperator), "").Where(qb.where()))
        qb.selectRaw(column)
        return try {
            val values = qb.queryRawFirst()
            values[0]
        } catch (e: SQLException) {
            e.printStackTrace()
            throw RuntimeException(e)
        }
    }

    fun Delete(params: BaseParams<TEntity>) {
        if (EmptyParamReturning() == EmptyParamReturns.Nothing && !IsUsable(params)) return
        try {
            val deleteBuilder = DAO().deleteBuilder()
            if (IsUsable(params)) deleteBuilder.setWhere(params.CreateCondition(MixedCondition(params.LogicalOperator), "").Where(deleteBuilder.where()))
            DAO().delete(deleteBuilder.prepare())
        } catch (e: SQLException) {
            e.printStackTrace()
            throw RuntimeException(e)
        }
    }

    fun Count(params: BaseParams<TEntity>): Long {
        return if (EmptyParamReturning() == EmptyParamReturns.Nothing && !IsUsable(params)) 0 else try {
            val qb = DAO().queryBuilder()
            if (IsUsable(params)) qb.setWhere(params.CreateCondition(MixedCondition(params.LogicalOperator), "").Where(qb.where()))
            qb.countOf()
        } catch (e: SQLException) {
            e.printStackTrace()
            throw RuntimeException(e)
        }
    }

    fun Sum(params: BaseParams<TEntity>, column: String?): Number {
        return if (EmptyParamReturning() == EmptyParamReturns.Nothing && !IsUsable(params)) 0 else try {
            val qb = DAO().queryBuilder()
            if (IsUsable(params)) qb.setWhere(params.CreateCondition(MixedCondition(params.LogicalOperator), "").Where(qb.where()))
            qb.selectRaw(String.format("SUM(%s)", column))
            val sum = FarayanUtility.TryParseDouble(qb.queryRaw().firstResult[0])
            sum ?: 0
        } catch (e: SQLException) {
            e.printStackTrace()
            throw RuntimeException(e)
        }
    }

    fun Aggregate(
            params: BaseParams<TEntity>,
            groupByColumn: String?,
            selectGroupByColumn: Boolean,
            vararg aggregateFunctions: SqlAggAndHave?
    ): Array<String?>? {
        return try {
            val aggregates = Aggregates(params, groupByColumn, selectGroupByColumn, *aggregateFunctions)!!.results
            if (aggregates.size == 0) null else aggregates[0]
        } catch (exception: Exception) {
            throw RuntimeException(exception)
        }
    }

    fun Aggregates(
            params: BaseParams<TEntity>,
            groupByColumn: String?,
            selectGroupByColumn: Boolean,
            vararg aggregateFunctions: SqlAggAndHave?
    ): GenericRawResults<Array<String?>?>? {
        return if (EmptyParamReturning() == EmptyParamReturns.Nothing && !IsUsable(params)) null else try {
            val qb = DAO().queryBuilder()
            if (IsUsable(params)) qb.setWhere(params.CreateCondition(MixedCondition(params.LogicalOperator), "").Where(qb.where()))
            var columns = arrayOf<String?>()
            if (FarayanUtility.IsUsable(groupByColumn)) {
                qb.groupBy(groupByColumn)
                if (selectGroupByColumn) {
                    columns = ArrayUtils.add(columns, groupByColumn)
                }
            }
            val preparedColumns = Arrays
                    .stream(aggregateFunctions)
                    .filter { x: SqlAggAndHave? ->
                        FarayanUtility.IsUsable(x?.AggregateExpression)
                    }
                    .map { x: SqlAggAndHave? -> x?.AggregateExpression }
                    .collect(Collectors.toList())
                    .toTypedArray()
            columns = ArrayUtils.addAll(columns, *preparedColumns)
            val having = Arrays
                    .stream(aggregateFunctions)
                    .filter { x: SqlAggAndHave? -> FarayanUtility.IsUsable(x?.HavingCondition) }
                    .map { x: SqlAggAndHave? -> x?.HavingCondition }
                    .collect(Collectors.joining(" and "))
            if (FarayanUtility.IsUsable(having)) qb.having(having)
            qb.selectRaw(*columns)
            qb.queryRaw()
        } catch (e: SQLException) {
            e.printStackTrace()
            throw RuntimeException(e)
        }
    }

    fun DAO(): RuntimeExceptionDao<TEntity, Int>
    fun IsUsable(params: BaseParams<TEntity>?): Boolean {
        return params?.IsUsable() ?: false
    }

    fun Save(item: TEntity): Int {
        return DAO().create(item)
    }

    fun SaveAll(vararg items: TEntity) {
        for (item in items) {
            DAO().create(item)
        }
    }

    fun Update(item: TEntity): Int {
        return DAO().update(item)
    }

    fun UpdateAll(vararg items: TEntity) {
        for (item in items) {
            DAO().update(item)
        }
    }

    fun UpdateAll(items: List<TEntity>) {
        for (item in items) {
            DAO().update(item)
        }
    }

    fun Delete(item: TEntity) {
        DAO().delete(item)
    }

    fun DeleteAll(vararg items: TEntity) {
        DAO().delete(Arrays.asList(*items))
    }

    fun SaveOnNeed(entity: TEntity?) {
        if (entity == null) return
        if (entity.id != 0) return
        Save(entity)
    }

    fun Refreshed(entity: TEntity, forced: Boolean = false): TEntity {
        if (entity == null)
            return entity
        if (forced || entity.NeedsRefresh())
            DAO().refresh(entity)
        return entity
    }

    @Deprecated("")
    fun TEntity?.RefreshedNullables(forced: Boolean = false): TEntity? {
        if (this == null)
            return this
        if (forced || this.NeedsRefresh())
            DAO().refresh(this)
        return this
    }

    fun NewParams(): BaseParams<TEntity>
}


fun <TEntity : IEntity> TEntity.ensured(repo: IRepo<TEntity>, forced: Boolean = false): TEntity {
    if (forced || this.NeedsRefresh())
        repo.Refreshed(this)
    return this
}