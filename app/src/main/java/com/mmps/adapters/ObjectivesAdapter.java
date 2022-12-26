package com.mmps.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mmps.R;
import com.mmps.models.ObjectiveModel;

import java.util.ArrayList;

/**
 * @author Aditya Kulkarni
 */

public class ObjectivesAdapter extends RecyclerView.Adapter<ObjectivesAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ObjectiveModel> objectiveModels;

    public ObjectivesAdapter(Context context, ArrayList<ObjectiveModel> objectiveModels) {
        this.context = context;
        this.objectiveModels = objectiveModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.objective_row_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return objectiveModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvObjective;
        public ViewHolder(View itemView) {
            super(itemView);

            tvObjective = itemView.findViewById(R.id.text1);
            tvObjective.setSelected(true);
        }

        void bindView(int position){
            tvObjective.setText(objectiveModels.get(position).getObjective());
        }
    }
}
