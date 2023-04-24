package com.example.lab1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Oceny extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ListAdapter adapter;
    private boolean[] checkedStates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oceny);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String[] nazwyPrzedmiotow = getResources().getStringArray(R.array.tablica);
        Integer liczbaPrzedmiotow = getIntent().getIntExtra("KLUCZ", 0);

        if (savedInstanceState != null) {
            checkedStates = savedInstanceState.getBooleanArray("checkedStates");
        } else {
            checkedStates = new boolean[liczbaPrzedmiotow];
        }

        List<ListForAdapter> listaOcen = new ArrayList<>();

        for (int i = 0; i < liczbaPrzedmiotow; i++) {
            ListForAdapter przedmiot = new ListForAdapter(nazwyPrzedmiotow[i], 0);
            przedmiot.setChecked(checkedStates[i]);
            listaOcen.add(przedmiot);
        }

        adapter = new ListAdapter(listaOcen, LayoutInflater.from(this));
        recyclerView.setAdapter(adapter);


        Button button = (Button) findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Double srednia = 0.0;
                int liczbaOcen= 0;

                for(ListForAdapter przedmiot : adapter.listaOcen){
                    int ocena = przedmiot.getOcena();
                    if (ocena > 0) {
                        srednia += ocena;
                        liczbaOcen++;
                    }
                }
                if (liczbaOcen > 0) {
                    srednia /= liczbaOcen;
                }

                Intent intent2 = new Intent();
                intent2.putExtra("SREDNIA", srednia.toString());
                setResult(78, intent2);
                Oceny.super.onBackPressed();
            }
        });


    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        boolean[] checkedStates = new boolean[adapter.listaOcen.size()];

        for (int i = 0; i < adapter.listaOcen.size(); i++) {
            checkedStates[i] = adapter.listaOcen.get(i).isChecked();
        }

        outState.putBooleanArray("checkedStates", checkedStates);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        checkedStates = savedInstanceState.getBooleanArray("checkedStates");

        for (int i = 0; i < adapter.listaOcen.size(); i++) {
            adapter.listaOcen.get(i).setChecked(checkedStates[i]);
        }

        adapter.notifyDataSetChanged();
    }
}