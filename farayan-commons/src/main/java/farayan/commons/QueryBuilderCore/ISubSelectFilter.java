package farayan.commons.QueryBuilderCore;

public interface ISubSelectFilter extends IFilter {
	void Condition(String name, MixedCondition mixed, String subSelectPropertyName);
}
