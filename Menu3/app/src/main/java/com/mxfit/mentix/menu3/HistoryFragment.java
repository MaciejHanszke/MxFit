package com.mxfit.mentix.menu3;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseHelper myDb;
    View globalview;
    HistoryFragment instance;
    MainActivity activity = MainActivity.instance;


    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        globalview = view;
        instance = this;
        showTables();

    }

    public void showTables()
    {
        myDb = new DatabaseHelper(getContext());
        recyclerView = (RecyclerView) globalview.findViewById(R.id.recyclerView);
        DBNameFormatter dbf = new DBNameFormatter();

        Cursor data = myDb.getListContents();
        Cursor subdata;

        List<Run> runList = new ArrayList<>();
        int i = 0;
        if(data.getCount() != 0)
        {
            while(data.moveToNext()){
                subdata = myDb.queryPass(data.getString(0));
                if( subdata != null && subdata.moveToFirst()) {
                    Run run = new Run(dbf.reformatName(data.getString(0),true), activity.formatter.format(subdata.getDouble(0)/1000)+"km", dbf.reformatTime(subdata.getInt(1)));
                    runList.add(i, run);
                    i++;
                }
            }
        }
        HistoryRecyclerViewAdapter adapter = new HistoryRecyclerViewAdapter(runList, myDb, instance);
        adapter.resetFound();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager((new LinearLayoutManager(getContext())));
    }

}
