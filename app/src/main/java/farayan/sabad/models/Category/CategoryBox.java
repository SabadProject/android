package farayan.sabad.models.Category;

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
import farayan.sabad.core.OnePlace.Category.CategoryEntity;
import farayan.sabad.core.OnePlace.Category.CategoryParams;
import farayan.sabad.core.OnePlace.Category.ICategoryRepo;

@AndroidEntryPoint
public class CategoryBox extends EntityAutoCompleteBox<CategoryEntity>
{
	private ICategoryRepo TheCategoryRepo;

	@Inject
	public void Inject(ICategoryRepo StoreRepo) {
		TheCategoryRepo = StoreRepo;
		Reload();
	}

	public CategoryBox(Context context) {
		super(context);
	}

	public CategoryBox(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CategoryBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected FilterableEntityAdapter<CategoryEntity> CreateAdapter() {
		CategoryParams categoryParams = new CategoryParams();
		categoryParams.QueryableName = new TextFilter(null, TextMatchModes.NotNull);
		return new CategoryBoxAdapter(getContext(), TheCategoryRepo.All(categoryParams));
	}

	@Override
	protected FilterableEntityAdapter<CategoryEntity> CreateAdapter(BaseParams<CategoryEntity> params) {
		return new CategoryBoxAdapter(getContext(), TheCategoryRepo.All(params));
	}

	@Override
	protected FilterableEntityAdapter<CategoryEntity> CreateAdapter(List<CategoryEntity> values) {
		return new CategoryBoxAdapter(getContext(), values);
	}

	@Override
	protected IRepo<CategoryEntity> Repo() {
		return TheCategoryRepo;
	}

	@Override
	protected CategoryEntity CreateEntity() {
		return new CategoryEntity();
	}
}
