package teamlld.nik.uniobuda.hu.walletapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import teamlld.nik.uniobuda.hu.walletapp.models.Transaction;

/**
 * Created by GERGO on 2017.05.01..
 */

public class FileWriter {


    String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
    String fileName = "Transactions.csv";
    String filePath = baseDir + File.separator + fileName;

    public void WriteTextFileTest(List<Transaction> list)
    {
        File f = new File(filePath );

        try {
            FileOutputStream stream = new FileOutputStream(f);
            for (int i=0;i<list.size();i++) {
                stream.write(list.get(i).getName().getBytes());

            }
            stream.close();
        }
        catch (IOException e) {

        }

    }
}
