package com.example.projekt3;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MainActivity extends Activity {

    private Button info;
    private Button download;
    private TextView link;
    private TextView sizeText;
    private TextView typeText;
    private TextView download_status;
    float procentPobrania;
    PostepInfo postep;
    private static final int KOD_WRITE_EXTERNAL_STORAGE = 2;
    private int requestCode=0;
    int status;

    private BroadcastReceiver mOdbiorcaRozgloszen = new BroadcastReceiver()
    {
        @Override
        //obsługa odebrania komunikatu
        public void onReceive(Context context, Intent intent)
        {

            Bundle bundle = intent.getExtras();
            postep = bundle.getParcelable(DownloadIntentService.INFO);
            download_status = findViewById(R.id.download_status);
            String pobrane = Integer.toString(postep.mPobranychBajtow);
            sizeText = findViewById(R.id.size);
            ProgressBar progres = findViewById(R.id.progressBar);
            String size = Integer.toString(postep.mRozmiar);
            procentPobrania = 100.0f * postep.mPobranychBajtow/postep.mRozmiar;
            status = postep.mStatus;
            progres.setProgress((int) procentPobrania);
            download_status.setText(pobrane);
            sizeText.setText(size);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        info = findViewById(R.id.info);
        link = findViewById(R.id.link);
        sizeText = findViewById(R.id.size);
        typeText = findViewById(R.id.type);
        download = findViewById(R.id.download);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == getPackageManager().PERMISSION_GRANTED) {
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            }
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, KOD_WRITE_EXTERNAL_STORAGE);
        }


        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyAsyncTask(MainActivity.this).execute(link.getText().toString());
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadIntentService.uruchomUsluge(MainActivity.this, link.getText().toString());
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int kodZadania, @NonNull String[] uprawnienia, @NonNull int[] decyzje){
        super.onRequestPermissionsResult(kodZadania, uprawnienia, decyzje);
        switch (kodZadania){
            case KOD_WRITE_EXTERNAL_STORAGE:
                if (uprawnienia.length > 0 && uprawnienia[0].equals(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)  && decyzje[0] == PackageManager.PERMISSION_GRANTED){
                } else{
                }

                break;
            default:
                break;
        }
    }
    @Override //zarejestrowanie odbiorcy
    protected void onResume()
    {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mOdbiorcaRozgloszen, new IntentFilter(DownloadIntentService.POWIADOMIENIE));

    }

    @Override //wyrejestrowanie odbiorcy
    protected void onPause()
    {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mOdbiorcaRozgloszen);
    }
    public void ustawRozmiar(String size){
        sizeText.setText(size);
    }
    public void ustawTyp(String type){
        typeText.setText(type);
    }
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putString("pobrano", download_status.getText().toString());
        outState.putString("rozmiar", sizeText.getText().toString());
        outState.putString("typ", typeText.getText().toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        download_status.setText(savedInstanceState.getString("pobrano"));
        sizeText.setText(savedInstanceState.getString("rozmiar"));
        typeText.setText(savedInstanceState.getString("typ"));
    }

}