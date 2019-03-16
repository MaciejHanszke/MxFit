package com.mxfit.mentix.menu3.SitupsPackage;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mxfit.mentix.menu3.GlobalValues;
import com.mxfit.mentix.menu3.R;

import java.util.ArrayList;

public class SitupsActivity extends AppCompatActivity implements SensorEventListener {

    private Button SitupBigButton;
    private Button FinishButton;
    private Button PauseButton;
    private LinearLayout LinLay;
    AlertDialog dialog;
    TextView [] textView;
    int day;
    int trainingLevel;
    int next = 1;
    int current = -1;
    int cycles;
    int allCycles;
    int currentSitups;
    int everySitup = 0;
    boolean clickable = true;
    boolean pausePressed = false;
    private CountDownTimer timer;
    SitupsActivity situpsActivity = this;

    private SensorManager mSensorManager;
    private Sensor mRotationSensor;

    private static final int SENSOR_DELAY = 500 * 1000; // 500ms
    private static final int FROM_RADS_TO_DEGS = -57;
    private final int acceptableThres = 10;

    Float val1, val2;
    float pitch;

    boolean up = true;
    Vibrator v;

    ArrayList<Integer> array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();

        day = extras.getInt("DAY");
        trainingLevel = extras.getInt("TrainingLevel");
        array = extras.getIntegerArrayList("Array");

