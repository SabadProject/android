package farayan.commons;

public interface OnPermissionRequestAnswered
{
	void answered(int requestCode, String[] permissions, int[] grantResults);
}
