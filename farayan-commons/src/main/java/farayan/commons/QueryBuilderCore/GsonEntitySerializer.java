package farayan.commons.QueryBuilderCore;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class GsonEntitySerializer implements JsonSerializer<IEntity>
{
	@Override
	public JsonElement serialize(IEntity src, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(src.getID());
	}
}
