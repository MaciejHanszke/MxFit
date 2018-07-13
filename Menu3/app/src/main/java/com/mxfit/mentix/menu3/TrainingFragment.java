package com.mxfit.mentix.menu3;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TrainingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TrainingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrainingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private int mParam2;
    private double mParam3;

    private TextView day;
    private TextView maxDay;
    private TextView tlevel;
    private TextView txtPushupDate;
    private TextView txtRunningDate;
    private LinearLayout txtPushupSets;
    private Switch SynchroSwitch;
    private Button buttonPushups;
    private Button buttonRunning;
    private Button buttonBoth;
    private TextView tvBoth1;
    private TextView tvBoth2;
    public TextView RunningDay;
    public TextView goal;
    DatabasePremadesHelper myDb;
    ArrayList <Integer> array;
    TextView [] textView;

    final MainActivity activity = MainActivity.instance;



    private OnFragmentInteractionListener mListener;

    public TrainingFragment() {
        // Required empty public constructor
    }


    public static TrainingFragment newInstance(int param1, int param2, double param3) {
        TrainingFragment fragment = new TrainingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        args.putDouble(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
            mParam3 = getArguments().getDouble(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_training, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myDb = new DatabasePremadesHelper(getContext());
        day = (TextView) view.findViewById(R.id.currentDay);
        maxDay = (TextView) view.findViewById(R.id.txtShowMaxDays);
        tlevel = (TextView) view.findViewById(R.id.currentTrainingLevel);
        txtPushupDate = (TextView) view.findViewById(R.id.TrainDate);
        txtRunningDate = (TextView) view.findViewById(R.id.TrainRDate);
        txtPushupSets = (LinearLayout)view.findViewById(R.id.fragcycle);
        SynchroSwitch = (Switch) view.findViewById(R.id.switch1);
        buttonRunning = (Button) view.findViewById(R.id.buttonRT);
        buttonPushups = (Button) view.findViewById(R.id.buttonPT);
        buttonBoth = (Button) view.findViewById(R.id.buttonBothT);
        goal = (TextView) view.findViewById(R.id.currentKilos);
        RunningDay = (TextView) view.findViewById(R.id.currentRDay);
        Button buttongotoPushups = (Button) view.findViewById(R.id.gotoPU);
        Button buttongotoRunning = (Button) view.findViewById(R.id.gotoR);
        tvBoth1 = (TextView) view.findViewById(R.id.viewBoth1);
        tvBoth2 = (TextView) view.findViewById(R.id.viewBoth2);

        SynchroSwitch.setChecked(activity.Synchronised);

        SynchroSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                activity.Synchronised = isChecked;
                setSyncButtonVisibility();
            }
        });


        buttonPushups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Pushupschoice = new Intent(getActivity(), PushupsChoiceActivity.class);
                startActivityForResult(Pushupschoice, 1);
            }
        });

        buttonRunning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Runningchoice = new Intent(getActivity(), PreRunChoiceActivity.class);
                startActivityForResult(Runningchoice, 2);
            }
        });

        buttonBoth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Daychoice = new Intent(getActivity(), DayChoiceActivity.class);
                startActivityForResult(Daychoice, 3);
            }
        });

        buttongotoPushups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.showPushups();
                activity.navigationView.getMenu().getItem(2).setChecked(true);
            }
        });

        buttongotoRunning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.showRunning();
                activity.navigationView.getMenu().getItem(1).setChecked(true);
            }
        });

        createView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                mParam1 = activity.day;
                mParam2 = activity.traininglevel;
            }
        }

        activity.saveTrainingData();
        createView();
    }

    public void createView(){
        day.setText(""+activity.day);
        tlevel.setText(""+activity.traininglevel);
        if(activity.lastPushupDate != null) {
            txtPushupDate.setText(activity.lastPushupDate);
        }

        if(activity.lastRunDate != null) {
            txtRunningDate.setText(activity.lastRunDate);
        }
        int textColor = getColors.getFont(getContext(),activity.themeNumber);
        txtPushupSets.removeAllViews();
        array = new PR().redo(myDb.getDayPushups(activity.day,activity.traininglevel).replace('x','-'));
        textView = new TextView[array.size()];
        for( int h = 0; h < array.size(); h++ )
        {
            textView[h] = new TextView(getContext());
            if (h != array.size()-1)
                textView[h].setText(""+array.get(h) + " - ");
            else
                textView[h].setText(""+array.get(h));
            textView[h].setTypeface(null, Typeface.BOLD);
            textView[h].setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            textView[h].setGravity(Gravity.END);
            textView[h].setTextColor(textColor);
            txtPushupSets.addView(textView[h]);

        }
        setSyncButtonVisibility();
        maxDay.setText("/"+myDb.getHowManyDays(activity.traininglevel));
        goal.setText(""+activity.goal);
        RunningDay.setText(""+activity.rday);
    }

    public void setSyncButtonVisibility(){
        if(activity.Synchronised)
        {
            buttonRunning.setVisibility(View.INVISIBLE);
            buttonPushups.setVisibility(View.INVISIBLE);
            buttonBoth.setVisibility(View.VISIBLE);
            tvBoth1.setVisibility(View.VISIBLE);
            tvBoth2.setVisibility(View.VISIBLE);
        } else {
            buttonRunning.setVisibility(View.VISIBLE);
            buttonPushups.setVisibility(View.VISIBLE);
            buttonBoth.setVisibility(View.INVISIBLE);
            tvBoth1.setVisibility(View.INVISIBLE);
            tvBoth2.setVisibility(View.INVISIBLE);
        }
    }


    public void onButtonPressed(int day, int TLevel, float goal) {
        if (mListener != null) {
            mListener.onFragmentInteraction(day, TLevel, goal);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int day, int TLevel, float goal);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
