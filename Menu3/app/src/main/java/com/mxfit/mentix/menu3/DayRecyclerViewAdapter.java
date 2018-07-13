package com.mxfit.mentix.menu3;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;



class DayRecyclerViewAdapter extends RecyclerView.Adapter<DayRecyclerViewAdapter.ViewHolder> {
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
    private List<Pushups> mPushups;
    Context context;
    DatabaseHelper myDb;
    MainActivity activity = MainActivity.instance;
    private DayChoiceActivity PCA;

    DayRecyclerViewAdapter(List<PreRun> preRuns, List<Pushups> pushups, DayChoiceActivity pPCA)
    {
        mPreRuns = preRuns;
        mPushups = pushups;
        PCA = pPCA;
    }

    @Override
    public DayRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.custom_list_item_day, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final PreRun preRun = mPreRuns.get(position);
        final Pushups pushups = mPushups.get(position);



            final TextView mLabel = viewHolder.label;
            TextView mkilometers = viewHolder.kilometers;
            mLabel.setText(""+preRun.getDay());
            String text = "<font color=#00a6ff>"+ pushups.getSets() +"</font>,  " + preRun.getKm();
            mkilometers.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
            LinearLayout Pop = viewHolder.pop;

        ImageView rfinished = (ImageView) viewHolder.itemView.findViewById(R.id.iconRFinished);
        ImageView pfinished = (ImageView) viewHolder.itemView.findViewById(R.id.iconPUFinished);
            if(pushups.getFinished())
            {
                pfinished.setBackgroundResource(R.drawable.ic_pfinished);}
            else
            {
                pfinished.setBackgroundResource(R.drawable.ic_punfinished);}
            if(preRun.isDone())
            {
                rfinished.setBackgroundResource(R.drawable.ic_finished);}
            else
            {
                rfinished.setBackgroundResource(R.drawable.ic_unfinished);}

                Pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(preRun.getDay(), preRun.getKm() );

            }
        });



    }

    @Override
    public int getItemCount() {
        return mPreRuns.size() < mPushups.size()? mPreRuns.size() : mPushups.size();
    }

    private void showDialog(final int day, final float goal){
        if(activity.lastPushupDate == null && activity.lastRunDate == null)
        {
            activity.rday = day;
            activity.goal = goal;
            PCA.setChosen();
            PCA.exit();
        } else {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("Overwrite?");
            alertDialog.setMessage("Are you sure you want to overwrite your progress?");
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    activity.rday = day;
                    activity.goal = goal;
                    activity.PUdateCount = 0;
                    PCA.setChosen();
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
