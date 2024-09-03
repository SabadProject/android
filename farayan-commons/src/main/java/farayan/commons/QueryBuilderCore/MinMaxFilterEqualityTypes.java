package farayan.commons.QueryBuilderCore;

/**
 * Created by Homayoun on 28/05/2017.
 */

public enum MinMaxFilterEqualityTypes {
    Min(0),
    Max(1),
    None(2),
    Both(3),
    ;
    public final int ServerCode;

    MinMaxFilterEqualityTypes(int serverCode) {
        ServerCode = serverCode;
    }
}
