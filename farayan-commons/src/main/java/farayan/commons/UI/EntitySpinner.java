package farayan.commons.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import farayan.commons.QueryBuilderCore.IEntity;
import farayan.commons.UI.Core.FilterableEntityAdapter;
import farayan.commons.UI.Core.GeneralCommands;
import farayan.commons.UI.Core.IBoxEntity;
import farayan.commons.UI.Core.IGeneralEvent;
import farayan.commons.UI.Core.IGeneralEventProvider;


public abstract class EntitySpinner<EntityType extends IEntity & IBoxEntity>
		extends Spinner
		implements IGeneralEventProvider<Void, GeneralCommands, EntityType>
{

	private IGeneralEvent<Void, GeneralCommands, EntityType> TheEvent;
	private EntityType selectedEntity;

	public EntitySpinner(Context context) {
		super(context);
		InitializeComponents();
	}

	public EntitySpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		InitializeComponents();
	}

	public EntitySpinner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		InitializeComponents();
	}

	private void InitializeComponents() {
		setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
				selectedEntity = adapter.getIEntity(position);
				if (TheEvent != null)
					TheEvent.OnEvent(GeneralCommands.Picked, selectedEntity);
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {
				selectedEntity = null;
				if (TheEvent != null)
					TheEvent.OnEvent(GeneralCommands.Picked, null);
			}
		});
		Reload();
	}

	private FilterableEntityAdapter<EntityType> adapter;

	protected void Reload() {
		adapter = CreateAdapter();
		setAdapter(adapter);
	}

	public void Refresh() {
		Reload();
	}

	protected abstract FilterableEntityAdapter<EntityType> CreateAdapter();

	protected abstract RuntimeExceptionDao<EntityType, Integer> DAO();

	/**
	 * گزینه کنونی را انتخاب می‌کند
	 * دقت کنید! نسخه‌ی ارسالی باید از پایگاه‌داده بازخوانی شده باشد!
	 *
	 * @param value
	 */
	public void setValue(EntityType value) {
	}

	@Override
	public void SetEventHandler(IGeneralEvent<Void, GeneralCommands, EntityType> iEvent) {
		TheEvent = iEvent;
	}

	public EntityType getSelectedEntity() {
		return selectedEntity;
	}

	public void setSelectedEntity(EntityType selectedEntityValue) {
		if (selectedEntityValue != null) {
			int itemPositionByID = adapter.getItemPositionByID(selectedEntityValue.getID());
			setSelection(itemPositionByID);
		} else {
			setSelection(0);
		}
		this.selectedEntity = selectedEntityValue;
	}
}
