package com.mxfit.mentix.menu3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ViewSwitcher;

import java.util.List;

public class DayChoiceActivity extends AppCompatActivity {

    DatabasePremadesHelper myDb;
    RecyclerView recyclerView;
    ViewSwitcher switcher;
    EditText addRun;
    MainActivity activity = MainActivity.instance;
    boolean hasChosen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_choice);

        refreshTables();
        switcher = (ViewSwitcher) findViewById(R.id.my_switcher);
        addRun = (EditText) findViewById(R.id.hidden_edit_view);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Choose Training Day");
        }
    }

    protected void exit(){
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        if(hasChosen) {
            int days = activity.rday;
            int puTrLevel = 1;

            while (days > myDb.getHowManyDays(puTrLevel)) {
                days -= myDb.getHowManyDays(puTrLevel);
                puTrLevel++;
            }
            activity.day = days;
            activity.traininglevel = puTrLevel;
            activity.PUdateCount = 0;
        }
        finish();
    }

    protected void refreshTables(){
        myDb = new DatabasePremadesHelper(this);
        List<PreRun> preRunList = myDb.getListPreRuns();
        List<Pushups> pushupList = myDb.getListPushups(false);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewD);
        DayRecyclerViewAdapter adapter = new DayRecyclerViewAdapter(preRunList, pushupList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));
    }

    @Override
    public void onBackPressed() {

        exit();
    }

    public void setChosen(){
        hasChosen = true;
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
