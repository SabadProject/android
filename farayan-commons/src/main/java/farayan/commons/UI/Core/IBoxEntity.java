package farayan.commons.UI.Core;

import farayan.commons.FarayanUtility;
import farayan.commons.QueryBuilderCore.IEntity;

public interface IBoxEntity extends IEntity
{
	String getTitle();

	void setTitle(String title);

	default boolean isFilterMatched(CharSequence criteria) {
		return FarayanUtility.Queryable(getTitle()).contains(FarayanUtility.Queryable(criteria.toString()));
	}
}
