package teamlld.nik.uniobuda.hu.walletapp;

import android.os.Handler;
import android.os.Looper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by admin on 2017. 05. 01..
 */

public class JsonDownloader extends Thread {

    protected String url;
    protected String text;
    protected Handler handler;
    protected ResultListener onResultListener;

    public JsonDownloader(String url) {
        this.url = url;
        this.handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void run() {
        try {
            URL parsedUrl = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) parsedUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream is = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuffer stringBuffer = new StringBuffer();

                String line = "";
                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line);
                }
                postSuccess(stringBuffer.toString());
                return;
            }

            postFailed("Error...");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setOnResultListener(ResultListener onResultListener) {
        this.onResultListener = onResultListener;
    }

    protected void postSuccess(final String text) {
        this.handler.post(new Runnable() {
            @Override
            public void run() {
                if (onResultListener != null)
                    onResultListener.onSuccess(text);
            }
        });
    }

    protected void postFailed(final String message) {
        this.handler.post(new Runnable() {
            @Override
            public void run() {
                if (onResultListener != null)
                    onResultListener.onSuccess(message);
            }
        });
    }

    public interface ResultListener {

        void onSuccess(String text);

        void onFailed(String message);
    }
}
