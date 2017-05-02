package teamlld.nik.uniobuda.hu.walletapp;


import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.FormatException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;


/**
 * Created by GERGO on 2017.04.01..
 */

interface NewTransactionListener {
    void NewTransactionAdded(Transaction transaction);
}

public class DatabaseHandler {

    // FIXME ennek a résznek nincs sok keresnivalója az adatbázisban, de egyelőre jobb ötletem nem volt
    private List<NewTransactionListener> listeners = new ArrayList<NewTransactionListener>();

    public void addListener(NewTransactionListener toAdd) {
        listeners.add(toAdd);
    }

    private void transactionAdded(Transaction transaction) {
        for (NewTransactionListener listener : listeners)
            listener.NewTransactionAdded(transaction);
    }
    // FIXME


    public final static String DB_NAME = "database";
    public final static int DB_VERSION = 1;
    public final static String TABLE_USERS = "users";
    public final static String TABLE_TRANSACTIONS = "transactions";
    public final static String TABLE_TYPES = "types";

    public static DatabaseHandler handler = null;
    public static DatabaseHelper helper = null;

    private DatabaseHandler(Context context) {
        helper = new DatabaseHelper(context);
    }

    public static DatabaseHandler getInstance(Context context){
        if (handler == null){
            handler = new DatabaseHandler(context);
        }

        return handler;
    }

    public long insertUser(String name, int balance, int userId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("balance", balance);
        values.put("_userId", userId);
        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }

    public long insertTransaction(String name, int value, boolean income, int typeId, long time, int userId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("value", value);
        values.put("income", income);
        values.put("_typeId", typeId);
        values.put("date", time);
        values.put("_userId", userId);

        long id = db.insert(TABLE_TRANSACTIONS, null, values);
        //TODO insertConflict ?
        db.close();

        transactionAdded(new Transaction(name, value, income, typeId, time));

        return id;
    }

    public Cursor getAllTransactionsOrderByDate(int userId, boolean desc) {
        //a userId-hoz tartozó user összes tranzakcióját visszaadja
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor result;

        if (desc) {
            result = db.query(TABLE_TRANSACTIONS, null, "_userId = ?", new String[]{Integer.toString(userId)}, null, null, "_transactionId DESC");
        } else {
            result = db.query(TABLE_TRANSACTIONS, null, "_userId = ?", new String[]{Integer.toString(userId)}, null, null, "_transactionId");
        }

        result.moveToFirst();
        db.close();
        return result;
    }

    public Cursor getAllTransactionsOrderByValue(int userId, boolean desc){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor result;


        if (desc) {
            result = db.query(TABLE_TRANSACTIONS, null, "_userId = ?", new String[]{Integer.toString(userId)}, null, null, "value DESC");
        } else {
            result = db.query(TABLE_TRANSACTIONS, null, "_userId = ?", new String[]{Integer.toString(userId)}, null, null, "value");
        }

        result.moveToFirst();
        db.close();
        return result;
    }

    public Cursor getTransactionsOrderByDate(int userId, boolean desc, boolean income){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor result;


        if (desc) {
            if (income){
                result = db.query(TABLE_TRANSACTIONS, null, "_userId = ? AND value > 0", new String[]{Integer.toString(userId)}, null, null, "_transactionId DESC");
            }
            else {
                result = db.query(TABLE_TRANSACTIONS, null, "_userId = ? AND value <= 0", new String[]{Integer.toString(userId)}, null, null, "_transactionId");
            }
        }
        else {
            if (income){
                result = db.query(TABLE_TRANSACTIONS, null, "_userId = ? AND value > 0", new String[]{Integer.toString(userId)}, null, null, "_transactionId");
            }
            else{
                result = db.query(TABLE_TRANSACTIONS, null, "_userId = ? AND value <= 0", new String[]{Integer.toString(userId)}, null, null, "_transactionId");
            }
        }

        result.moveToFirst();
        db.close();
        return result;
    }

    public Cursor getTransactionsOrderByValue(int userId, boolean desc, boolean income){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor result;


        if (desc) {
            if (income){
                result = db.query(TABLE_TRANSACTIONS, null, "_userId = ? AND value > 0", new String[]{Integer.toString(userId)}, null, null, "value DESC");
            }
            else {
                result = db.query(TABLE_TRANSACTIONS, null, "_userId = ? AND value <= 0", new String[]{Integer.toString(userId)}, null, null, "value DESC");
            }
        }
        else {
            if (income){
                result = db.query(TABLE_TRANSACTIONS, null, "_userId = ? AND value > 0", new String[]{Integer.toString(userId)}, null, null, "value");
            }
            else {
                result = db.query(TABLE_TRANSACTIONS, null, "_userId = ? AND value <= 0", new String[]{Integer.toString(userId)}, null, null, "value");
            }
        }

        result.moveToFirst();
        db.close();
        return result;
    }

