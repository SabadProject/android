package farayan.sabad.models.Group;

import android.content.Context;
import android.util.AttributeSet;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import farayan.commons.QueryBuilderCore.BaseParams;
import farayan.commons.QueryBuilderCore.IRepo;
import farayan.commons.UI.Core.FilterableEntityAdapter;
import farayan.commons.UI.EntityAutoCompleteBox;
import farayan.sabad.SabadTheApp;
import farayan.sabad.core.OnePlace.Group.GroupEntity;
import farayan.sabad.core.OnePlace.Group.GroupParams;
import farayan.sabad.core.OnePlace.Group.IGroupRepo;

@AndroidEntryPoint
public class GroupBox extends EntityAutoCompleteBox<GroupEntity>
{
	public IGroupRepo TheGroupRepo;

	@Inject
	public void Inject(IGroupRepo repo) {
		this.TheGroupRepo = repo;
		Reload();
	}

	public GroupBox(Context context) {
		super(context);
	}

	public GroupBox(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GroupBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected FilterableEntityAdapter<GroupEntity> CreateAdapter() {
		return new GroupBoxAdapter(SabadTheApp.getContext(), Repo().All(new GroupParams()));
	}

	@Override
	protected FilterableEntityAdapter<GroupEntity> CreateAdapter(BaseParams<GroupEntity> params) {
		return new GroupBoxAdapter(SabadTheApp.getContext(), Repo().All(params));
	}

	@Override
	protected FilterableEntityAdapter<GroupEntity> CreateAdapter(List<GroupEntity> values) {
		return new GroupBoxAdapter(SabadTheApp.getContext(), values);
	}

	@Override
	protected IRepo<GroupEntity> Repo() {
		return TheGroupRepo;
	}

	@Override
	protected GroupEntity CreateEntity() {
		return new GroupEntity();
	}
}
