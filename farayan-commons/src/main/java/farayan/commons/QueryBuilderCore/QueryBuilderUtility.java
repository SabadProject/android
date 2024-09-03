package farayan.commons.QueryBuilderCore;

import android.util.Log;

import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.List;

import farayan.commons.FarayanUtility;

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
