package com.mmps.adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mmps.R;
import com.mmps.models.SmartLibraryResponseModel;
import com.mmps.models.SubjectModel;
import com.mmps.utils.URLConstants;
import com.mmps.worker.LibraryTasks;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Aditya Kulkarni
 */

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<SubjectModel> subjectModels;
    private int libraryId;
    private String databaseName;
    private SmartLibraryResponseModel libraryResponseModel;

    public SubjectsAdapter(Context context, ArrayList<SubjectModel> subjectModels, int libraryId, String databaseName, SmartLibraryResponseModel libraryResponseModel) {
        this.context = context;
        this.subjectModels = subjectModels;
        this.libraryId = libraryId;
        this.databaseName = databaseName;
        this.libraryResponseModel = libraryResponseModel;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_row_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return subjectModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvTitle;
        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.text1);
            tvTitle.setSelected(true);
        }

        void bindView(final int position){
            tvTitle.setText(subjectModels.get(position).getSubject());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("library", libraryResponseModel);
                    map.put("subject", subjectModels.get(position));
                    map.put(URLConstants.PARAM_ORG_ID, libraryId);
                    map.put(URLConstants.PARAM_DB_NAME, databaseName);
                    map.put(URLConstants.PARAM_SUBJECT_ID, subjectModels.get(position).getSrNo());
                    new LibraryTasks(URLConstants.OBJECTIVE_ACTION, map, context).execute();
                }
            });
        }
    }
}