        try {
            mSensorManager = (SensorManager) getSystemService(Activity.SENSOR_SERVICE);
            mRotationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            mSensorManager.registerListener(this, mRotationSensor, SENSOR_DELAY);
        } catch (Exception e) {
            Toast.makeText(this, "Hardware compatibility issue", Toast.LENGTH_LONG).show();
            finish();
        }

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_situps);
        SitupBigButton = (Button) findViewById(R.id.button7);
        FinishButton = (Button) findViewById(R.id.button8);
        PauseButton = (Button) findViewById(R.id.button9);
        SitupBigButton.setAllCaps(false);
        System.out.println();
        for(int k = 0; k < array.size(); k++)
        {
            if(array.get(k) > 0) {
                cycles++;
            }
        }
        allCycles = (cycles*2)-1;
        LinLay = (LinearLayout)findViewById(R.id.cycle);
        LinLay.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        int j = 0;



        textView = new TextView[allCycles];

        for( int h = 0; h < allCycles; h++ )
        {
            textView[h] = new TextView(this);
            if(h%2==0)
            {
                textView[h].setText(""+array.get(j));
                j++;
            } else {
                textView[h].setText("■");
            }
            textView[h].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
            textView[h].setGravity(Gravity.CENTER);
            LinLay.addView(textView[h]);
        }
        currentSitups = array.get(0);
        setSitups();
        setCurrent();

        val1 = GlobalValues.situpsCalibrationHigh;
        val2 = GlobalValues.situpsCalibrationLow;
        if(val1 == null || val2 == null)
            calibrate();

        FinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit();
            }
        });

        PauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timer != null && !pausePressed)
                {

                    pausePressed = true;
                    timer.cancel();
                    timer = null;
                    createTimer(5,true);
                    timer.start();
                }
            }
        });


        //dialogs
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.Pushups_warning)
                .setTitle(R.string.Pushups_title);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                GlobalValues.situpsDone+= everySitup;
                mSensorManager.unregisterListener(situpsActivity, mRotationSensor);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        });
        dialog = builder.create();
    }

    public String reformatTime(int seconds)
    {
        int minutes;

        minutes = seconds /60;
        seconds = seconds %60;
        minutes = minutes%60;

        String time = minutes+":";

        if(seconds<10)
            time += "0"+seconds;
        else
            time += seconds;
        return time;
    }


    private void setSitups(){
        if(!clickable)
        {setCurrent();}
        clickable = true;
        Spannable spannable = new SpannableString(currentSitups + "\n Sit-ups");
        int length = String.valueOf(currentSitups).length();
        spannable.setSpan(new RelativeSizeSpan(6.5f), 0, length,Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        SitupBigButton.setText(spannable);
    }

    private void setCurrent(){
        current++;
        if(current<(cycles*2-1)) {
            textView[current].setBackgroundColor(Color.GREEN);
            if (current > 0)
                textView[current - 1].setBackgroundColor(Color.TRANSPARENT);
        }

    }

    private void createTimer(int Seconds, boolean isBreakTimer){

        if(isBreakTimer) {
            timer = new CountDownTimer(Seconds * 1000 + 999, 100) {
                @Override
                public void onTick(long l) {
                    String reformattedTime = reformatTime((int) l / 1000);
                    Spannable spannable = new SpannableString("Break Time:\n" + reformattedTime);
                    spannable.setSpan(new RelativeSizeSpan(6.5f), 12, 12 + reformattedTime.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    SitupBigButton.setText(spannable);
                }


                @Override
                public void onFinish() {
                    setSitups();
                    timer = null;
                }
            };
        } else if (!isBreakTimer) {
            timer = new CountDownTimer(Seconds * 1000+999, 100) {
                @Override
                public void onTick(long l) {
                    String reformattedTime = reformatTime((int) l / 1000);
                    Spannable spannable = new SpannableString("Situps finished. \nReturning to menu in:" + reformattedTime);
                    SitupBigButton.setText(spannable);
                    }


                @Override
                public void onFinish() {
                    exit();
                    timer = null;
                }
            };
        }
    }

    private void exit(){
        if(currentSitups == 0)
        {
            Intent intent = new Intent();
            intent.putExtra("haveFinished", "1");
            setResult(RESULT_OK, intent);
            GlobalValues.situpsDone+= everySitup;
            mSensorManager.unregisterListener(this, mRotationSensor);
            finish();
        } else {
            dialog.show();
        }

    }

    public void calibrate(){
        final Context con = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(con);
        builder.setMessage("Press ok when the telephone is vertical")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        val1 = pitch;
                        AlertDialog.Builder builder = new AlertDialog.Builder(con);
                        builder.setMessage("Press ok when the telephone is horizontal")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        val2 = pitch;
                                        if(val1>val2)
                                        {
                                            float tmp = val1.floatValue();
                                            val1 = val2;
                                            val2 = tmp;
                                        }
                                        val1+=acceptableThres;
                                        val2-=acceptableThres;

                                        if(val2 - val1 < 20)
                                        {
                                            val2 = null;
                                            val1 = null;
                                            calibrate();
                                        } else {
                                            GlobalValues.situpsCalibrationHigh = val1;
                                            GlobalValues.situpsCalibrationLow = val2;
                                            System.out.println(GlobalValues.situpsCalibrationHigh);
                                            System.out.println(GlobalValues.situpsCalibrationLow);
                                            GlobalValues.saveTrainingData();
                                        }

                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }





    @Override
    public void onBackPressed() {
        exit();
        }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor == mRotationSensor) {
            if (sensorEvent.values.length > 4) {
                float[] truncatedRotationVector = new float[4];
                System.arraycopy(sensorEvent.values, 0, truncatedRotationVector, 0, 4);
                update(truncatedRotationVector);
            } else {
                update(sensorEvent.values);
            }
        }
    }

    private void update(float[] vectors) {
        float[] rotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(rotationMatrix, vectors);
        int worldAxisX = SensorManager.AXIS_X;
        int worldAxisZ = SensorManager.AXIS_Z;
        float[] adjustedRotationMatrix = new float[9];
        SensorManager.remapCoordinateSystem(rotationMatrix, worldAxisX, worldAxisZ, adjustedRotationMatrix);
        float[] orientation = new float[3];
        SensorManager.getOrientation(adjustedRotationMatrix, orientation);
        pitch = Math.abs(orientation[1] * FROM_RADS_TO_DEGS);
        if(val1 != null && val2 != null)
        {
            if(pitch < val1) {
                if(!up) {
                    if(clickable && currentSitups >0)
                    {
                        currentSitups--;
                        everySitup++;
                        setSitups();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            //deprecated in API 26
                            v.vibrate(100);
                        }
                    }
                    if(clickable && currentSitups ==0)
                    {
                        setSitups();
                        setCurrent();
                        clickable = false;
                        if (next<cycles){
                            pausePressed= false;
                            createTimer(trainingLevel*60,true);
                            timer.start();

                            currentSitups = array.get(next);

                            next++;
                        } else {
                            createTimer(3,false);
                            timer.start();
                        }
                    }



                }
                up = true;
            }
            if(pitch > val2)
                up = false;

        }

        //todo make it only first calibration, show a button to recalibrate further in the future



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_situp, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_calibrate:
                calibrate();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


}
