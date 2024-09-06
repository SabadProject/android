package farayan.commons.UI.Core;

public interface IGeneralEvent<ReturnType, CommandType extends Enum<CommandType>, ValueType> {
	ReturnType OnEvent(CommandType command, ValueType value);
}
