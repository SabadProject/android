package farayan.commons.UI.Core;

public interface IGeneralEventProvider<ReturnType, CommandType extends Enum<CommandType>, ValueType> {
	void SetEventHandler(IGeneralEvent<ReturnType, CommandType, ValueType> iEvent);
}
