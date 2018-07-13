package com.mxfit.mentix.menu3;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


class PreRunRecyclerViewAdapter extends RecyclerView.Adapter<PreRunRecyclerViewAdapter.ViewHolder> {
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout pop;
        TextView label;
        TextView kilometers;



        ViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            pop = (LinearLayout) itemView.findViewById(R.id.Popup3);
            label = (TextView) itemView.findViewById(R.id.txtCstDay);
            kilometers = (TextView) itemView.findViewById(R.id.text_km);

        }

        @Override
        public void onClick(View view) {
            }
    }

    private List<PreRun> mPreRuns;
    Context context;
    MainActivity activity = MainActivity.instance;
    private PreRunChoiceActivity PCA;

    PreRunRecyclerViewAdapter(List<PreRun> preRuns, PreRunChoiceActivity pPCA)
    {
        mPreRuns = preRuns;
        PCA = pPCA;
    }

    @Override
    public PreRunRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.custom_list_item_preruns, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final PreRun preRun = mPreRuns.get(position);



            final TextView mLabel = viewHolder.label;
            TextView mkilometers = viewHolder.kilometers;
            mLabel.setText(""+preRun.getDay());
            mkilometers.setText(""+preRun.getKm());
            LinearLayout Pop = viewHolder.pop;

        ImageView finished = (ImageView) viewHolder.itemView.findViewById(R.id.iconFinished);
            if(preRun.isDone())
            {
                finished.setBackgroundResource(R.drawable.ic_finished);}
            else
            {
                finished.setBackgroundResource(R.drawable.ic_unfinished);}
            TextView txt = (TextView) viewHolder.itemView.findViewById(R.id.textViewDay);
            LinearLayout.LayoutParams layoutparams;
                Pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(preRun.getDay(), preRun.getKm());
            }
        });



    }

    @Override
    public int getItemCount() {
        return mPreRuns.size();
    }

    private void showDialog(final int day, final float goal){
        if(activity.lastRunDate == null)
        {
            activity.rday = day;
            activity.goal = goal;
            activity.RdateCount = 0;
            PCA.exit();
        } else {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("Overwrite?");
            alertDialog.setMessage("Are you sure you want to overwrite your progress?");
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    activity.rday = day;
                    activity.goal = goal;
                    activity.RdateCount = 0;
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
