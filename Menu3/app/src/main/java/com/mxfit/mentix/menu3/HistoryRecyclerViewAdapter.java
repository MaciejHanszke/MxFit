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
import android.widget.Toast;

import java.util.List;

class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder> {
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout pop;
        TextView label;
        TextView distance;
        public TextView time;



        ViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            pop = (LinearLayout) itemView.findViewById(R.id.Popup);
            label = (TextView) itemView.findViewById(R.id.text_label);
            distance = (TextView) itemView.findViewById(R.id.text_distance);
            time = (TextView) itemView.findViewById(R.id.text_time);

        }

        @Override
        public void onClick(View view) {
            }
    }

    private List<Run> mRuns;
    Context context;
    DatabaseHelper myDb;
    private HistoryFragment HisFrag;
    MainActivity activity = MainActivity.instance;
    private boolean beenFound = false;

    HistoryRecyclerViewAdapter(List<Run> Runs, DatabaseHelper db, HistoryFragment ins)
    {
        mRuns = Runs;
        myDb = db;
        HisFrag = ins;
    }

    @Override
    public HistoryRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.custom_list_item, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        final Run run = mRuns.get(position);



        TextView mLabel = viewHolder.label;
        mLabel.setText(run.getLabel());

        TextView mDistance = viewHolder.distance;
        mDistance.setText(run.getDistance());

        TextView mTime = viewHolder.time;
        mTime.setText(run.getTime());



        LinearLayout Pop = viewHolder.pop;

        Pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShowMap(activity,run.getLabel());
            }
        });

        ImageView delete = (ImageView) viewHolder.itemView.findViewById(R.id.Delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure that you want to delete this training?")
                        .setTitle("Delete?");
                builder.setNeutralButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(context, "Deleted: "+ run.getLabel(), Toast.LENGTH_SHORT).show();
                        myDb.dropTable((String) run.getLabel());
                        HisFrag.showTables();
                    }
                });

                builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        return;
                    }
                });
                dialog = builder.create();
                dialog.show();
            }
        });
        DBNameFormatter dbf = new DBNameFormatter();
        if(activity.dbTableName != null && !beenFound && dbf.formatName(run.getLabel()).equals(activity.dbTableName)){
            delete.setVisibility(View.INVISIBLE);
            beenFound = true;
        }




    }

    @Override
    public int getItemCount() {
        return mRuns.size();
    }

    public void resetFound(){
        beenFound = false;
    }
}
