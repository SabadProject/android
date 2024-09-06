package farayan.commons.QueryBuilderCore;

/**
 * Created by Homayoun on 28/05/2017.
 */

public class QueryBuilderUtility {
    public static String Count(String column) {
        return String.format("COUNT(%s)", column);
    }
    public static String Sum(String column) {
        return String.format("SUM(%s)", column);
    }
}
