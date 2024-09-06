package farayan.commons.QueryBuilderCore;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.field.DatabaseField;

import java.util.UUID;

public abstract class BaseEntity<TEntity extends IEntity> implements IEntity
{
	@DatabaseField(columnName = BaseSchema.ID, generatedId = true)
	public int ID;

	@DatabaseField(columnName = BaseSchema.AlwaysID)
	public UUID AlwaysID = UUID.randomUUID();

	@Override
	public int getID() {
		return ID;
	}

	/**
	 * موجودیت را در صورتی که لازم باشد، از بانک اطلاعاتی می‌خواند و سپس برمی‌گرداند
	 *
	 * @return
	 */
	@Deprecated
	public TEntity Refreshed(IRepo<TEntity> repo) {
		return repo.Refreshed((TEntity) this, false);
	}

	@Deprecated
	public TEntity RefreshForced(IRepo<TEntity> repo) {
		return repo.Refreshed((TEntity) this, true);
	}

	public abstract RuntimeExceptionDao<TEntity, Integer> DAO();

	/**
	 * موجودیت را در صورتی که لازم باشد، از بانک اطلاعاتی می‌خواند و سپس برمی‌گرداند
	 *
	 * @return
	 */
	@Deprecated
	public TEntity Refreshed() {
		if (DAO() == null)
			throw new RuntimeException("DAO is null");
		if (getID() > 0 && NeedsRefresh())
			DAO().refresh((TEntity) this);
		return (TEntity) this;
	}

	/**
	 * موجودیت را در صورتی که لازم باشد، از بانک اطلاعاتی می‌خواند و سپس برمی‌گرداند
	 *
	 * @return
	 */
	@Deprecated
	public TEntity Refreshed(boolean refreshForced) {
		if (DAO() == null)
			throw new RuntimeException("DAO is null");
		if (refreshForced) {
			DAO().refresh((TEntity) this);
			return (TEntity) this;
		}

		return Refreshed();
	}
}
