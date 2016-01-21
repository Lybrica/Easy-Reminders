// ------------------------------------ DBADapter.java ---------------------------------------------

// TODO: Change the package to match your project.
package lybrica.easyreminders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;



// Change the package (at top) to match your project.

public class DBAdapter {

    /////////////////////////////////////////////////////////////////////
    //	Constants & Data
    /////////////////////////////////////////////////////////////////////
    // For logging:
    private static final String TAG = "DBAdapter";

    // DB Fields
    public static final String KEY_ROWID = "_id";
    public static final int COL_ROWID = 0;
    /*
     * CHANGE 1:
     */
    // TODO: Setup your fields here:
    public static final String KEY_TITLE = "title";
    public static final String KEY_NAME = "name";
    public static final String KEY_STUDENTNUM = "studentnum";
    public static final String KEY_FAVCOLOUR = "favcolour";
    public static final String KEY_STYLE = "style";

    // TODO: Setup your field numbers here (0 = KEY_ROWID, 1=...)
    public static final int COL_TITLE = 1;
    public static final int COL_NAME = 2;
    public static final int COL_STUDENTNUM = 3;
    public static final int COL_FAVCOLOUR = 4;
    public static final int COL_STYLE = 5;


    public static final String[] ALL_KEYS = new String[] {KEY_ROWID,KEY_TITLE, KEY_NAME, KEY_STUDENTNUM, KEY_FAVCOLOUR, KEY_STYLE};

    // DB info: it's name, and the table we are using (just one).
    public static final String DATABASE_NAME = "MyDb";
    public static final String DATABASE_TABLE = "mainTable";
    // Track DB version if a new version of your app changes the format.
    public static final int DATABASE_VERSION = 7;

    private static final String DATABASE_CREATE_SQL =
            "create table " + DATABASE_TABLE
                    + " (" + KEY_ROWID + " integer primary key autoincrement, "
			
			/*
			 * CHANGE 2:
			 */
                    // TODO: Place your fields here!
                    // + KEY_{...} + " {type} not null"
                    //	- Key is the column name you created above.
                    //	- {type} is one of: text, integer, real, blob
                    //		(http://www.sqlite.org/datatype3.html)
                    //  - "not null" means it is a required field (must be given a value).
                    // NOTE: All must be comma separated (end of line!) Last one must have NO comma!!
                    + KEY_TITLE + " text not null, "
                    + KEY_NAME + " text not null, "
                    + KEY_STUDENTNUM + " integer not null, "
                    + KEY_FAVCOLOUR + " string not null, "
                    + KEY_STYLE + " string not null"

                    // Rest  of creation:
                    + ");";

    // Context of application who uses us.
    private final Context context;

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;

    /////////////////////////////////////////////////////////////////////
    //	Public methods:
    /////////////////////////////////////////////////////////////////////

    public DBAdapter(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    // Open the database connection.
    public DBAdapter open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    // Close the database connection.
    public void close() {
        myDBHelper.close();
    }

    // Add a new set of values to the database.
    public long insertRow(String title, String name, String studentNum, int favColour, boolean style) {
		/*
		 * CHANGE 3:
		 */
        // TODO: Update data in the row with new fields.
        // TODO: Also change the function's arguments to be what you need!
        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_STUDENTNUM, studentNum);
        initialValues.put(KEY_FAVCOLOUR, favColour);
        initialValues.put(KEY_STYLE, style);

        // Insert it into the database.
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    // Delete a row from the database, by rowId (primary key)
    public boolean deleteRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        return db.delete(DATABASE_TABLE, where, null) != 0;
    }

    public void deleteAll() {
        Cursor c = getAllRows();
        long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
        if (c.moveToFirst()) {
            do {
                deleteRow(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
    }

    // Return all data in the database.
    public Cursor getAllRows() {
        String where = null;
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Get a specific row (by rowId)
    public Cursor getRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


    public ArrayList<String> getIDs() {

        ArrayList<String> strings = new ArrayList<String>();
        String query = String.format("SELECT * FROM mainTable");
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst())
            do {
                strings.add(c.getString(0)); // 0 = columnindex
            } while (c.moveToNext());

        return strings;
    }

    public ArrayList<String> getTitles() {

        ArrayList<String> strings = new ArrayList<String>();
        String query = String.format("SELECT * FROM mainTable");
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst())
            do {
                strings.add(c.getString(1)); // 1 = columnindex
            } while (c.moveToNext());

        return strings;
    }

    public ArrayList<String> getContents() {

        ArrayList<String> strings = new ArrayList<String>();
        String query = String.format("SELECT * FROM mainTable");
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst())
            do {
                strings.add(c.getString(2)); // 2 = columnindex
            } while (c.moveToNext());

        return strings;
    }

    public ArrayList<Integer> getColors() {

        ArrayList<Integer> strings = new ArrayList<Integer>();
        String query = String.format("SELECT * FROM mainTable");
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst())
            do {
                strings.add(c.getInt(4)); // 4 = columnindex
            } while (c.moveToNext());

        return strings;
    }

    public ArrayList<Integer> getStyles() {

        ArrayList<Integer> strings = new ArrayList<Integer>();
        String query = String.format("SELECT * FROM mainTable");
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                strings.add(Integer.parseInt(c.getString(5)));
            } while (c.moveToNext());
        }

        return strings;
    }

    // Change an existing row to be equal to new data.
    public boolean updateRow(long rowId,String title, String name, String studentNum, int favColour, boolean style) {
        String where = KEY_ROWID + "=" + rowId;

		/*
		 * CHANGE 4:
		 */
        // TODO: Update data in the row with new fields.
        // TODO: Also change the function's arguments to be what you need!
        // Create row's data:
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_TITLE, title);
        newValues.put(KEY_NAME, name);
        newValues.put(KEY_STUDENTNUM, studentNum);
        newValues.put(KEY_FAVCOLOUR, favColour);
        newValues.put(KEY_STYLE, style);

        // Insert it into the database.
        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
    }



    /////////////////////////////////////////////////////////////////////
    //	Private Helper Classes:
    /////////////////////////////////////////////////////////////////////

    /**
     * Private class which handles database creation and upgrading.
     * Used to handle low-level database access.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DATABASE_CREATE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

            // Recreate new database:
            onCreate(_db);
        }
    }
}
