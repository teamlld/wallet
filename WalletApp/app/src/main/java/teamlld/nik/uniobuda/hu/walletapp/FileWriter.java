package teamlld.nik.uniobuda.hu.walletapp;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import teamlld.nik.uniobuda.hu.walletapp.models.Transaction;

/**
 * Created by GERGO on 2017.05.01..
 */

public class FileWriter {

    public static void WriteTextFileTest(Cursor cursor) throws IOException {
        String baseDir = android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
        String fileName = "Transactions.csv";
        String filePath = baseDir + File.separator + fileName;

        File file = new File(filePath);
        File fileDir = new File(baseDir);

        if (!fileDir.exists())
        {
            fileDir.mkdirs();
        }

        if (!file.exists())
        {
            file.createNewFile();
        }

        FileOutputStream stream = new FileOutputStream(file,false);
        OutputStreamWriter writer = new OutputStreamWriter(stream);

        while (!cursor.isAfterLast()) {
            String toWrite = cursor.getString(cursor.getColumnIndex(("name"))) + ";" +
                             cursor.getInt(cursor.getColumnIndex(("date"))) + ";" +
                             cursor.getInt(cursor.getColumnIndex(("value"))) + ";\n";
            writer.write(toWrite);
            cursor.moveToNext();
        }
        writer.flush();
        writer.close();
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
