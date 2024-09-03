package farayan.commons.QueryBuilderCore;


import farayan.commons.Action;
import farayan.commons.IValueProvider;

public class PropertyFilter {
	/*@Deprecated
	public PropertyFilter(String name, IFilter iFilter) {
		this.Name = name;
		this.Filter = iFilter;
		this.SubSelectPropertyName = "";
		FilterProvider = null;
	}*/

	/*@Deprecated
	public PropertyFilter(String name, IFilter iFilter, String subSelectPropertyName) {
		this.Name = name;
		this.Filter = iFilter;
		this.SubSelectPropertyName = subSelectPropertyName;
		FilterProvider = null;
	}*/

	public PropertyFilter(String name, IValueProvider<IFilter>  iFilterProvider) {
		this.Name = name;
		this.Filter = null;
		this.SubSelectPropertyName = "";
		FilterProvider = iFilterProvider;
	}

	public PropertyFilter(String name, IValueProvider<IFilter>  iFilterProvider, String subSelectPropertyName) {
		this.Name = name;
		this.Filter = null;
		this.SubSelectPropertyName = subSelectPropertyName;
		FilterProvider = iFilterProvider;
	}

	public final String Name;
	@Deprecated
	public final IFilter Filter;
	public final IValueProvider<IFilter> FilterProvider;
	public final String SubSelectPropertyName;
}