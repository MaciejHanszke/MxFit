package com.mxfit.mentix.menu3;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PushupsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PushupsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PushupsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private TextView txtSum;
    private TextView txttotalSum;
    private TextView txtTrainDate;
    private TextView txtDay;
    private TextView txtTrLevel;
    private TextView txtMaxDays;
    private ProgressBar progressBar;
    int day;
    int trainingLevel;
    ArrayList <Integer> array;
    private LinearLayout LinLay;
    TextView [] textView;
    final MainActivity activity = MainActivity.instance;
    DatabasePremadesHelper myDb;

    int showMaxDays;

    private OnFragmentInteractionListener mListener;

    public PushupsFragment() {
        // Required empty public constructor
    }

    public static PushupsFragment newInstance(int param1, int param2) {
        PushupsFragment fragment = new PushupsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            day = getArguments().getInt(ARG_PARAM1);
            trainingLevel = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pushups, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button startPushups = (Button) view.findViewById(R.id.button2);

        myDb = new DatabasePremadesHelper(getContext());


        LinLay = (LinearLayout)view.findViewById(R.id.fragcycle);
        txtSum = (TextView) view.findViewById(R.id.fragsum);
        txttotalSum = (TextView) view.findViewById(R.id.fragtotalsum);
        txtTrainDate = (TextView) view.findViewById(R.id.TrainDate);
        txtDay = (TextView) view.findViewById(R.id.currentDay);
        txtTrLevel = (TextView) view.findViewById(R.id.trLevel);
        txtMaxDays = (TextView) view.findViewById(R.id.txtShowMaxDays);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar3);
        CreatePushupsTable();

        startPushups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!activity.isInTraining) {
                    Intent pushups = new Intent(getActivity(), PushupsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("DAY", day);
                    bundle.putInt("TrainingLevel", trainingLevel);
                    bundle.putIntegerArrayList("Array", array);
                    pushups.putExtras(bundle);
                    startActivityForResult(pushups, 1);
                } else {
                    Toast.makeText(getContext(),"You have to finish running first!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button buttonChangeTraining = (Button) view.findViewById(R.id.viewChangeTraining);
        buttonChangeTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent Pushupschoice = new Intent(getActivity(), PushupsChoiceActivity.class);
                    startActivityForResult(Pushupschoice, 2);

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                myDb.setPushupsFinished(day,trainingLevel);
                if(!activity.Synchronised)
                {
                    new SaveFileCalendar().setPushupsDate();
                } else {
                    new SaveFileCalendar().setDayDate(true);
                }

                day++;
                fixDays();
                onButtonPressed(day,trainingLevel);
                activity.resetAlarm();
            }
        }
        if (requestCode == 2) {
            if(resultCode == RESULT_OK) {
                day = activity.day;
                trainingLevel = activity.traininglevel;
            }
        }
        activity.saveTrainingData();
        CreatePushupsTable();
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int pday, int tlevel) {
        pday = day;
        tlevel = trainingLevel;
        if (mListener != null) {
            mListener.onFragmentInteraction(pday, tlevel);
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

    public void CreatePushupsTable()
    {
        int sum= 0;
        fixDays();
        LinLay.removeAllViews();
        array = new PR().redo(myDb.getDayPushups(day,trainingLevel).replace('x','-'));
        textView = new TextView[array.size()];
        int textColor = getColors.getFont(getContext(),activity.themeNumber);
        for( int h = 0; h < array.size(); h++ )
        {
            textView[h] = new TextView(getContext());
            if (h != array.size()-1)
                textView[h].setText(""+array.get(h) + " - ");
            else
                textView[h].setText(""+array.get(h));

            textView[h].setTextColor(textColor);
            textView[h].setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            textView[h].setGravity(Gravity.END);
            LinLay.addView(textView[h]);
            sum+= array.get(h);
        }
        txtSum.setText(""+sum);
        txttotalSum.setText(""+activity.pushupsDone);
        txtDay.setText(""+day);
        txtTrLevel.setText(""+trainingLevel);
        progressBar.getProgressDrawable().setColorFilter(getColors.getFont(getContext(),activity.themeNumber), PorterDuff.Mode.SRC_IN);
        progressBar.setProgress(day*100/showMaxDays);
        txtMaxDays.setText("/"+showMaxDays);
        if(activity.lastPushupDate!= null) {
            txtTrainDate.setText(activity.lastPushupDate);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    interface OnFragmentInteractionListener {
        void onFragmentInteraction(int pday, int tlevel);
    }

    public void fixDays()
    {
        showMaxDays = myDb.getHowManyDays(trainingLevel);
        if(day>showMaxDays)
        {
            day = 1;
            if(trainingLevel < myDb.getHowManyTrainings())
                trainingLevel++;
        }
    }
}
