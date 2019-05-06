package com.mxfit.mentix.menu3.HistoryPackage;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.mxfit.mentix.menu3.Utils.ColorLineMethods;
import com.mxfit.mentix.menu3.Utils.DBNameFormatter;
import com.mxfit.mentix.menu3.Utils.DatabaseHelper;
import com.mxfit.mentix.menu3.MainActivity;
import com.mxfit.mentix.menu3.R;
import com.mxfit.mentix.menu3.Utils.FireBaseConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mxfit.mentix.menu3.GlobalValues.*;
import static java.lang.Math.round;

public class HistoryActivity extends AppCompatActivity implements OnMapReadyCallback {


    String DBname;
    String ActionBarName;
    boolean saveToFireDB;
    DatabaseHelper myDb;
    List<LatLng> mapList;
    Double distance;
    int time;
    final MainActivity activity = MainActivity.instance;
    ColorLineMethods colorLineMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.activity_history);
        Bundle extras = getIntent().getExtras();
        DBname = extras.getString("tablename");
        ActionBarName = extras.getString("barname");
        saveToFireDB = extras.getBoolean("saveToFireDB");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);
        colorLineMethods = new ColorLineMethods();
        colorLineMethods.checkLinePrefs();

        TextView TimeText = (TextView) findViewById(R.id.his_time);
        TextView DistanceText = (TextView) findViewById(R.id.his_distance);
        TextView SpeedText = (TextView) findViewById(R.id.his_AVS);


        myDb = new DatabaseHelper(this);

        Cursor data = myDb.selectSpecificTable(DBname);

        mapList = new ArrayList<>();
        int i = 0;

        if(data.getCount() != 0)
        {
            data.moveToFirst();
            distance = data.getDouble(0);
            time = (int)data.getDouble(1);
            while(data.moveToNext()){
                    LatLng point = new LatLng(data.getDouble(0), data.getDouble(1));
                    mapList.add(i, point);
                    i++;
            }
        }
        if(saveToFireDB) {
            FireBaseConnection.db = FirebaseFirestore.getInstance();
            Map<String, Object> run = new HashMap<>();

            run.put("Distance", distance);
            run.put("Time", time);
            ArrayList<GeoPoint> pointList = new ArrayList<>();
            for(int p = 0; p<mapList.size(); p++)
            {
                LatLng point = mapList.get(p);
                pointList.add(new GeoPoint(point.latitude, point.longitude));
            }

            run.put("Points", pointList);

            FireBaseConnection.db.collection("users").document(FireBaseConnection.user.getUid())
                    .collection("runs").document(DBname).set(run)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            System.out.println("Sent!");
                        }
                    });
        }

        DBNameFormatter dbf = new DBNameFormatter();
        TimeText.setText(dbf.reformatTime(time));
        DistanceText.setText(activity.formatter.format(distance)+"m");
        SpeedText.setText((round((distance/time)*3.6 * 100.0)/100.0)+" Km/h");
        setupActionBar();
    }

    public void setTheme(){
        if(activity.themeNumber == 2)
            setTheme(R.style.AppTheme);
        else if(activity.themeNumber == 3)
            setTheme(R.style.DeusAge);
        else
            setTheme(R.style.DailyTheme);
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(ActionBarName);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        LatLng last = new LatLng(0,0);
        for (LatLng object: mapList) {
            PolylineOptions PolOps = new PolylineOptions().jointType(2).width(5);
            if (isMultiColorLine && isCustomLine) {
                colorLineMethods.setLineColor();
                PolOps.color(Color.rgb((int)tempColors[0], (int)tempColors[1], (int)tempColors[2]));
            } else if (isCustomLine){
                PolOps.color(LineColor1);
            } else {
                PolOps.color(Color.BLUE);
            }
            if(!last.equals(activity.zero))
            {
                googleMap.addPolyline(PolOps
                        .add(last, object));
            }
            last = object;
        }

        if(mapList.size()>0)
        {
            googleMap.addMarker(new MarkerOptions()
                    .position(mapList.get(0))
                    .title("Start")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapList.get(0), 10.5f));
            googleMap.addMarker(new MarkerOptions()
                    .position(mapList.get(mapList.size()-1))
                    .title("Finish")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
        }
    }
}
