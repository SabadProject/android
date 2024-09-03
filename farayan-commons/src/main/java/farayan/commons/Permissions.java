package farayan.commons;

import android.Manifest;

public enum Permissions
{
	Camera(Manifest.permission.CAMERA, 0),
	FineLocation(Manifest.permission.ACCESS_FINE_LOCATION, 1),
	CoarseLocation(Manifest.permission.ACCESS_COARSE_LOCATION, 2),
	WriteExternalStorage(Manifest.permission.WRITE_EXTERNAL_STORAGE, 3),
	;

	public final String Name;
	public final int Code;

	Permissions(String name, int code) {
		Name = name;
		Code = code;
	}
}
