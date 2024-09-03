package farayan.commons.QueryBuilderCore;

/**
 * Created by Homayoun on 27/05/2017.
 */

@Deprecated // can be replaced by using IsNull or IsNotNull Operator
public enum PropertyValueConditionModes {
    ProvidedValueOrIgnore(0),
    ProvidedValueOrNull(1),
    ProvidedValueOrHavingValue(2);
    public final  int ServerCode;

    PropertyValueConditionModes(int serverCode) {
        ServerCode = serverCode;
    }
}
