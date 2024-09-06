package farayan.commons.QueryBuilderCore;

public interface IFilter {
	boolean IsUsable();

	void Condition(String name, MixedCondition mixed);
}
