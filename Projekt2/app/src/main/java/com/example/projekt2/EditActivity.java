package com.example.projekt2;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> activityLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);


        Button WebButton = findViewById(R.id.web);
        Button CancelButton = findViewById(R.id.cancel);
        EditText ManufacturerText = findViewById(R.id.manufacturer);
        EditText WebText = findViewById(R.id.webText);
        EditText ModelText = findViewById(R.id.model);
        EditText AndroidVersion = findViewById(R.id.version);
        Button UpdateButton = findViewById(R.id.update);
        EditText pom = findViewById(R.id.pomoc);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("id")) {
            String phoneId = intent.getStringExtra("id");
            String manufacturer = intent.getStringExtra("manufacturer");
            String model = intent.getStringExtra("model");
            String version = intent.getStringExtra("version");
            String web = intent.getStringExtra("web");
            ManufacturerText.setText(manufacturer);
            ModelText.setText(model);
            AndroidVersion.setText(version);
            WebText.setText(web);
            pom.setText(phoneId);
        } else {
            finish();
        }
        //mPhoneViewModel = new ViewModelProvider(this).get(PhoneViewModel.class);

        WebButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browser = new Intent("android.intent.action.VIEW", Uri.parse(WebText.getText().toString()));
                startActivity(browser);
            }
        });

        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        UpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText manufacturerText = findViewById(R.id.manufacturer);
                EditText modelText = findViewById(R.id.model);
                EditText androidVersionText = findViewById(R.id.version);
                EditText webText = findViewById(R.id.webText);

                boolean isValid = true;
                if (manufacturerText.getText().toString().trim().isEmpty()) {
                    manufacturerText.setError("Manufacturer name is required");
                    isValid = false;
                }
                if (modelText.getText().toString().trim().isEmpty()) {
                    modelText.setError("Model name is required");
                    isValid = false;
                }
                if (androidVersionText.getText().toString().trim().isEmpty()) {
                    androidVersionText.setError("Android version is required");
                    isValid = false;
                }
                if (webText.getText().toString().trim().isEmpty()) {
                    webText.setError("Web site is required");
                    isValid = false;
                }

                if (isValid) {
                    //savePhone(view);
                    Intent intent = new Intent();

                    intent.putExtra("id", pom.getText().toString());
                    intent.putExtra("Manufacturer", manufacturerText.getText().toString());
                    intent.putExtra("Model", modelText.getText().toString());
                    intent.putExtra("Android version", androidVersionText.getText().toString());
                    intent.putExtra("Web site", webText.getText().toString());
                    setResult(79, intent);
                    EditActivity.super.onBackPressed();
                }
            }
        });

    }

}