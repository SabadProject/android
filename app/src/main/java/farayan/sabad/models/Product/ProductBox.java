package farayan.sabad.models.Product;

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
import farayan.sabad.core.OnePlace.Product.IProductRepo;
import farayan.sabad.core.OnePlace.Product.ProductEntity;
import farayan.sabad.core.OnePlace.Product.ProductParams;

@AndroidEntryPoint
public class ProductBox extends EntityAutoCompleteBox<ProductEntity>
{
	private IProductRepo TheProductRepo;

	@Inject
	public void Inject(IProductRepo productRepo) {
		TheProductRepo = productRepo;
	}

	private ProductParams TheParams;

	public ProductBox(Context context) {
		super(context);
	}

	public ProductBox(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ProductBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected FilterableEntityAdapter<ProductEntity> CreateAdapter() {
		ProductParams params = new ProductParams();
		params.QueryableName = new TextFilter(null, TextMatchModes.NotNull);
		List<ProductEntity> entities = TheProductRepo.All(params);
		return new ProductBoxAdapter(getContext(), entities);
	}

	@Override
	protected FilterableEntityAdapter<ProductEntity> CreateAdapter(BaseParams<ProductEntity> params) {
		return new ProductBoxAdapter(getContext(), TheProductRepo.All(params));
	}

	@Override
	protected FilterableEntityAdapter<ProductEntity> CreateAdapter(List<ProductEntity> values) {
		return new ProductBoxAdapter(getContext(), values);
	}

	@Override
	protected IRepo<ProductEntity> Repo() {
		return TheProductRepo;
	}

	@Override
	protected ProductEntity CreateEntity() {
		return new ProductEntity();
	}
}
