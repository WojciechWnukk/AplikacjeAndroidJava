package com.example.lab1;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button button;
    private Button button2;
    private EditText nameEditText;
    private EditText surnameEditText;
    private EditText numberEditText;
    private TextView averageText;

    ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.d(TAG, "onActivityResult: ");

                    if(result.getResultCode()==78){
                        Intent intent = result.getData();
                        if(intent != null){
                            String data = intent.getStringExtra("SREDNIA");
                            averageText = findViewById(R.id.averageText);
                            button2 = findViewById(R.id.button2);
                            button = findViewById(R.id.button);
                            averageText.setText("Twoja średnia to: " + data);
                            averageText.setVisibility(View.VISIBLE);
                            button.setVisibility(View.INVISIBLE);
                            button2.setVisibility(View.VISIBLE);
                            Double dataInt = Double.parseDouble(data);
                            if(dataInt>=3.0){
                                button2.setText("SUPER :)");
                            }
                            else
                            {
                                button2.setText("Tym razem mi nie poszło");
                            }
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        nameEditText = findViewById(R.id.name);
        surnameEditText = findViewById(R.id.surname);
        numberEditText = findViewById(R.id.number);
        nameEditText.addTextChangedListener(nameTextWatcher);
        surnameEditText.addTextChangedListener(surnameTextWatcher);
        numberEditText.addTextChangedListener(numberTextWatcher);
        button.setVisibility(View.INVISIBLE);
        //averageText.setVisibility(View.INVISIBLE);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Oceny.class);
                String numberString = numberEditText.getText().toString();
                int numberInt = Integer.parseInt(numberString);
                intent.putExtra("KLUCZ", numberInt);
                activityLauncher.launch(intent);

            }
        });

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(button2.getText()=="SUPER :)"){
                    finish();
                    Toast.makeText(getApplicationContext(), "Gratulacje", Toast.LENGTH_SHORT).show();
                }
                else{
                    finish();
                    Toast.makeText(getApplicationContext(), "Lipa fest", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    private boolean isNameValid(String name) {
        boolean isValid = !TextUtils.isEmpty(name);
        if (!isValid) {
            nameEditText.setError("Pole imienia nie może być puste.");
        } else {
            nameEditText.setError(null);
        }
        return isValid;
    }
    private boolean isSurnameValid(String surname) {
        boolean isValid = !TextUtils.isEmpty(surname);
        if (!isValid) {
            surnameEditText.setError("Pole nazwiska nie może być puste.");
        } else {
            surnameEditText.setError(null);
        }
        return isValid;
    }
    private boolean isNumberValid(String number) {
        try {
            int value = Integer.parseInt(number);
            return value >= 5 && value <= 15;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    TextWatcher nameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // sprawdź, czy dane w polu imienia są poprawne
            boolean isNameValid = isNameValid(s.toString());

            // włącz lub wyłącz przycisk na podstawie wyniku
            button.setVisibility(isNameValid && isNumberValid(numberEditText.getText().toString()) && isSurnameValid(surnameEditText.getText().toString()) ? View.VISIBLE : View.INVISIBLE);

        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher surnameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // sprawdź, czy dane w polu imienia są poprawne
            boolean isSurnameValid = isSurnameValid(s.toString());

            // włącz lub wyłącz przycisk na podstawie wyniku
            button.setVisibility(isNameValid(nameEditText.getText().toString()) && isNumberValid(numberEditText.getText().toString())  && isSurnameValid ? View.VISIBLE : View.INVISIBLE);

        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher numberTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            boolean isNumberValid = isNumberValid(s.toString());

            if (!isNumberValid) {
                Toast.makeText(getApplicationContext(), "Wartość pola liczby musi być z przedziału od 5 do 15.", Toast.LENGTH_SHORT).show();
                numberEditText.setError("Nieprawidłowa wartość liczby");
            } else {
                numberEditText.setError(null);
            }

            button.setVisibility(isNumberValid && isNameValid(nameEditText.getText().toString()) && isSurnameValid(surnameEditText.getText().toString()) ? View.VISIBLE : View.INVISIBLE);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };



    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("nameText", nameEditText.getText().toString());
        outState.putString("surnameText", surnameEditText.getText().toString());
        outState.putString("numberText", numberEditText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        nameEditText.setText(savedInstanceState.getString("nameText"));
        surnameEditText.setText(savedInstanceState.getString("surnameText"));
        numberEditText.setText(savedInstanceState.getString("numberText"));
    }




}