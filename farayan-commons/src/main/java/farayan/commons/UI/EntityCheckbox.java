package farayan.commons.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import farayan.commons.QueryBuilderCore.IEntity;
import farayan.commons.UI.Core.GeneralCommands;
import farayan.commons.UI.Core.IBoxEntity;
import farayan.commons.UI.Core.IGeneralEvent;
import farayan.commons.UI.Core.IGeneralEventProvider;


public abstract class EntityCheckbox<EntityType extends IEntity & IBoxEntity>
		extends HorizontalScrollView
		implements IGeneralEventProvider<Void, GeneralCommands, EntityType>
{

	private IGeneralEvent<Void, GeneralCommands, EntityType> TheEvent;
	private final List<EntityType> selectedEntities = new ArrayList<>();
	private CompoundButton.OnCheckedChangeListener checkBox_OnCheckedChangeListener = (buttonView, isChecked) -> {
		EntityType entity = (EntityType) buttonView.getTag();
		if (isChecked) {
			boolean current = selectedEntities.stream().anyMatch(x -> x.getID() == entity.getID());
			if (!current)
				selectedEntities.add(entity);
		} else {
			boolean current = selectedEntities.stream().anyMatch(x -> x.getID() == entity.getID());
			if (current)
				selectedEntities.remove(entity);
		}
		if (TheEvent != null)
			TheEvent.OnEvent(GeneralCommands.Filter, entity);
	};
	private boolean loaded = false;
	private LinearLayout linearLayout;

	public EntityCheckbox(Context context) {
		super(context);
		InitializeComponents();
	}

	public EntityCheckbox(Context context, AttributeSet attrs) {
		super(context, attrs);
		InitializeComponents();
	}

	public EntityCheckbox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		InitializeComponents();
	}

	private void InitializeComponents() {

		Reload();

	}

	private List<EntityType> adapter;

	protected void Reload() {
		adapter = Entities();
		this.removeAllViews();
		linearLayout = new LinearLayout(getContext());
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		addView(linearLayout);
		for (EntityType entityType : adapter) {
			CheckBox checkBox = new CheckBox(getContext());
			checkBox.setText(getText(entityType));
			checkBox.setTag(entityType);
			checkBox.setOnCheckedChangeListener(checkBox_OnCheckedChangeListener);
			prepareCheckbox(checkBox);
			linearLayout.addView(checkBox);
		}
		loaded = true;
	}

	protected abstract void prepareCheckbox(CheckBox checkBox);

	protected abstract String getText(EntityType entityType);

	protected abstract List<EntityType> Entities();

	protected abstract RuntimeExceptionDao<EntityType, Integer> DAO();

	@Override
	public void SetEventHandler(IGeneralEvent<Void, GeneralCommands, EntityType> iEvent) {
		TheEvent = iEvent;
	}

	public void setSelectedEntities(EntityType... selectedEntities) {
		ensureLoaded();
		final int childrenCount = linearLayout.getChildCount();
		for (int index = 0; index < childrenCount; index++) {
			View child = linearLayout.getChildAt(index);
			if (child instanceof CheckBox) {
				CheckBox checkBox = (CheckBox) child;
				EntityType tag = (EntityType) checkBox.getTag();
				boolean selected = Arrays.stream(selectedEntities).anyMatch(x -> x.getID() == tag.getID());
				checkBox.setChecked(selected);
			}
		}
		this.selectedEntities.clear();
		this.selectedEntities.addAll(Arrays.stream(selectedEntities).collect(Collectors.toList()));
	}

	private void ensureLoaded() {
		if (!loaded)
			Reload();
	}

	public List<EntityType> getSelectedEntities() {
		return new ArrayList<>(selectedEntities);

		/*final int childrenCount = getChildCount();
		List<EntityType> selecteds = new ArrayList<>();
		for (int index = 0; index < childrenCount; index++) {
			View child = getChildAt(index);
			if (child instanceof CheckBox && ((CheckBox) child).isChecked()) {
				selecteds.add((EntityType) child.getTag());
			}
		}
		return selecteds;*/
	}
}
