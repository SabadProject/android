package farayan.commons.Utilities;

import farayan.commons.QueryBuilderCore.BaseEntity;
import farayan.commons.QueryBuilderCore.IRepo;

public class EntityUtility
{

	/**
	 * @param entity
	 * @param <TEntity>
	 * @return
	 */
	@Deprecated
	public static <TEntity extends BaseEntity<TEntity>> TEntity Refreshed(IRepo<TEntity> repo, TEntity entity) {
		return repo.Refreshed(entity, false);
		/*if (entity == null)
			return null;
		return entity.Refreshed();*/
	}
}
