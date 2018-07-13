package com.mxfit.mentix.menu3;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.List;

public class PushupsChoiceActivity extends AppCompatActivity {

    DatabasePremadesHelper myDb;
    RecyclerView recyclerView;
    private List<Pushups> PushupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pushups_choice);
        myDb = new DatabasePremadesHelper(this);


        PushupList = myDb.getListPushups(true);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewPU);
        PushupRecyclerViewAdapter adapter = new PushupRecyclerViewAdapter(PushupList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Choose Training Day");
        }
    }


    protected void exit(){
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
    }

    @Override
    public void onBackPressed() {

        exit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            exit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
