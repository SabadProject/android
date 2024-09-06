package farayan.commons.UI.Core;

import farayan.commons.QueryBuilderCore.IEntity;

public interface IEntityView<EntityType extends IEntity> {
	void DisplayEntity(EntityType entity);
}
