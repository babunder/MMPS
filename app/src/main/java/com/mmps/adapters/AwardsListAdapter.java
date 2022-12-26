package com.mmps.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mmps.R;
import com.mmps.models.AwardsModel;
import com.mmps.models.SmartLibraryResponseModel;
import com.mmps.utils.URLConstants;
import com.mmps.worker.LibraryTasks;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Aditya Kulkarni
 */

public class AwardsListAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<AwardsModel> awardsModels;
    private int SrNo;
    private String dbName;
    private SmartLibraryResponseModel libraryResponseModel;

    public AwardsListAdapter(Context context, ArrayList<AwardsModel> awardsModels, int SrNo, String dbName, SmartLibraryResponseModel libraryResponseModel) {
        this.context = context;
        this.awardsModels = awardsModels;
        this.SrNo = SrNo;
        this.dbName = dbName;
        this.libraryResponseModel = libraryResponseModel;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.awards_list_row_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return awardsModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvAwardName;
        public ViewHolder(View itemView) {
            super(itemView);

            tvAwardName = itemView.findViewById(R.id.text1);
        }

        void bindView(final int position){
            tvAwardName.setText(awardsModels.get(position).getAwardTitle());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("Award", awardsModels.get(position).getAwardId() + "");
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("library", libraryResponseModel);
                    map.put(URLConstants.PARAM_ORG_ID, SrNo);
                    map.put(URLConstants.PARAM_DB_NAME, dbName);
                    map.put(URLConstants.PARAM_AWARD_ID, awardsModels.get(position).getSrNo());
                    new LibraryTasks(URLConstants.AWARD_INFO_ACTION, map, context).execute();
                }
            });
        }
    }
}
