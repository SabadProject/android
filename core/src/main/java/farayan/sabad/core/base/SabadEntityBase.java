package farayan.sabad.core.base;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.field.DatabaseField;

import farayan.commons.QueryBuilderCore.BaseEntity;
import farayan.commons.QueryBuilderCore.IEntity;

public abstract class SabadEntityBase<TEntity extends IEntity> extends BaseEntity<TEntity>
{
	@DatabaseField(columnName = SabadEntitySchema.GlobalID)
	public Long GlobalID;

	@DatabaseField(columnName = SabadEntitySchema.PendingSync)
	public boolean PendingSync;

	@DatabaseField(columnName = SabadEntitySchema.LastSynced)
	public Long LastSynced;

	@Override
	public RuntimeExceptionDao<TEntity, Integer> DAO() {
		return null;
	}
}
