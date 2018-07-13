package com.mxfit.mentix.menu3;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mxfit.mentix.menu3.MainActivity;
import com.mxfit.mentix.menu3.R;

import java.util.ArrayList;

public class PushupsActivity extends AppCompatActivity {

    private Button PushupsBigButton;
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
    int currentPushups;
    int everyPushup = 0;
    boolean clickable = true;
    boolean pausePressed = false;
    private CountDownTimer timer;
    final MainActivity activity = MainActivity.instance;

    ArrayList<Integer> array;

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


    private void setPushups(){
        if(!clickable)
        {setCurrent();}
        clickable = true;
        Spannable spannable = new SpannableString(currentPushups + "\n Push-ups");
        int length = String.valueOf(currentPushups).length();
        spannable.setSpan(new RelativeSizeSpan(6.5f), 0, length,Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        PushupsBigButton.setText(spannable);
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
                    PushupsBigButton.setText(spannable);
                }


                @Override
                public void onFinish() {
                    setPushups();
                    timer = null;
                }
            };
        } else if (!isBreakTimer) {
            timer = new CountDownTimer(Seconds * 1000+999, 100) {
                @Override
                public void onTick(long l) {
                    String reformattedTime = reformatTime((int) l / 1000);
                    Spannable spannable = new SpannableString("Pushups finished. \nReturning to menu in:" + reformattedTime);
                    PushupsBigButton.setText(spannable);
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
        if(currentPushups == 0)
        {
            Intent intent = new Intent();
            intent.putExtra("haveFinished", "1");
            setResult(RESULT_OK, intent);
            activity.pushupsDone+=everyPushup;
            finish();
        } else {
            dialog.show();
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        day = extras.getInt("DAY");
        trainingLevel = extras.getInt("TrainingLevel");
        array = extras.getIntegerArrayList("Array");




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pushups);
        PushupsBigButton = (Button) findViewById(R.id.button7);
        FinishButton = (Button) findViewById(R.id.button8);
        PauseButton = (Button) findViewById(R.id.button9);
        PushupsBigButton.setAllCaps(false);
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
        currentPushups = array.get(0);
        setPushups();
        setCurrent();



        //buttons
        PushupsBigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clickable && currentPushups>0)
                {
                    currentPushups--;
                    everyPushup++;
                    setPushups();

                }
                if(clickable && currentPushups==0)
                {
                    setPushups();
                    setCurrent();
                    clickable = false;
                    if (next<cycles){
                        pausePressed= false;
                        createTimer(trainingLevel*60,true);
                        timer.start();

                    currentPushups = array.get(next);

                        next++;
                    } else {
                        createTimer(3,false);
                        timer.start();
                    }
                }

            }
        });

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
                activity.pushupsDone+=everyPushup;
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

    @Override
    public void onBackPressed() {
        exit();
        }



}
