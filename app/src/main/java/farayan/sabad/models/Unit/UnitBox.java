package farayan.sabad.models.Unit;

import android.content.Context;
import android.util.AttributeSet;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import farayan.commons.QueryBuilderCore.BaseParams;
import farayan.commons.QueryBuilderCore.IRepo;
import farayan.commons.UI.Core.FilterableEntityAdapter;
import farayan.commons.UI.EntityAutoCompleteBox;
import farayan.sabad.core.OnePlace.Unit.IUnitRepo;
import farayan.sabad.core.OnePlace.Unit.UnitParams;
import farayan.sabad.core.model.unit.UnitEntity;

@AndroidEntryPoint
public class UnitBox extends EntityAutoCompleteBox<UnitEntity> {
    private IUnitRepo TheUnitRepo;

    public UnitBox(Context context) {
        super(context);
    }

    public UnitBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UnitBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Inject
    public void Inject(IUnitRepo unitRepo) {
        TheUnitRepo = unitRepo;
    }

    @Override
    protected FilterableEntityAdapter<UnitEntity> CreateAdapter() {
        return new UnitBoxAdapter(getContext(), TheUnitRepo.All(new UnitParams()));
    }

    @Override
    protected FilterableEntityAdapter<UnitEntity> CreateAdapter(BaseParams<UnitEntity> params) {
        return new UnitBoxAdapter(getContext(), TheUnitRepo.All(params));
    }

    @Override
    protected FilterableEntityAdapter<UnitEntity> CreateAdapter(List<UnitEntity> values) {
        return new UnitBoxAdapter(getContext(), values);
    }

    @Override
    protected IRepo<UnitEntity> Repo() {
        return TheUnitRepo;
    }

    @Override
    protected UnitEntity CreateEntity() {
        return new UnitEntity();
    }
}
