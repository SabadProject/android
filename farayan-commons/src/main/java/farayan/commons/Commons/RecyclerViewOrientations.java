package farayan.commons.Commons;

import androidx.recyclerview.widget.LinearLayoutManager;

;

public enum RecyclerViewOrientations
{
	Vertical(LinearLayoutManager.VERTICAL),
	Horizontal(LinearLayoutManager.HORIZONTAL);

	public final int Value;

	RecyclerViewOrientations(int value) {
		Value = value;
	}
}
