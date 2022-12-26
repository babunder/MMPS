package com.mmps.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mmps.R;
import com.mmps.adapters.ObjectivesAdapter;
import com.mmps.models.ObjectiveModel;
import com.mmps.models.SmartLibraryResponseModel;
import com.mmps.models.SubjectModel;
import com.mmps.utils.SOAPUtils;
import com.mmps.utils.URLConstants;
import com.mmps.worker.LibraryTasks;

import java.util.ArrayList;
import java.util.HashMap;

public class ObjectiveActivity extends BaseActivity {

    private Toolbar toolbar;
    private SmartLibraryResponseModel libraryResponseModel;
    private TextView tvTitle, tvSubjectDescription;
    private RecyclerView rvObjectives;
    private Button btnSearch;
    private EditText etSearchBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objective);

        initToolbar();
        initui();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
    }

    private void initui() {
        rvObjectives = findViewById(R.id.rvObjectives);
        rvObjectives.setLayoutManager(new LinearLayoutManager(ObjectiveActivity.this));
        tvTitle = toolbar.findViewById(R.id.tv_title);
        tvTitle.setSelected(true);
        tvSubjectDescription = findViewById(R.id.tvSubjectDescription);
        btnSearch = findViewById(R.id.btnSearch);
        etSearchBook = findViewById(R.id.etSeachBook);

        tvTitle.setText((String) getIntent().getExtras().get("courseTitle"));

        if(getIntent().hasExtra("library")){
            libraryResponseModel = (SmartLibraryResponseModel) getIntent().getExtras().get("library");
            final int libraryId = libraryResponseModel.getLibraryId();
            final String databaseName = libraryResponseModel.getDatabaseName();
            final int regionId = libraryResponseModel.getRegionId();
            btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SOAPUtils.isNetworkConnected(ObjectiveActivity.this)) {
                        if (!etSearchBook.getText().toString().isEmpty()) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put(URLConstants.PARAM_SEARCH, etSearchBook.getText().toString());
                            map.put(URLConstants.PARAM_ORG_ID, libraryId);
                            map.put(URLConstants.PARAM_REGION, regionId);
                            map.put(URLConstants.PARAM_DB_NAME, databaseName);
                            map.put("library", libraryResponseModel);
                            new LibraryTasks(URLConstants.ACTION_SEARCH_BOOK, map, ObjectiveActivity.this).execute();
                        } else {
                            etSearchBook.setError(getString(R.string.cannot_be_empty));
                        }
                    } else {
                        Toast.makeText(ObjectiveActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        if(getIntent().hasExtra("subject")){
            SubjectModel courseModel = (SubjectModel) getIntent().getExtras().get("subject");
            tvTitle.setText(courseModel.getDescription());
            tvSubjectDescription.setText(courseModel.getDescription());
        }

        if(getIntent().hasExtra("objectives")){
            ArrayList<ObjectiveModel> objectiveModels = (ArrayList<ObjectiveModel>) getIntent().getExtras().get("objectives");
            rvObjectives.setAdapter(new ObjectivesAdapter(ObjectiveActivity.this, objectiveModels));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return false;
        }
    }
}
