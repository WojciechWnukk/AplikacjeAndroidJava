package com.example.projekt2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private PhoneViewModel mPhoneViewModel;
    private List<Phone> phones = new ArrayList<>();

    ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.d(TAG, "onActivityResult: ");

                    if(result.getResultCode()==78){
                        Intent intent = result.getData();
                        Log.d(TAG, "onActivityResult with RESULT_OK");
                        if(intent != null){
                            String manufacturer = intent.getStringExtra("Manufacturer");
                            String model = intent.getStringExtra("Model");
                            String version = intent.getStringExtra("Android version");
                            String web = intent.getStringExtra("Web site");
                            Phone phone = new Phone(model, manufacturer, model, version, web);
                            mPhoneViewModel.insert(phone);
                        }
                    }
                    if(result.getResultCode()==79) {
                        Intent intent = result.getData();
                        Log.d("ELO", "UPDATE przechodzi");
                        if(intent != null){
                            String id = intent.getStringExtra("id");
                            String manufacturer = intent.getStringExtra("Manufacturer");
                            String model = intent.getStringExtra("Model");
                            String version = intent.getStringExtra("Android version");
                            String web = intent.getStringExtra("Web site");
                            Log.d("ELO", "UPDATE przechodzi" + manufacturer + model + version);
                            long idlong = Long.parseLong(id);
                            Phone phone = new Phone(idlong, model, manufacturer, model, version, web);
                            mPhoneViewModel.updatePhone(phone);
                            Log.d("ELO", "iddd" +idlong);
                        }
                    }
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton insertButton = findViewById(R.id.insert);
        mPhoneViewModel = new ViewModelProvider(this).get(PhoneViewModel.class);
        insertButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, InsertActivity.class);
                activityLauncher.launch(intent);
            }
        });



        mPhoneViewModel = new ViewModelProvider(this).get(PhoneViewModel.class);
        ListAdapter adapter = new ListAdapter(this, phones);
        mPhoneViewModel.getAllPhones().observe(this, new Observer<List<Phone>>() {
            @Override
            public void onChanged(List<Phone> phones) {
                adapter.submitList(phones);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Phone phone) {
                MainActivity.this.onItemClick(phone);
            }
        });




        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Phone phoneToDelete = adapter.getCurrentList().get(position);
                mPhoneViewModel.deletePhone((int) phoneToDelete.getId());
                //adapter.notifyItemRemoved(position);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.delete){
            mPhoneViewModel.deleteAll();
            //mPhoneViewModel.getAllPhones();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onItemClick(Phone phone) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("id", Long.toString(phone.getId()));
        //Log.d("ELO", "" + phone.getId());
        intent.putExtra("manufacturer", phone.getPhoneProducer());
        intent.putExtra("model", phone.getPhoneModel());
        intent.putExtra("version", phone.getAndroidVersion());
        intent.putExtra("web", phone.getPhonePage());
        activityLauncher.launch(intent);
    }


}