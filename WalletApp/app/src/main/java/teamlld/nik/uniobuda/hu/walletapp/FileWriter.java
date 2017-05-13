package teamlld.nik.uniobuda.hu.walletapp;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import teamlld.nik.uniobuda.hu.walletapp.models.Transaction;

/**
 * Created by GERGO on 2017.05.01..
 */

public class FileWriter {

    public static void WriteTextFileTest(Context context, Cursor cursor) throws IOException
    {
//        String baseDir = android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
//        String fileName = "Transactions.csv";
//        String filePath = baseDir + File.separator + fileName;
       // File f = new File(filePath);

       // File f = new File(android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),"Transaction.csv");
        //File f = new File(android.os.Environment.getExternalStorageDirectory(),"Transactions.csv");

//        File f = new File(context.getFilesDir(),"Transaction.csv");
//
//        f.mkdir();
//        f.createNewFile();

       // FileOutputStream stream = new FileOutputStream(f);


        Log.d("valami",context.getFilesDir().getAbsolutePath());

        FileOutputStream stream = context.openFileOutput("Transaction.csv", context.MODE_PRIVATE);

        while (!cursor.isAfterLast()) {
            String toWrite = cursor.getString(cursor.getColumnIndex(("name"))) + ";" +
                             cursor.getInt(cursor.getColumnIndex(("date"))) + ";" +
                             cursor.getInt(cursor.getColumnIndex(("value"))) + ";\n";
            stream.write(toWrite.getBytes());
            cursor.moveToNext();
        }
        stream.flush();
        stream.close();
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
