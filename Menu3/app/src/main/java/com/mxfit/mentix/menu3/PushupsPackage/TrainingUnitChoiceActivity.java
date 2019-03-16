package com.mxfit.mentix.menu3.PushupsPackage;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.mxfit.mentix.menu3.Beans.TrainingUnit;
import com.mxfit.mentix.menu3.R;
import com.mxfit.mentix.menu3.Utils.DatabasePremadesHelper;

import java.util.List;

public class TrainingUnitChoiceActivity extends AppCompatActivity {

    DatabasePremadesHelper myDb;
    RecyclerView recyclerView;
    private List<TrainingUnit> trainingUnitList;
    String RVlastDate;
    int RVday,
        RVtrainingLevel,
        RVdateCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_unit_choice);
        myDb = new DatabasePremadesHelper(this);


        Bundle extras = getIntent().getExtras();
        String type = extras.getString("type");
        String trainingType = extras.getString("trainingType");

        trainingUnitList = myDb.getTrainingUnitList(true, type, trainingType);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewPU);

        TrainingUnitRecyclerViewAdapter adapter = new TrainingUnitRecyclerViewAdapter(trainingUnitList, this, trainingType);

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
