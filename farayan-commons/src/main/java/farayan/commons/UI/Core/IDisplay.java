package farayan.commons.UI.Core;

import farayan.commons.QueryBuilderCore.IEntity;

public interface IDisplay<TValue> {
	void Display(TValue entity);
}
