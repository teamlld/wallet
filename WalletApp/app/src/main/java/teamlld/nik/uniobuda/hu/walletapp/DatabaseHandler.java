package teamlld.nik.uniobuda.hu.walletapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by GERGO on 2017.04.01..
 */

interface NewTransactionListener{
    void NewTransactionAdded(Transaction transaction);
}

public class DatabaseHandler {

    // FIXME ennek a résznek nincs sok keresnivalója az adatbázisban, de egyelőre jobb ötletem nem volt
    private List<NewTransactionListener> listeners = new ArrayList<NewTransactionListener>();

    public void addListener(NewTransactionListener toAdd) {
        listeners.add(toAdd);
    }

    private void transactionAdded(Transaction transaction)
    {
        for (NewTransactionListener listener : listeners)
            listener.NewTransactionAdded(transaction);
    }
    // FIXME


    public final static String DB_NAME = "database";
    public final static int DB_VERSION = 1;
    public final static String TABLE_USERS = "users";
    public final static String TABLE_TRANSACTIONS = "transactions";

    public DatabaseHelper helper;

    public DatabaseHandler(Context context) {
        helper = new DatabaseHelper(context);
    }

    public long insertUser(String name, int balance, int userId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("balance", balance);
        values.put("_userId",userId);
        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }

    public long insertTransaction(String name, int value, boolean income, String type, long dateTime, int userId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("value", value);
        values.put("income", income);
        values.put("type", type);
        values.put("date", dateTime);
        values.put("_userId", userId);

        long id = db.insert(TABLE_TRANSACTIONS, null, values);
        //TODO insertConflict ?
        db.close();

        transactionAdded(new Transaction(name,value,income,type,dateTime));

        return id;
    }

    public Cursor getAllTransactions(int userId) {
        //a userId-hoz tartozó user összes tranzakcióját visszaadja
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor result = db.query(TABLE_TRANSACTIONS, null, "_userId = ?", new String[] {Integer.toString(userId)}, null, null, "_transactionId DESC");
        result.moveToFirst();
        db.close();
        return result;
    }

    public Cursor getLatestTransactions(int count,int userId) {
        //a userId-hoz tartozó user count db utolsó tranzakcióját adja vissza
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM transactions WHERE _userId = ? ORDER BY _transactionId DESC LIMIT ?",new String[]{Integer.toString(userId), Integer.toString(count)});
        result.moveToFirst();
        db.close();
        return result;
    }

    public Cursor getUserById(int userId)
    {
        //visszaad egy user-t ID alapján, ha nem létezik akkor a Cursor üres lesz.
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor result = db.query(TABLE_USERS, null, "_userId = ?",new String[]{Integer.toString(userId)} , null, null, null);
        result.moveToFirst();
        db.close();
        return result;
    }

    public void updateUserBalance(int userId, int newBalance)
    {
        //frissíti a user egyenlegét
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("balance",newBalance);
        db.update(TABLE_USERS,values,"_userId = ?",new String[]{Integer.toString(userId)});
        db.close();
    }

   /* public static String converDateToString(Date time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        //Date date = new Date();
        return dateFormat.format(time);
    }

    public static Date convertStringToDate(String dateTme)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1=new Date();
        try {
            date1 = simpleDateFormat.parse(dateTme);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }*/

    public void generateDemoDataTransactions(int userid)
    {
        Random rnd=new Random();
        for (int i=0;i<20;i++)
        {
            insertTransaction(i+". tranzakció",rnd.nextInt(15),rnd.nextBoolean(),"típus", Calendar.getInstance().getTimeInMillis(),userid);
        }
    }

    public class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
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
                    "type   VARCHAR(255)," +
                    "date   INTEGER," +
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
