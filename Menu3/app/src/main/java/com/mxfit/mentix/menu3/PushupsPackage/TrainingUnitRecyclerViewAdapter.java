package com.mxfit.mentix.menu3.PushupsPackage;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mxfit.mentix.menu3.Beans.TrainingUnit;
import com.mxfit.mentix.menu3.GlobalValues;
import com.mxfit.mentix.menu3.MainActivity;
import com.mxfit.mentix.menu3.R;
import com.mxfit.mentix.menu3.Utils.DatabaseHelper;

import java.util.List;




class TrainingUnitRecyclerViewAdapter extends RecyclerView.Adapter<TrainingUnitRecyclerViewAdapter.ViewHolder> {
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout pop;
        TextView label;
        TextView sets;



        ViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            pop = (LinearLayout) itemView.findViewById(R.id.Popup2);
            label = (TextView) itemView.findViewById(R.id.txtCstDay);
            sets = (TextView) itemView.findViewById(R.id.text_sets);

        }

        @Override
        public void onClick(View view) {
            }
    }

    private List<TrainingUnit> mPushups;
    Context context;
    DatabaseHelper myDb;
    MainActivity activity = MainActivity.instance;
    private TrainingUnitChoiceActivity PCA;
    private String trainingType;


    TrainingUnitRecyclerViewAdapter(List<TrainingUnit> pushups, TrainingUnitChoiceActivity pPCA, String trainingType)
    {
        mPushups = pushups;
        PCA = pPCA;
        this.trainingType = trainingType;
    }

    @Override
    public TrainingUnitRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.custom_list_item_pushups, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final TrainingUnit trainingUnit = mPushups.get(position);



            final TextView mLabel = viewHolder.label;
            TextView mSets = viewHolder.sets;

        ImageView finished = (ImageView) viewHolder.itemView.findViewById(R.id.iconFinished);
            TextView txt = (TextView) viewHolder.itemView.findViewById(R.id.textViewDay);
            LinearLayout.LayoutParams layoutparams;




        LinearLayout Pop = viewHolder.pop;

            if(trainingUnit.getLabel()!=0) {
                layoutparams = (LinearLayout.LayoutParams) finished.getLayoutParams();
                mLabel.setText("" + trainingUnit.getLabel());
                mSets.setText(trainingUnit.getSets());
                Pop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    showDialog(trainingUnit.getLabel(), trainingUnit.getTrainingLevel(),trainingType);
                    }
                });
                if (trainingUnit.getFinished()) {
                    finished.setBackgroundResource(R.drawable.ic_finished);
                } else {
                    finished.setBackgroundResource(R.drawable.ic_unfinished);
                }
                mSets.setGravity(Gravity.END|Gravity.CENTER_VERTICAL);
                finished.setVisibility(View.VISIBLE);
                txt.setVisibility(View.VISIBLE);
                layoutparams.setMargins(20,20,20,20);
                finished.setLayoutParams(layoutparams);
            }else {
                layoutparams = (LinearLayout.LayoutParams) finished.getLayoutParams();
                String name = "Training Level: " + trainingUnit.getTrainingLevel();
                mLabel.setText(" ");
                mSets.setText(name);
                mSets.setGravity(Gravity.CENTER);
                Pop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                finished.setVisibility(View.INVISIBLE);
                txt.setVisibility(View.GONE);
                layoutparams.setMargins(150,10,10,10);
                finished.setLayoutParams(layoutparams);
            }



    }

    @Override
    public int getItemCount() {
        return mPushups.size();
    }

    private void showDialog(final int pday, final int ptrainingLevel, String trainingType){
        if(trainingType.equals("pushups")) {
            if (GlobalValues.lastPushupDate == null) {
                GlobalValues.pushupsDay = pday;
                GlobalValues.pushupsTraininglevel = ptrainingLevel;
                GlobalValues.PUdateCount = 0;
                PCA.exit();
            } else {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Overwrite?");
                alertDialog.setMessage("Are you sure you want to overwrite your progress?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        GlobalValues.pushupsDay = pday;
                        GlobalValues.pushupsTraininglevel = ptrainingLevel;
                        GlobalValues.PUdateCount = 0;
                        PCA.exit();
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        PCA.exit();
                    }
                });
                alertDialog.show();

            }
        } else if (trainingType.equals("situps")) {
            if (GlobalValues.lastSitupDate == null) {
                GlobalValues.situpsDay = pday;
                GlobalValues.situpsTraininglevel = ptrainingLevel;
                GlobalValues.SUdateCount = 0;
                PCA.exit();
            } else {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Overwrite?");
                alertDialog.setMessage("Are you sure you want to overwrite your progress?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        GlobalValues.situpsDay = pday;
                        GlobalValues.situpsTraininglevel = ptrainingLevel;
                        GlobalValues.SUdateCount = 0;
                        PCA.exit();
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        PCA.exit();
                    }
                });
                alertDialog.show();

            }
        }
    }
}
