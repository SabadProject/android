package farayan.commons.QueryBuilderCore;

/**
 * Created by Homayoun on 28/11/2016.
 */

public enum SortDirections {
    Ascending(true),
    Descending(false);

    public final boolean a2z;

    SortDirections(boolean a2z) {
        this.a2z = a2z;
    }
}
