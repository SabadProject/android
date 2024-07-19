package farayan.sabad.models.Unit;

import android.content.Context;

import java.util.Collection;

import farayan.commons.UI.Core.FilterableEntityAdapter;
import farayan.commons.UI.Core.IEntityView;
import farayan.sabad.core.model.unit.UnitEntity;
import farayan.sabad.ui.UnitBoxItemComponent;

public class UnitBoxAdapter extends FilterableEntityAdapter<UnitEntity> {
    public UnitBoxAdapter(Context ctx, Collection<? extends UnitEntity> items) {
        super(ctx, items);
    }

    @Override
    protected IEntityView<UnitEntity> NewView(Context ctx) {
        return new UnitBoxItemComponent(ctx);
    }
}
