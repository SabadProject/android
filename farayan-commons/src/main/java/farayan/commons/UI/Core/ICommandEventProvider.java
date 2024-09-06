package farayan.commons.UI.Core;

public interface ICommandEventProvider<ValueType> {
    void SetEventHandler(ICommandEvent<ValueType> iCommandEvent);
}

//public interface ICommandEventProvider<CommandType extends Enum<CommandType>, ValueType> {
//    void SetEventHandler(ICommandEvent<CommandType, ValueType> iEvent);
//}
