package com.mxfit.mentix.menu3;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.grantland.widget.AutofitTextView;
import static android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM;
import static java.lang.Math.round;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                   OnMapReadyCallback,
                   LocationListener,
                    TrainingFragment.OnFragmentInteractionListener,
                    PushupsFragment.OnFragmentInteractionListener
{

    //90 do -90

    DatabaseHelper myDb;
    DatabasePremadesHelper runDb;
    Date date;
    DateFormat dateFormat;
    String dbTableName;

    private GoogleMap mMap;
    LocationManager locationManager;
    private Location mLastLocation;
    NavigationView navigationView;
    boolean exists = true;
    boolean existsUpd = true;

    String dist = "0.00";

    private boolean drawLine = false;
    private boolean mapVisibility = false;
    private boolean TrackCamera = true;
    private boolean isTrackerInitialized = false;
    protected boolean isInTraining = false;
    //wybral goal
    private boolean hasGoal = true;
    //ukonczyl goal
    private boolean hasFinishedGoal = false;

    LatLng last = new LatLng(0.0, 0.0);
    LatLng zero = new LatLng(0.0, 0.0);
    LatLng locations[] = new LatLng[20];
    int locationsArraySize = 0;
    private float distance = 0;
    private float goaldistance = 0;


    private TextView txtTime;
    private AutofitTextView txtDistance;
    private TextView txtMenuTime;
    private TextView txtMenuDistance;
    private TextView txtMenuAvgSpeed;
    private TextView txtMenuCurGoal;
    private ImageButton btnLocationUpdates;
    private Button btnTrackCamera;
    private FloatingActionButton btnMapVis;
    private ImageButton btnStop;
    String curTime;
    RelativeLayout mapVis;
    RelativeLayout fragVis;
    NumberFormat formatter;
    FragmentManager manager;
    long storeSeconds;
    int themeNumber;

    static MainActivity instance;


    double AVSspeed = 0.0;
    String currentTime = "0:00:00";
    Toolbar toolbar;

    final int MSG_START_TIMER = 0;
    final int MSG_STOP_TIMER = 1;
    final int MSG_UPDATE_TIMER = 2;
    final int MSG_PAUSE_TIMER = 3;

    StopWatch timer = new StopWatch();
    int REFRESH_RATE = 1000;
    SharedPreferences prefs;
    boolean AlarmEnabled;

    //trainings
    int day = 1;
    int traininglevel = 1;
    int rday = 1;
    float goal = 1;
    int pushupsDone = 0;
    String lastPushupDate;
    String lastRunDate;
    boolean Synchronised = false;
    SaveFileCalendar saveFileCalendar;
    int PUdateCount = 0;
    int RdateCount = 0;

    //Settings preferences
    int welcomeScreen = 0;
    int REFRESH = 2;

    //checkLinePrefs()
    boolean isCustomLine;
    int LineColor1;
    boolean isMultiColorLine;
    int LineColor2;
    boolean goingUp = true;
    int TempColor;
    int Step = 20;
    int ColorIterator;
    double stepColors[];
    double tempColors[];
    ColorLineMethods colorLineMethods;
    private boolean FirstZoom = true;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_START_TIMER:
                    timer.resume(); //start timer
                    mHandler.sendEmptyMessage(MSG_UPDATE_TIMER);
                    break;

                case MSG_UPDATE_TIMER:
                    currentTime = timer.toString();
                        curTime = currentTime;
                    txtTime.setText("" + curTime);
                    if (exists && findViewById(R.id.textView2) != null && existsUpd) {
                        reinitializeFragButtons();
                        existsUpd = false;
                    }
                    if (txtMenuTime != null) {
                        txtMenuTime.setText(curTime);
                    }
                        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIMER, REFRESH_RATE); //text view is updated every second,

                    break;                                  //though the timer is still running
                case MSG_STOP_TIMER:
                    mHandler.removeMessages(MSG_UPDATE_TIMER); // no more updates.
                    timer.stop();//stop timer
                    currentTime = timer.toString();
                    txtTime.setText("" + currentTime);
                    if (txtMenuTime != null) {
                        txtMenuTime.setText(currentTime);
                    }
                    break;
                case MSG_PAUSE_TIMER:
                    mHandler.removeMessages(MSG_UPDATE_TIMER); // no more updates.
                    timer.pause();
                    break;

                default:
                    break;
            }
        }
    };

    private void reinitializeFragButtons() {
        txtMenuTime = fragment.getView().findViewById(R.id.textView2);
        txtMenuAvgSpeed = fragment.getView().findViewById(R.id.textView13);
        txtMenuDistance = fragment.getView().findViewById(R.id.textView12);
        txtMenuCurGoal = fragment.getView().findViewById(R.id.GoalInt);
        TextView txtMenuFinGoal = fragment.getView().findViewById(R.id.GoalFinal);

        setTxtMenuAvgSpeed();
        setTxtMenuDistance();
        txtMenuFinGoal.setText(""+formatter.format(goal));

    }

    public void saveTrainingData() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("Pushups_day", day);
        editor.putInt("Training_level", traininglevel);
        editor.putInt("Pushups_number", pushupsDone);
        editor.putInt("Pushups_break_number", PUdateCount);
        editor.putString("Date_pushup", lastPushupDate);

        editor.putInt("Running_day", rday);
        editor.putFloat("Running_goal", goal);
        editor.putInt("Running_break_number", RdateCount);
        editor.putString("Date_running", lastRunDate);

        editor.putBoolean("Synchronise_Pushups_Running", Synchronised);
        editor.apply();

    }

    public void loadTrainingData(){
        day = prefs.getInt("Pushups_day",1);
        traininglevel = prefs.getInt("Training_level",1);
        pushupsDone = prefs.getInt("Pushups_number",0);
        PUdateCount = prefs.getInt("Pushups_break_number",0);
        lastPushupDate = prefs.getString("Date_pushup", null);

        rday = prefs.getInt("Running_day",1);
        goal = prefs.getFloat("Running_goal", (float) 1.0);
        RdateCount = prefs.getInt("Running_break_number",0);
        lastRunDate = prefs.getString("Date_running", null);

        Synchronised = prefs.getBoolean("Synchronise_Pushups_Running",false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 7171:
                if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    AlertDialog dialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(R.string.Permissions_warning)
                            .setTitle(R.string.Permissions_title);
                    builder.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                    dialog = builder.create();
                    dialog.show();
                }
                break;
        }
    }

    public void showHome(){
        switch (welcomeScreen) {
            case 0:
                showTraining();
                break;
            case 1:
                showRunning();
                break;
            case 2:
                showPushups();
                break;
        }

        if(welcomeScreen != 1)
        {
            hideMainLayout();
        }

        existsUpd = true;
        navigationView.getMenu().getItem(welcomeScreen).setChecked(true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        instance = this;
        Intent myIntent = new Intent(getBaseContext(), ClosingService.class);
        startService(myIntent);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.mxfitlaunchersplash);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        mapVis = (RelativeLayout) findViewById(R.id.mapsLayout);
        fragVis = (RelativeLayout) findViewById(R.id.mainLayout);
        btnMapVis = (FloatingActionButton) findViewById(R.id.mapVisBut);

        txtTime = (TextView) findViewById(R.id.txtTimer);
        txtDistance = (AutofitTextView) findViewById(R.id.txtDistance);
        btnLocationUpdates = (ImageButton) findViewById(R.id.btnTrackLocation);
        btnTrackCamera = (Button) findViewById(R.id.btnMoveCamera);
        btnStop = (ImageButton) findViewById(R.id.StopButton);
        btnStop.setEnabled(false);
        formatter = new DecimalFormat("#0.00");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();




        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        hideMainLayout();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        loadTrainingData();

        saveFileCalendar = new SaveFileCalendar();
        if(!Synchronised) {
            saveFileCalendar.checkPushupsDate();
            saveFileCalendar.checkRunningDate();
        } else {
            saveFileCalendar.checkDayDate();
        }

        loadPreferences();
        showHome();
        setName();


        btnLocationUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleDrawLine();
            }
        });

        btnTrackCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleTrack();
            }
        });

        btnMapVis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleMap();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInTraining) {
                    showTrainingEndAlert();
                }
            }
        });


    }



    protected void setName(){
        String name = prefs.getString("display_name","");
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.nickName);
        TextView navMsg = (TextView) headerView.findViewById(R.id.welcomemsg);
        navUsername.setText(name);
        if(!name.equals("")) {
            navMsg.setText(getResources().getString(R.string.welcome_back) + ",");
        } else {
            navMsg.setText(R.string.welcome_back);
        }
    }

    private void initializeLocationManager() {
        if (!isTrackerInitialized) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, REFRESH*1000, REFRESH * 2, this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            isTrackerInitialized = true;
        }
    }

    private void deinitializeLocationManager() {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
            locationManager = null;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        mMap.setMyLocationEnabled(false);

        last = zero;
        isTrackerInitialized = false;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.GPS_name);
        alertDialog.setMessage(R.string.GPS_warning);
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public void showTrainingEndAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.Pushups_title);
        alertDialog.setMessage(R.string.EndTraining_warning);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                saveDataOnStop(true);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public void saveDataOnStop(Boolean resetView){
        if(drawLine){
            storeSeconds = timer.getRunningTime();
        }
        mHandler.sendEmptyMessage(MSG_STOP_TIMER);
        saveDBonStop(locationsArraySize-1);
        if((goaldistance/1000)>=goal) {
            runDb = new DatabasePremadesHelper(getBaseContext());
            if (rday < runDb.getHowManyRuns()) {
                runDb.setRunFinished(rday);
                rday++;
                goal = runDb.getDayRun(rday);
            }
            if(!Synchronised)
            {
                new SaveFileCalendar().setRunningDate();
            } else {
                new SaveFileCalendar().setDayDate(false);
            }

            resetAlarm();

            goaldistance = 0;
        }

        saveTrainingData();

        if(resetView) {
        AVSspeed = 0.0;
        distance = 0;
        if (drawLine) {
            toggleDrawLine();
        }
        isInTraining = false;
        mMap.clear();
        reinitializeFragButtons();
        txtDistance.setText(formatter.format(distance) + " m");
        new ShowMap(instance, dbTableName.substring(2));
        dbTableName = null;
        if(goaldistance/1000<=goal) {
            hasFinishedGoal = false;
        }
        btnStop.setEnabled(false);
        }
    }

    public void addDBLocation(LatLng latLng, int Size)
    {
        if(locationsArraySize < Size) {
            locations[locationsArraySize] = latLng;
            locationsArraySize++;
        } else if (locationsArraySize == Size)
        {
            locations[locationsArraySize] = latLng;
            saveDBonStop(Size);
        }
    }

    public void saveDBonStop(int Size){
        if(myDb!= null) {
            if(drawLine){
                storeSeconds = timer.getRunningTime();
            }
            myDb.insertMultipleData(locations, Size, distance, storeSeconds);
            locationsArraySize = 0;
            for (int i = 0; i <= Size; i++) {
                locations[i] = zero;
            }
        }
    }


    private void toggleDrawLine() {
        if (!drawLine) {
            if (!checkPermissions()) {
                return;
            }
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showSettingsAlert();
                return;
            }
            initializeLocationManager();

            if (!isInTraining) {
                date = new Date();
                dateFormat = new SimpleDateFormat("yyyyśMMśdd_HHźmmźss");
                dbTableName = "t_" + dateFormat.format(date);
                myDb = new DatabaseHelper(this, dbTableName);
                btnStop.setEnabled(true);
                isInTraining = true;
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
            colorLineMethods = new ColorLineMethods();
            colorLineMethods.checkLinePrefs();
            btnLocationUpdates.setImageResource(R.drawable.ic_pause);
            drawLine = true;
            mHandler.sendEmptyMessage(MSG_START_TIMER);
            reinitializeFragButtons();
        } else {
            pauseTracking();
        }
    }



    public void pauseTracking() {
        btnLocationUpdates.setImageResource(R.drawable.ic_play);
        drawLine = false;
        storeSeconds = timer.getRunningTime();
        mHandler.sendEmptyMessage(MSG_PAUSE_TIMER);
    }

    public boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                    }, 7171);

            return false;
        } else {
            return true;
        }
    }

    private void toggleMap() {
        initializeLocationManager();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        if (!mapVisibility) {
            mapVis.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            btnMapVis.setImageResource(R.drawable.ic_showmenu);
            btnMapVis.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(getResources(),android.R.color.white,null)));
            if (Build.VERSION.SDK_INT >= 21) {
                int cx = (btnMapVis.getRight()+btnMapVis.getLeft())/2;
                int cy = (btnMapVis.getTop()+btnMapVis.getBottom())/2;
                btnMapVis.setEnabled(false);
                float finalRadius = (float) Math.hypot(fragVis.getWidth(), fragVis.getHeight());

                Animator anim =
                        ViewAnimationUtils.createCircularReveal(fragVis, cx, cy, finalRadius, 0);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        fragVis.setVisibility(View.INVISIBLE);
                        fragVis.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));
                        btnMapVis.setEnabled(true);
                    }
                });

                mapVis.setVisibility(View.VISIBLE);
                anim.start();
            } else {
                fragVis.setVisibility(View.INVISIBLE);
                fragVis.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));
                mapVis.setVisibility(View.VISIBLE);
            }
            mapVisibility = true;
        } else {
            fragVis.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            btnMapVis.setImageResource(R.drawable.ic_map);
            btnMapVis.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(getResources(),R.color.colorPrimary,null)));
            if (Build.VERSION.SDK_INT >= 21) {
                int cx = (btnMapVis.getRight()+btnMapVis.getLeft())/2;
                int cy = (btnMapVis.getTop()+btnMapVis.getBottom())/2;
                btnMapVis.setEnabled(false);
                float finalRadius = (float) Math.hypot(mapVis.getWidth(), mapVis.getHeight());

                Animator anim =
                        ViewAnimationUtils.createCircularReveal(fragVis, cx, cy, 0, finalRadius);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mapVis.setVisibility(View.INVISIBLE);
                        mapVis.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));
                        btnMapVis.setEnabled(true);
                    }
                });

                fragVis.setVisibility(View.VISIBLE);
                anim.start();
            } else {
                mapVis.setVisibility(View.INVISIBLE);
                mapVis.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));
                fragVis.setVisibility(View.VISIBLE);
            }
            mapVisibility = false;
            reinitializeFragButtons();
            setTxtMenuAvgSpeed();
            setTxtMenuDistance();
            txtMenuTime.setText(currentTime);
        }
    }

    private void setTxtMenuAvgSpeed() {
        if (txtMenuAvgSpeed != null) {
            txtMenuAvgSpeed.setText(""+AVSspeed);
        }
    }

    private void setTxtMenuDistance() {
        if (txtMenuDistance != null) {
            double MenuDist = distance/1000.0;
            dist = formatter.format(MenuDist);
            txtMenuDistance.setText(dist);
            if(hasGoal)
            {
                double CurGoalDist = goaldistance/1000;
                txtMenuCurGoal.setText(formatter.format(CurGoalDist));
                if(CurGoalDist>=goal)
                {
                    hasFinishedGoal = true;
                    txtMenuCurGoal.setTextColor(Color.GREEN);
                } else {
                    txtMenuCurGoal.setTextColor(Color.RED);
                }
            }

        }
    }

    private void toggleTrack() {
        if (!TrackCamera) {
            DrawableCompat.setTint(btnTrackCamera.getBackground(), ContextCompat.getColor(this, R.color.colorPrimary));
            TrackCamera = true;
        } else {
            DrawableCompat.setTint(btnTrackCamera.getBackground(), ContextCompat.getColor(this, android.R.color.black));
            TrackCamera = false;
        }
    }

    private void exitPrompt() {
        if (isInTraining) {
            AlertDialog dialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.Pushups_warning)
                    .setTitle(R.string.Pushups_title);
            builder.setNeutralButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    deinitializeLocationManager();
                    saveDataOnStop(false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        finishAndRemoveTask();
                    } else {
                        finishAffinity();
                    }
                }
            });
            builder.setNegativeButton("Minimize", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    moveTaskToBack(true);
                }
            });
            builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    return;
                }
            });
            dialog = builder.create();
            dialog.show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (welcomeScreen == 0 && fragment instanceof TrainingFragment) {
                exitPrompt();
            } else if (welcomeScreen == 1 && fragment instanceof MapsFragment) {
                exitPrompt();
            } else if (welcomeScreen == 2 && fragment instanceof PushupsFragment) {
                exitPrompt();
            } else {
                showHome();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }


    public void loadPreferences()
    {
        setTheme();
        AlarmEnabled = prefs.getBoolean("switch_notifications_enable",false);
        if(AlarmEnabled)
            setTrainingNotification(prefs.getInt("Hour",12),prefs.getInt("Minute",0));
        welcomeScreen = Integer.parseInt(prefs.getString("example_list", "0"));
        REFRESH = Integer.parseInt(prefs.getString("sync_frequency", "2"));
        if(prefs.getBoolean("pref_theme_color", false))
        {
            if(prefs.getBoolean("switch_bar_color", false))
                toolbar.setBackgroundColor(prefs.getInt("color1", 0));
            else
                {
                    if(themeNumber != 3)
                        toolbar.setBackgroundColor(getResources().getColor(R.color.barColor));
                    else
                        toolbar.setBackgroundColor(getResources().getColor(R.color.Gold));
                }

            if(prefs.getBoolean("switch_background_color", false)) {
                if (prefs.getBoolean("switch_gradient_color", false)) {
                    int Color1 = prefs.getInt("color2", 0), Color2 = prefs.getInt("color3", 0);
                    int[] colors = new int[]{Color1, Color2};
                    GradientDrawable gd = new GradientDrawable(TOP_BOTTOM, colors);
                    fragVis.setBackground(gd);
                } else
                fragVis.setBackgroundColor(prefs.getInt("color2", 0));
            }
            else
                fragVis.setBackground(ContextCompat.getDrawable(this, R.drawable.pushupgui));
        } else {
            if(themeNumber != 3)
                toolbar.setBackgroundColor(getResources().getColor(R.color.barColor));
            else
                toolbar.setBackgroundColor(getResources().getColor(R.color.Gold));
            fragVis.setBackground(ContextCompat.getDrawable(this, R.drawable.pushupgui));
        }
    }

    public void setTheme(){
        themeNumber = Integer.parseInt(prefs.getString("theme_number", "1"));
        if(themeNumber == 1)
            setTheme(R.style.DailyTheme);
        else if (themeNumber == 2)
            setTheme(R.style.AppTheme);
        else if (themeNumber == 3)
            setTheme(R.style.DeusAge);
        else if (themeNumber == 4)
            setTheme(R.style.MinimalisticTheme);
    }

    protected void showRunning() {
        if (!(fragment instanceof MapsFragment)) {
            fragment = MapsFragment.newInstance(dist,AVSspeed,formatter.format(goaldistance/1000),goal,hasFinishedGoal,currentTime);
        }

        if (fragment != null) {
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.mainLayout, fragment, fragment.getTag()).commit();
        }

        exists = true;
        btnMapVis.setVisibility(View.VISIBLE);
        btnLocationUpdates.setVisibility(View.VISIBLE);
        btnStop.setVisibility(View.VISIBLE);
        initializeLocationManager();
        if (!mapVisibility) {
            mapVis.setVisibility(View.INVISIBLE);
            fragVis.setVisibility(View.VISIBLE);
            mapVis.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));
            fragVis.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        } else {
            mapVis.setVisibility(View.VISIBLE);
            fragVis.setVisibility(View.INVISIBLE);
            mapVis.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            fragVis.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));
        }


    }

    private void showTraining() {
        if (!(fragment instanceof TrainingFragment)) {
            fragment = TrainingFragment.newInstance(day,traininglevel,goal);
        }

        if (fragment != null) {
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.mainLayout, fragment, fragment.getTag()).commit();
        }

    }

    protected void showPushups() {
        if (!(fragment instanceof PushupsFragment)) {
            fragment = PushupsFragment.newInstance(day,traininglevel);
        }

        if (fragment != null) {
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.mainLayout,
                                               fragment,
                                               fragment.getTag()).commit();
        }

    }

    private void showHistory() {
        if (!(fragment instanceof HistoryFragment)) {
            fragment = new HistoryFragment();
        }

        if (fragment != null) {
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.mainLayout, fragment, fragment.getTag()).commit();
        }

    }

    Fragment fragment = null;

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_trainings)     showTraining();
        else if (id == R.id.nav_running)  showRunning();
        else if (id == R.id.nav_pushups)  showPushups();
        else if (id == R.id.nav_history)  showHistory();


        if(id != R.id.nav_running)
        {
            if(!isInTraining && id != R.id.nav_settings)
            {
                deinitializeLocationManager();
            }
            existsUpd = true;
            exists = false;
            if(id == R.id.nav_exit)
            {
                exitPrompt();
            } else if (id == R.id.nav_settings) {
                Intent settings = new Intent(this, SettingsActivity.class);
                startActivity(settings);
            }
            else
            {
                hideMainLayout();
            }


        }

        if(fragment != null){
            manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.mainLayout, fragment, fragment.getTag()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void hideMainLayout()
    {
        mapVis.setVisibility(View.INVISIBLE);
        fragVis.setVisibility(View.VISIBLE);
        mapVis.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));
        fragVis.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        btnMapVis.setVisibility(View.INVISIBLE);
        btnStop.setVisibility(View.INVISIBLE);
        btnLocationUpdates.setVisibility(View.INVISIBLE);

    }

    public void resetAlarm(){
        if(AlarmEnabled) {
            cancelAlarm();
            setTrainingNotification(prefs.getInt("Hour", 12), prefs.getInt("Minute", 0));
        }
    }

    public void setTrainingNotification(int hour, int minute){

        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.setAction(NotificationReceiver.ACTION_ALARM_RECEIVER);
        boolean isWorking = (PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_NO_CREATE) != null);
        Date closestDate = saveFileCalendar.WhichDateFirst(lastPushupDate,lastRunDate);

        if(!isWorking && closestDate!=null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(closestDate);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);


            startAlarm(calendar);
            Toast.makeText(this, "Reminder set", Toast.LENGTH_SHORT).show();
        }
    }

    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.setAction(NotificationReceiver.ACTION_ALARM_RECEIVER);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

        ComponentName receiver = new ComponentName(this, NotificationReceiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    protected void cancelAlarm(){
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.setAction(NotificationReceiver.ACTION_ALARM_RECEIVER);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();

        ComponentName receiver = new ComponentName(this, NotificationReceiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng sydney = new LatLng(location.getLatitude (), location.getLongitude());

        if(TrackCamera)
        {
            float zoom = mMap.getCameraPosition().zoom;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoom));
        }

        if(FirstZoom) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 9.0f));
            FirstZoom = false;
        }

        if (drawLine && !last.equals(zero)) {
            PolylineOptions PolOps = new PolylineOptions().jointType(2).width(5);
            if (isMultiColorLine && isCustomLine) {
                colorLineMethods.setLineColor();
                PolOps.color(Color.rgb((int)tempColors[0], (int)tempColors[1], (int)tempColors[2]));
            } else if (isCustomLine){
                PolOps.color(LineColor1);
            } else {
                PolOps.color(Color.BLUE);
            }
            mMap.addPolyline(PolOps.add(last, sydney));
            addDBLocation(sydney,5);
            distance += location.distanceTo(mLastLocation);
            if(hasGoal)
            {
                goaldistance += location.distanceTo(mLastLocation);
            }
            setTxtMenuDistance();
            if(distance < 1000000)
                txtDistance.setText(formatter.format(distance)+ " m");
            else
                txtDistance.setText(formatter.format(distance/1000)+ " km");
            if(distance>0)
            {
                AVSspeed = (round((distance/timer.getRunningTime())*3.6 * 100.0)/100.0);
                setTxtMenuAvgSpeed();
            }

        }

        last = sydney;
        mLastLocation = location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        if(drawLine)
        {
            showSettingsAlert();
            pauseTracking();
        }
    }

    @Override
    protected void onDestroy() {
        deinitializeLocationManager();
        saveTrainingData();
        super.onDestroy();
    }

    //trainingfragment
    @Override
    public void onFragmentInteraction(int pday, int TLevel, float pgoal) {
        day = pday;
        traininglevel = TLevel;
        goal = pgoal;
    }

    //pushupsfragment
    @Override
    public void onFragmentInteraction(int pday, int tlevel) {
        day = pday;
        traininglevel = tlevel;
    }
}
