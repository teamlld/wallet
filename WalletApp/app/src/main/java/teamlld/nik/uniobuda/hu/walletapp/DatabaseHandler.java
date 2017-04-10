package teamlld.nik.uniobuda.hu.walletapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by GERGO on 2017.04.01..
 */


public class DatabaseHandler {

    public final static String DB_NAME = "database";
    public final static int DB_VERSION = 1;
    public final static String TABLE_USERS = "users";
    public final static String TABLE_TRANSACTIONS = "transactions";

    public DatabaseHelper helper;

    public DatabaseHandler(Context context) {
        helper = new DatabaseHelper(context);
    }

    public long insertUser(String name, int balance) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("balance", balance);

        long id = db.insert(TABLE_USERS, null, values);
        //TODO insertConflict ?
        db.close();
        return id;
    }

    public long insertTransaction(String name, int value, boolean income, String type, int userId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("value", value);
        values.put("income", income);
        values.put("type", type);
        values.put("_userId", userId);
        long id = db.insert(TABLE_TRANSACTIONS, null, values);
        //TODO insertConflict ?
        db.close();
        return id;
    }

    public Cursor getAllTransactions() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor result = db.query(TABLE_TRANSACTIONS, null, null, null, null, null, null);
        result.moveToFirst();
        db.close();
        return result;
    }

    //TODO lekérdezések kitalálása

    public class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_USERS + "(" +
                    "_userId    INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name   VARCHAR(255)," +
                    "balance   INTEGER" +
                    ")");
            db.execSQL("CREATE TABLE " + TABLE_TRANSACTIONS + "(" +
                    "_transactionId    INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name   VARCHAR(255)," +
                    "value  INTEGER," +
                    "income BOOLEAN," +
                    "type   VARCHAR(255)," +
                    //     "date   DATETIME"  + TODO ezt visszarakni, egyelőre egyszerűbb volt enélkül
                    "_userId INTEGER," +
                    "FOREIGN KEY(_userId) REFERENCES " + TABLE_USERS + "(_userId)" +
                    ")");
        }

        //TODO upgradelés lépésekben
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //FIXME QnD
            db.execSQL("DROP TABLE IF EXIST " + TABLE_USERS);
            onCreate(db);
        }
    }

}
