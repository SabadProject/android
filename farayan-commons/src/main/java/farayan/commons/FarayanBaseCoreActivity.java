package farayan.commons;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FarayanBaseCoreActivity extends AppCompatActivity
{
	public OnPermissionRequestAnswered setOnPermissionRequestAnswered;

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (setOnPermissionRequestAnswered != null)
			setOnPermissionRequestAnswered.answered(requestCode, permissions, grantResults);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EnsureDirection();
	}

	private void EnsureDirection() {
		if(doRTL()){
			getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
		}
	}

	protected boolean doRTL() {
		return true;
	}
}
