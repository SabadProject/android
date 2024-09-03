package farayan.commons.UI;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;


import java.util.List;

import farayan.commons.FarayanUtility;
import farayan.commons.QueryBuilderCore.BaseParams;
import farayan.commons.QueryBuilderCore.IEntity;
import farayan.commons.QueryBuilderCore.IRepo;
import farayan.commons.R;
import farayan.commons.UI.Core.IBoxEntity;
import farayan.commons.UI.Core.IGenericEvent;
import farayan.commons.UI.Core.FilterableEntityAdapter;


public abstract class EntityAutoCompleteBox<EntityType extends IEntity & IBoxEntity> extends CommonAutoCompleteTextBox
{
	private IGenericEvent<EntityType> OnEntityChangedEvent;

	private IGenericEvent<String> OnTextChangedEvent;

	private final AdapterView.OnItemClickListener OnItemClickListener = new AdapterView.OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			EntityType currentValue = adapter.getIEntity(position);
			FireEventOnNeeded(currentValue);
		}
	};

	public EntityAutoCompleteBox(Context context) {
		super(context);
		InitializeComponents();
	}

	public EntityAutoCompleteBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		LoadAttributes(attrs, 0);
		InitializeComponents();
	}

	public EntityAutoCompleteBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LoadAttributes(attrs, defStyle);
		InitializeComponents();
	}

	private void LoadAttributes(AttributeSet attrs, int defStyle) {
		TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.EntityAutoCompleteBox, defStyle, 0);
		try {
			int anInt = typedArray.getInt(R.styleable.EntityAutoCompleteBox_autoSelect, AutoSelects.None.ID);
			setAutoSelect(AutoSelects.ByID(anInt));
		} finally {
			typedArray.recycle();
		}
	}

	private void InitializeComponents() {
		setOnItemClickListener(OnItemClickListener);
	}

	private EntityType lastValue;

	private FilterableEntityAdapter<EntityType> adapter;

	public void Reload(BaseParams<EntityType> params) {
		adapter = CreateAdapter(params);
		setAdapter(adapter);
		reloaded();
	}

	public void Reload(List<EntityType> values) {
		adapter = CreateAdapter(values);
		setAdapter(adapter);
		reloaded();
	}


	@Override
	protected void Reload() {
		adapter = CreateAdapter();
		setAdapter(adapter);
		reloaded();
	}

	public enum AutoSelects
	{
		None(1),
		Single(2),
		First(3);

		private final int ID;

		AutoSelects(int id) {
			ID = id;
		}

		public static AutoSelects ByID(int anInt) {
			for (AutoSelects value : values()) {
				if (value.ID == anInt) {
					return value;
				}
			}
			return null;
		}
	}

	private AutoSelects AutoSelect = AutoSelects.None;

	public void setAutoSelect(AutoSelects autoSelect) {
		AutoSelect = autoSelect;
	}

	private void reloaded() {
		if (AutoSelect == null)
			return;
		switch (AutoSelect) {
			case First:
				if (adapter.getCount() > 0) {
					setValue(adapter.getIEntity(0));
				}
				break;
			case Single:
				if (adapter.getCount() == 1) {
					setValue(adapter.getIEntity(0));
				}
				break;
			case None:
				setValue(null);
				break;
		}
	}

	public void Refresh() {
		Reload();
	}

	protected abstract FilterableEntityAdapter<EntityType> CreateAdapter();

	protected abstract FilterableEntityAdapter<EntityType> CreateAdapter(BaseParams<EntityType> params);

	protected abstract FilterableEntityAdapter<EntityType> CreateAdapter(List<EntityType> values);

	@Override
	protected void InitializeLayout() {
		addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (OnTextChangedEvent != null) {
					OnTextChangedEvent.Fire(s.toString());
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				EntityType currentValue = SelectedEntity(false);
				FireEventOnNeeded(currentValue);
			}
		});
	}

	private void FireEventOnNeeded(EntityType currentValue) {
		Boolean changed = null;
		if (changed == null && lastValue == null && currentValue == null)
			changed = false;

		if (changed == null && lastValue == null && currentValue != null)
			changed = true;

		if (changed == null && lastValue != null && currentValue == null)
			changed = true;

		if (changed == null && lastValue.getID() != currentValue.getID())
			changed = true;

		if (changed == null)
			changed = false;

		lastValue = currentValue;

		if (changed) {
			OnSelectedItemChanged(currentValue);
			if (OnEntityChangedEvent != null)
				OnEntityChangedEvent.Fire(currentValue);
		}
	}

	protected void OnSelectedItemChanged(EntityType currentValue) {

	}

	public EntityType SelectedEntity(boolean autoCreate) {
		if (adapter == null)
			return null;
		String enteredName = getText().toString();
		if (FarayanUtility.IsNullOrEmpty(enteredName))
			return null;

		EntityType selectedItem;
		for (int index = 0; index < adapter.getCount(); index++) {
			if (verifyEntityTitle(((IBoxEntity) adapter.getItem(index)), enteredName)) {
				selectedItem = (EntityType) adapter.getItem(index);
				return selectedItem;
			}
		}
		if (autoCreate) {
			EntityType item = CreateEntity();
			item.setTitle(enteredName);
			Repo().Save(item);
			adapter.addItem(item);
			adapter.notifyDataSetChanged();
			return item;
		} else {
			return null;
		}
	}

	protected boolean verifyEntityTitle(IBoxEntity iEntity, String enteredName) {
		if (iEntity == null)
			return false;
		if (FarayanUtility.IsNullOrEmpty(iEntity.getTitle()))
			return false;
		return iEntity.getTitle().equalsIgnoreCase(enteredName);
	}

	protected abstract IRepo<EntityType> Repo();

	protected abstract EntityType CreateEntity();

	/**
	 * گزینه کنونی را انتخاب می‌کند
	 * دقت کنید! نسخه‌ی ارسالی باید از پایگاه‌داده بازخوانی شده باشد!
	 *
	 * @param value
	 */
	public void setValue(EntityType value) {
		if (value != null) {
			setText(value.getTitle());
		} else {
			setSelection(0);
		}
	}

	@Override
	protected CharSequence textValue(Object selectedItem) {
		if (selectedItem == null)
			return null;
		return ((EntityType) selectedItem).getTitle();
	}

/*	protected CharSequence ItemDisplayText(EntityType selectedItem) {
		if (selectedItem == null)
			return null;
		return selectedItem.getTitle();
	}*/

	@Override
	public int getImeOptions() {
		return EditorInfo.IME_ACTION_NEXT;
	}

	@Override
	public int getInputType() {
		return EditorInfo.TYPE_CLASS_TEXT;
	}

	public void setOnEntityChangedEvent(IGenericEvent<EntityType> onEntityChangedEvent) {
		OnEntityChangedEvent = onEntityChangedEvent;
	}

	public void setOnTextChangedEvent(IGenericEvent<String> onTextChangedEvent) {
		OnTextChangedEvent = onTextChangedEvent;
	}
}
