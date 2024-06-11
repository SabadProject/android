package farayan.sabad.models.Store;

import android.content.Context;
import android.util.AttributeSet;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import farayan.commons.QueryBuilderCore.BaseParams;
import farayan.commons.QueryBuilderCore.IRepo;
import farayan.commons.QueryBuilderCore.TextFilter;
import farayan.commons.QueryBuilderCore.TextMatchModes;
import farayan.commons.UI.Core.FilterableEntityAdapter;
import farayan.commons.UI.EntityAutoCompleteBox;
import farayan.sabad.core.OnePlace.Store.IStoreRepo;
import farayan.sabad.core.OnePlace.Store.StoreEntity;
import farayan.sabad.core.OnePlace.Store.StoreParams;

@AndroidEntryPoint
public class StoreBox extends EntityAutoCompleteBox<StoreEntity>
{
	private IStoreRepo TheStoreRepo;

	@Inject
	public void Inject(IStoreRepo StoreRepo) {
		TheStoreRepo = StoreRepo;
		Reload();
	}

	public StoreBox(Context context) {
		super(context);
	}

	public StoreBox(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public StoreBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected FilterableEntityAdapter<StoreEntity> CreateAdapter() {
		StoreParams storeParams = new StoreParams();
		storeParams.QueryableName = new TextFilter(null, TextMatchModes.NotNull);
		return new StoreBoxAdapter(getContext(), TheStoreRepo.All(storeParams));
	}

	@Override
	protected FilterableEntityAdapter<StoreEntity> CreateAdapter(BaseParams<StoreEntity> params) {
		return new StoreBoxAdapter(getContext(), TheStoreRepo.All(params));
	}

	@Override
	protected FilterableEntityAdapter<StoreEntity> CreateAdapter(List<StoreEntity> values) {
		return new StoreBoxAdapter(getContext(), values);
	}

	@Override
	protected IRepo<StoreEntity> Repo() {
		return TheStoreRepo;
	}

	@Override
	protected StoreEntity CreateEntity() {
		return new StoreEntity();
	}
}
