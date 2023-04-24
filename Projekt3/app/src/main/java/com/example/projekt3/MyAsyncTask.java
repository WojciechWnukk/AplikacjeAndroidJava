package com.example.projekt3;

import android.os.AsyncTask;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MyAsyncTask extends AsyncTask<String, Integer, String> {

    private MainActivity mainActivity;
    private String typPliku;
    private int rozmiarPliku;

    public MyAsyncTask(MainActivity activity) {
        this.mainActivity = activity;
        this.typPliku = null;
        this.rozmiarPliku = 0;
    }

    @Override
    protected String doInBackground(String... urls) {
        HttpsURLConnection polaczenie = null;
        FileOutputStream strumienDoPliku = null;
        InputStream StrumienZSieci = null;
        try {
            URL url = new URL(urls[0]);
            polaczenie = (HttpsURLConnection) url.openConnection();
            polaczenie.setRequestMethod("GET");

            rozmiarPliku = polaczenie.getContentLength();
            typPliku = polaczenie.getContentType();

            if (StrumienZSieci != null)
            {
                try {
                    StrumienZSieci.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //odbieranie danych, zamykanie plików i połączenia..
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (polaczenie != null) polaczenie.disconnect();
        }

        //w trakcie wykonania zadania można wysłać informację o postępie
        //argumentem publishProgress też jest Integer.. params - stąd "dziwny" argument
        //publishProgress(new Integer[] {i+1});

        //po zakończeniu zadania zwracamy wynik
        return null;
    }
    @Override
    protected void onProgressUpdate(Integer... values)
    {
        //aktualizacja informacji o postępie
    }
    @Override
    protected void onPostExecute(String string)
    {
        super.onPostExecute(string);
        this.mainActivity.ustawRozmiar(String.valueOf(rozmiarPliku));
        this.mainActivity.ustawTyp(typPliku);
    }
}

