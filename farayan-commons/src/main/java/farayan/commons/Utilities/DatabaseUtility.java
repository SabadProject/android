package farayan.commons.Utilities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseUtility {
    public static void addColumn(SQLiteDatabase database, String table, String column) {
        if (!containsColumn(database, table, column))
            database.execSQL("alter table `" + table + "` add `" + column + "`");
    }

    public static void dropColumn(SQLiteDatabase database, String table, String column) {
        if (!containsColumn(database, table, column))
            database.execSQL("alter table `" + table + "` drop column `" + column + "`");
    }

    public static void addColumns(SQLiteDatabase database, String table, String... columns) {
        for (String column : columns) {
            DatabaseUtility.addColumn(database, table, column);
        }
    }

    public static void dropColumns(SQLiteDatabase database, String table, String... columns) {
        for (String column : columns) {
            DatabaseUtility.dropColumn(database, table, column);
        }
    }

    public static boolean containsColumn(SQLiteDatabase db, String table, String column) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("PRAGMA table_info(" + table + ")", null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    if (column.equalsIgnoreCase(name)) {
                        return true;
                    }
                }
            }
            return false;
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
    }
}
