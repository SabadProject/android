package farayan.commons.UI.Core;

public interface ICommandEvent<ValueType> {

    void OnEvent(String command, ValueType value);
}
