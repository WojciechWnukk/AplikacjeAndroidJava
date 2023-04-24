package com.example.projekt3;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class DownloadIntentService extends IntentService {
    private static final String AKCJA_ZADANIE1 = "com.example.intent_service.action.zadanie1";
    private static final String PARAMETR1 = "com.example.intent_service.extra.url";
    private static final int ID_POWIADOMIENIA = 1;
    private static final String ID_KANALU = "kanal";
    private NotificationManager mManagerPowiadomien;
    public final static String POWIADOMIENIE = "com.example.intent_service.odbiornik";
    public final static String INFO = "info";


    public static void uruchomUsluge(Context context, String parametr){
        Intent zamiar = new Intent(context, DownloadIntentService.class);
        zamiar.setAction(AKCJA_ZADANIE1);
        zamiar.putExtra(PARAMETR1, parametr);
        context.startService(zamiar);
    }

    public DownloadIntentService(){
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mManagerPowiadomien = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        przygotujKanalPowiadomien();
        startForeground(ID_POWIADOMIENIA, utworzPowiadomienie());

        if(intent != null){
            final String action = intent.getAction();
            if(AKCJA_ZADANIE1.equals(action)) {
                final String url = intent.getStringExtra(PARAMETR1);
                wykonajZadanie(url);
            }else{
                Log.e("intent_service", "nieznana akcja");
            }
        }
        Log.d("intent_service", "usługa wykonała zadanie");
        mManagerPowiadomien.notify(ID_POWIADOMIENIA, utworzPowiadomienie());
    }



    private void wykonajZadanie(String strona) {
        HttpURLConnection polaczenie = null;
        FileOutputStream strumienDoPliku = null;
        int mPobranychBajtow = 0;
        InputStream strumienZSieci = null;
        PostepInfo progres = new PostepInfo();
        mPobranychBajtow = 0;
        try {
            URL url = new URL(strona);
            polaczenie = (HttpsURLConnection) url.openConnection();
            polaczenie.setRequestMethod("GET");
            File plikRoboczy = new File(url.getFile());
            File plikWyjsciowy = new File(Environment.getExternalStorageDirectory() + File.separator+ plikRoboczy.getName());
            if (plikWyjsciowy.exists())
            {
                plikWyjsciowy.delete();
            }
            DataInputStream czytnik = new DataInputStream(polaczenie.getInputStream());
            strumienDoPliku = new FileOutputStream(plikWyjsciowy.getPath());
            byte bufor[] = new byte[1024];
            progres.mRozmiar = polaczenie.getContentLength();
            progres.mStatus = 0;
            int pobrano = czytnik.read(bufor, 0, 1024);
            while (pobrano != -1)
            {
                strumienDoPliku.write(bufor, 0, pobrano);
                mPobranychBajtow += pobrano;
                progres.mPobranychBajtow = mPobranychBajtow;
                pobrano = czytnik.read(bufor, 0, 1024);
                Log.d("intent_service","Pobrano " + mPobranychBajtow + " bajtow pliku " + plikWyjsciowy.getName());
                wyslijBroadcast(progres);
            }
            progres.mStatus = 1;
            wyslijBroadcast(progres);
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (strumienDoPliku != null)
            {
                try {
                    strumienDoPliku.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (strumienZSieci != null)
            {
                try {
                    strumienZSieci.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (polaczenie != null) polaczenie.disconnect();
        }
    }


    private void przygotujKanalPowiadomien()
    {
        mManagerPowiadomien = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //Android 8/Oreo wymaga kanału powiadomień
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE)
        {
            CharSequence name = getString(R.string.app_name);
            //tworzymy kanał - nadajemy mu jakieś ID (stała typu String)
            NotificationChannel kanal = new NotificationChannel(ID_KANALU, name, NotificationManager.IMPORTANCE_LOW);

            //tworzymy kanał w menadżerze powiadomień
            mManagerPowiadomien.createNotificationChannel(kanal);
        }
    }


    private Notification utworzPowiadomienie(){
        Intent intencjaPowiadomienia = new Intent(this, MainActivity.class);
       intencjaPowiadomienia.putExtra("Intencja", MainActivity.class);

        TaskStackBuilder budowniczStosu = TaskStackBuilder.create(this);
        budowniczStosu.addParentStack(MainActivity.class);
        budowniczStosu.addNextIntent(intencjaPowiadomienia);
        PendingIntent intencjaOczekujaca = budowniczStosu.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder budowniczyPowiadomien = new Notification.Builder(this);
        budowniczyPowiadomien.setContentTitle("Pobieram plik").setProgress(100, wartoscPostepu(), false).setContentIntent(intencjaOczekujaca).setSmallIcon(R.drawable.ic_launcher_foreground).setWhen(System.currentTimeMillis()).setPriority(Notification.PRIORITY_HIGH);
        if (wartoscPostepu()<100) //jezeli pobieranie trwa...
        {
            //budowniczyPowiadomien.setProgress(100, (int) procentPobrania,false);
            budowniczyPowiadomien.setOngoing(false);
        }
        else
        {
            budowniczyPowiadomien.setOngoing(true);
        }

        //ustawiamy kanał powiadomień dla tworzonego powiadomienia
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE)
        {
            budowniczyPowiadomien.setChannelId(ID_KANALU);
        }

        //tworzymy i zwracamy powiadomienie
        return budowniczyPowiadomien.build();
    }
    private int wartoscPostepu() //do zrobienia
    {
        return 0;
    }

    public void wyslijBroadcast(PostepInfo postep)
    {
        Intent zamiar = new Intent(POWIADOMIENIE);
        zamiar.putExtra(INFO, postep);
        LocalBroadcastManager.getInstance(this).sendBroadcast(zamiar);
    }


}
