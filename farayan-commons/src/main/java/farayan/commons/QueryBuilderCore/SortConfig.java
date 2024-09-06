package farayan.commons.QueryBuilderCore;

import farayan.commons.FarayanUtility;

public class SortConfig {
    private String Column;
    private SortDirections Direction = SortDirections.Ascending;

    public SortConfig(String column) {
        Column = column;
    }

    public SortConfig(String column, SortDirections direction) {

        Column = column;
        Direction = direction;
    }

    public String getColumn() {
        return Column;
    }

    public void setColumn(String column) {
        Column = column;
    }

    public SortDirections getDirection() {
        if (Direction == null)
            return SortDirections.Ascending;
        return Direction;
    }

    public void setDirection(SortDirections direction) {
        Direction = direction;
    }

    public boolean IsUsable() {
        return FarayanUtility.IsUsable(Column);
    }

    public void By(String column, SortDirections direction) {
        setColumn(column);
        setDirection(direction);
    }
}
