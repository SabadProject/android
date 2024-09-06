package farayan.commons.QueryBuilderCore;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtility
{
	public static Gson prepared(){
		return new GsonBuilder()
				.registerTypeAdapter(IEntity.class, new GsonEntitySerializer())
				.setPrettyPrinting()
				.serializeNulls()
				.create();
	}
}
