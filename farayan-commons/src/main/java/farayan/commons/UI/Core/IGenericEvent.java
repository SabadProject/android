package farayan.commons.UI.Core;

public interface IGenericEvent<TValue>
{
	static <T> void Exec(IGenericEvent<T> event, T value) {
		if (event != null)
			event.Fire(value);
	}

	void Fire(TValue value);
}
