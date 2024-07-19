package farayan.sabad.ui;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import farayan.commons.UI.Core.IEntityView;
import farayan.sabad.core.model.unit.UnitEntity;

public class UnitBoxItemComponent extends UnitBoxItemComponentParent implements IEntityView<UnitEntity> {
    private UnitEntity TheEntity;

    public UnitBoxItemComponent(Context context) {
        super(context);
    }

    public UnitBoxItemComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UnitBoxItemComponent(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public UnitBoxItemComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void DisplayEntity(UnitEntity entity) {
        TheEntity = entity;
        NameTextView().setText(entity.getDisplayableName());
    }
}
