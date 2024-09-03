package farayan.commons.QueryBuilderCore;

import java.util.UUID;

public class BasePortable
{
	public final long LocalID;
	public final UUID AlwaysID;

	public BasePortable(long localID, UUID alwaysID) {
		LocalID = localID;
		AlwaysID = alwaysID;
	}
}
