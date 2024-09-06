package farayan.commons.UI.Core;

import farayan.commons.FarayanUtility;
import farayan.commons.QueryBuilderCore.IEntity;

/**
 * هم متن جستجو و هم موجودیت را نگه می‌دارد
 */
public class Quentity<TEntity extends IEntity>
{
	public final String Query;
	public final TEntity Entity;

	public Quentity(String query, TEntity entity) {
		Query = query;
		Entity = entity;
	}

	public boolean CreateAllowed() {
		return Entity == null && FarayanUtility.IsUsable(Query);
	}
}
