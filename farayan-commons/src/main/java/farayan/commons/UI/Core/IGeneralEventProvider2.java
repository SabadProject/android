package farayan.commons.UI.Core;

public interface IGeneralEventProvider2<ReturnType, CommandType extends Enum<CommandType>, ValueType> {
	void SetEventHandler2(IGeneralEvent<ReturnType, CommandType, ValueType> iEvent);
}
