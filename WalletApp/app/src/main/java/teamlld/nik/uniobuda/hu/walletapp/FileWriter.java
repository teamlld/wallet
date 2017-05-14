package teamlld.nik.uniobuda.hu.walletapp;

import android.database.Cursor;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import teamlld.nik.uniobuda.hu.walletapp.models.User;

/**
 * Created by GERGO on 2017.05.01..
 */

public class FileWriter {

    public static void WriteTextFileTest(Cursor cursor, User user) throws IOException {
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

        writer.write(user.getName() + " egyenlege;" + String.valueOf(user.getBalance()) + "\n");

        while (!cursor.isAfterLast()) {
            Date date = new Date(cursor.getLong(cursor.getColumnIndex(("date"))));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd hh:mm");
            String dateString = formatter.format(date);
            String toWrite = cursor.getString(cursor.getColumnIndex(("name"))) + ";" +
                             dateString + ";" +
                             cursor.getInt(cursor.getColumnIndex(("value"))) + "\n";
            writer.write(toWrite);
            cursor.moveToNext();
        }
        writer.flush();
        writer.close();
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}
