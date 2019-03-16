package com.mxfit.mentix.menu3.RunningPackage;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mxfit.mentix.menu3.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class MapsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM6 = "param6";

    private String mParam1;
    private double mParam2;
    private String mParam3;
    private double mParam4;
    private boolean mParam5;
    private String mParam6;

    public MapsFragment() {
        // Required empty public constructor
    }

    public static MapsFragment newInstance(String param1, double param2,String param3, double param4,boolean param5, String param6) {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putDouble(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putDouble(ARG_PARAM4, param4);
        args.putBoolean(ARG_PARAM5, param5);
        args.putString(ARG_PARAM6, param6);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getDouble(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
            mParam4 = getArguments().getDouble(ARG_PARAM4);
            mParam5 = getArguments().getBoolean(ARG_PARAM5);
            mParam6 = getArguments().getString(ARG_PARAM6);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_maps, container, false);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView txtMenuDistance = (TextView) view.findViewById(R.id.textView2);
        txtMenuDistance.setText(mParam6);

        txtMenuDistance = (TextView) view.findViewById(R.id.textView12);
        txtMenuDistance.setText(mParam1);

        txtMenuDistance = (TextView) view.findViewById(R.id.textView13);
        txtMenuDistance.setText(""+mParam2);

        txtMenuDistance = (TextView) view.findViewById(R.id.GoalInt);
        txtMenuDistance.setText(mParam3);


        if(mParam5)
        {
            txtMenuDistance.setTextColor(Color.GREEN);
        } else {
            txtMenuDistance.setTextColor(Color.RED);
        }


        txtMenuDistance = (TextView) view.findViewById(R.id.GoalFinal);
        NumberFormat formatter = new DecimalFormat("#0.00");
        txtMenuDistance.setText(""+formatter.format(mParam4));



    }
}
