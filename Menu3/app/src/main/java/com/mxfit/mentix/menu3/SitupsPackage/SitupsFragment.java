package com.mxfit.mentix.menu3.SitupsPackage;

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

import com.mxfit.mentix.menu3.GlobalValues;
import com.mxfit.mentix.menu3.MainActivity;
import com.mxfit.mentix.menu3.PushupsPackage.TrainingUnitChoiceActivity;
import com.mxfit.mentix.menu3.R;
import com.mxfit.mentix.menu3.Utils.DatabasePremadesHelper;
import com.mxfit.mentix.menu3.Utils.GetColors;
import com.mxfit.mentix.menu3.Utils.PR;
import com.mxfit.mentix.menu3.Utils.SaveFileCalendar;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SitupsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SitupsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SitupsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    final String name = "Sit-ups";


    private TextView txtSum;
    private TextView txttotalSum;
    private TextView txtTrainDate;
    private TextView txtDay;
    private TextView txtTrLevel;
    private TextView txtMaxDays;
    private ProgressBar progressBar;
    ArrayList <Integer> array;
    private LinearLayout LinLay;
    TextView [] textView;
    final MainActivity activity = MainActivity.instance;
    DatabasePremadesHelper myDb;

    int showMaxDays;

    private OnFragmentInteractionListener mListener;

    public SitupsFragment() {
        // Required empty public constructor
    }

    public static SitupsFragment newInstance(int param1, int param2) {
        SitupsFragment fragment = new SitupsFragment();
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
            GlobalValues.situpsDay = getArguments().getInt(ARG_PARAM1);
            GlobalValues.situpsTraininglevel = getArguments().getInt(ARG_PARAM2);
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
        TextView txtTrainingType = (TextView) view.findViewById(R.id.TrainingType);
        txtTrainingType.setText(name);
        CreateSitupTable();

        TextView textViewCounter = (TextView)view.findViewById(R.id.txtTypeCounter);
        textViewCounter.setText(name+" done so far:");
        startPushups.setText("Start " + name +" Training");

        startPushups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!activity.isInTraining) {
                    Intent situps = new Intent(getActivity(), SitupsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("DAY", GlobalValues.situpsDay);
                    bundle.putInt("TrainingLevel", GlobalValues.situpsTraininglevel);
                    bundle.putIntegerArrayList("Array", array);
                    situps.putExtras(bundle);
                    startActivityForResult(situps, 1);
                } else {
                    Toast.makeText(getContext(),"You have to finish running first!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button buttonChangeTraining = (Button) view.findViewById(R.id.viewChangeTraining);
        buttonChangeTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent situpsChoice = new Intent(getActivity(), TrainingUnitChoiceActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("type", "SitupsL%");
                    bundle.putString("trainingType", "situps");
                    situpsChoice.putExtras(bundle);

                    startActivityForResult(situpsChoice, 2);

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                myDb.setSitupsFinished(GlobalValues.situpsDay,GlobalValues.situpsTraininglevel);

                new SaveFileCalendar().setSitupsDate();

                GlobalValues.situpsDay++;
                fixDays();
                activity.resetAlarm();
            }
        }
        GlobalValues.saveTrainingData();
        CreateSitupTable();
    }


    public void CreateSitupTable()
    {
        int sum= 0;
        fixDays();
        LinLay.removeAllViews();
        array = new PR().redo(myDb.getDaySitups(GlobalValues.situpsDay,GlobalValues.situpsTraininglevel).replace('x','-'));
        textView = new TextView[array.size()];
        int textColor = GetColors.getFont(getContext(),activity.themeNumber);
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
        txttotalSum.setText(""+GlobalValues.situpsDone);
        txtDay.setText(""+GlobalValues.situpsDay);
        txtTrLevel.setText(""+GlobalValues.situpsTraininglevel);
        progressBar.getProgressDrawable().setColorFilter(GetColors.getFont(getContext(),activity.themeNumber), PorterDuff.Mode.SRC_IN);
        progressBar.setProgress(GlobalValues.situpsDay*100/showMaxDays);
        txtMaxDays.setText("/"+showMaxDays);
        if(GlobalValues.lastSitupDate!= null) {
            txtTrainDate.setText(GlobalValues.lastSitupDate);
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
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int pday, int tlevel);
    }

    public void fixDays()
    {
        showMaxDays = myDb.getHowManySitupDays(GlobalValues.situpsTraininglevel);
        if(GlobalValues.situpsDay>showMaxDays)
        {
            GlobalValues.situpsDay = 1;
            if(GlobalValues.situpsTraininglevel < myDb.getSitupTrainingsInTrainingLevel())
                GlobalValues.situpsTraininglevel++;
        }
    }
}