    public Cursor getLatestTransactions(int count, int userId) {
        //a userId-hoz tartozó user count db utolsó tranzakcióját adja vissza
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM transactions WHERE _userId = ? ORDER BY _transactionId DESC LIMIT ?", new String[]{Integer.toString(userId), Integer.toString(count)});
        result.moveToFirst();
        db.close();
        return result;
    }

    public Cursor getUserById(int userId) {
        //visszaad egy user-t ID alapján, ha nem létezik akkor a Cursor üres lesz.
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor result = db.query(TABLE_USERS, null, "_userId = ?", new String[]{Integer.toString(userId)}, null, null, null);
        result.moveToFirst();
        db.close();
        return result;
    }

    public Cursor getTypeById(int typeId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor result = db.query(TABLE_TYPES, null, "_id = ?", new String[]{Integer.toString(typeId)}, null, null, null);
        result.moveToFirst();
        db.close();
        return result;
    }

    public Cursor getTypes(boolean income) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor result;
        result = db.query(TABLE_TYPES, null, "income = ?", new String[]{Integer.toString(income ? 1 : 0)}, null, null, null);
        result.moveToFirst();
        db.close();
        return result;
    }

    public void updateUserBalance(int userId, int newBalance) {
        //frissíti a user egyenlegét
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("balance", newBalance);
        db.update(TABLE_USERS, values, "_userId = ?", new String[]{Integer.toString(userId)});
        db.close();
    }

    public int currentTimeToInt() {
        String newdate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        return Integer.parseInt(newdate);
    }

    public void loadDatabaseWithDemoData() {
        Random rnd = new Random();
        Date date1 = new GregorianCalendar(2017, Calendar.FEBRUARY, 1).getTime();
        Date date2 = new GregorianCalendar(2017, Calendar.FEBRUARY, 4).getTime();
        Date date3 = new GregorianCalendar(2017, Calendar.FEBRUARY, 13).getTime();

        //String date="2017040"+(i+1);
        insertTransaction(1 + ". trans.", rnd.nextInt(1000), rnd.nextBoolean(), 1, date1.getTime(), 1000);
        insertTransaction(2 + ". trans.", rnd.nextInt(1000), rnd.nextBoolean(), 2, date2.getTime(), 1000);
        insertTransaction(3 + ". trans.", rnd.nextInt(1000), rnd.nextBoolean(), 3, date3.getTime(), 1000);

    }

    public class DatabaseHelper extends SQLiteOpenHelper {

        Context context;

        public DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_USERS + "(" +
                    "_userId    INTEGER PRIMARY KEY, " +
                    "name   VARCHAR(255)," +
                    "balance   INTEGER" +
                    ")");
            db.execSQL("CREATE TABLE " + TABLE_TRANSACTIONS + "(" +
                    "_transactionId    INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name   VARCHAR(255)," +
                    "value  INTEGER," +
                    "income BOOLEAN," +
                    "_typeId   INTEGER," +
                    "date   INTEGER," +
                    "_userId INTEGER," +
                    "FOREIGN KEY(_userId) REFERENCES " + TABLE_USERS + "(_userId)," +
                    "FOREIGN KEY (_typeId) REFERENCES " + TABLE_TYPES + "(_id)" +
                    ")");
            db.execSQL("CREATE TABLE " + TABLE_TYPES + "(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name VARCHAR(255)," +
                    "income BOOLEAN" +
                    ")");

            InsertInitialTypes(db);
        }

        private void InsertInitialTypes(SQLiteDatabase db) {
            ContentValues values = new ContentValues();
            String[] types_income = context.getResources().getStringArray(R.array.types_income);
            for (int i = 0; i < types_income.length; i++) {
                values.put("name", types_income[i]);
                values.put("income", true);
                db.insert(TABLE_TYPES, null, values);
            }
            String[] types_expense = context.getResources().getStringArray(R.array.types_expense);
            for (int i = 0; i < types_expense.length; i++) {
                values.put("name", types_expense[i]);
                values.put("income", false);
                db.insert(TABLE_TYPES, null, values);
            }
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
