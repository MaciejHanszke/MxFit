package com.mxfit.mentix.menu3.RunningPackage;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.mxfit.mentix.menu3.Beans.PreRun;
import com.mxfit.mentix.menu3.R;
import com.mxfit.mentix.menu3.Utils.DatabasePremadesHelper;

import java.util.List;

public class PreRunChoiceActivity extends AppCompatActivity {

    DatabasePremadesHelper myDb;
    RecyclerView recyclerView;
    private List<PreRun> PreRunList;
    private Button buttonSwitch;
    boolean switched = false;
    ViewSwitcher switcher;
    EditText addRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_run);

        refreshTables();
        buttonSwitch = (Button) findViewById(R.id.buttonSwitcheroo);
        switcher = (ViewSwitcher) findViewById(R.id.my_switcher);
        addRun = (EditText) findViewById(R.id.hidden_edit_view);

        addRun.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    switchViews();
                    return true;
                }
                return false;
            }
        });


        buttonSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchViews();
                }
        });


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

    protected void refreshTables(){
        myDb = new DatabasePremadesHelper(this);
        PreRunList = myDb.getListPreRuns();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewPR);
        PreRunRecyclerViewAdapter adapter = new PreRunRecyclerViewAdapter(PreRunList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));
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

    public void switchViews()
    {
        if(!switched)
        {
            switcher.showNext();
            buttonSwitch.setText(">");
            switched = true;
        } else{
            String text =addRun.getText().toString();
            if(!text.isEmpty()) {
                Double data = Double.parseDouble(text);
                if(data <= 1000000) {
                    myDb.insertRunData(data);
                    refreshTables();
                } else {
                    Toast.makeText(this,"You can't go past 1000000 kilometers!",Toast.LENGTH_SHORT).show();
                }
            }
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            switcher.showPrevious();
            buttonSwitch.setText("+");
            switched = false;
        }
    }

}
