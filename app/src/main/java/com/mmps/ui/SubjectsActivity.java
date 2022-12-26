package com.mmps.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
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
import com.mmps.adapters.SubjectsAdapter;
import com.mmps.models.CourseModel;
import com.mmps.models.SmartLibraryResponseModel;
import com.mmps.models.SubjectModel;
import com.mmps.utils.SOAPUtils;
import com.mmps.utils.URLConstants;
import com.mmps.worker.LibraryTasks;

import java.util.ArrayList;
import java.util.HashMap;

public class SubjectsActivity extends BaseActivity {

    private Toolbar toolbar;
    private SmartLibraryResponseModel libraryResponseModel;
    private int libraryId;
    private String databaseName;
    private TextView tvTitle, tvCourseDescription, tvCOurseEligibility, tvCourseDuration;
    private RecyclerView rvSubjects;
    private Button btnSearch;
    private EditText etSearchBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);

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
        rvSubjects = findViewById(R.id.rvSubjects);
        rvSubjects.setLayoutManager(new LinearLayoutManager(SubjectsActivity.this));
        tvTitle = toolbar.findViewById(R.id.tv_title);
        tvCourseDescription = findViewById(R.id.tvCourseDescription);
        tvCOurseEligibility = findViewById(R.id.tvCourseEligibility);
        tvCourseDuration = findViewById(R.id.tvCourseDuration);
        btnSearch = findViewById(R.id.btnSearch);
        etSearchBook = findViewById(R.id.etSeachBook);

        tvTitle.setText((String) getIntent().getExtras().get("courseTitle"));

        if(getIntent().hasExtra("library")){
            libraryResponseModel = (SmartLibraryResponseModel) getIntent().getExtras().get("library");
            libraryId = libraryResponseModel.getLibraryId();
            databaseName = libraryResponseModel.getDatabaseName();
            final int regionId = libraryResponseModel.getRegionId();
            btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SOAPUtils.isNetworkConnected(SubjectsActivity.this)) {
                        if (!etSearchBook.getText().toString().isEmpty()) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put(URLConstants.PARAM_SEARCH, etSearchBook.getText().toString());
                            map.put(URLConstants.PARAM_ORG_ID, libraryId);
                            map.put(URLConstants.PARAM_REGION, regionId);
                            map.put(URLConstants.PARAM_DB_NAME, databaseName);
                            map.put("library", libraryResponseModel);
                            new LibraryTasks(URLConstants.ACTION_SEARCH_BOOK, map, SubjectsActivity.this).execute();
                        } else {
                            etSearchBook.setError(getString(R.string.cannot_be_empty));
                        }
                    } else {
                        Toast.makeText(SubjectsActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        if(getIntent().hasExtra("course")){
            CourseModel courseModel = (CourseModel) getIntent().getExtras().get("course");
            tvTitle.setText(courseModel.getCourseTitle());
            tvCourseDescription.setText(courseModel.getDescription());
            tvCourseDuration.setText(courseModel.getDuration());
            tvCOurseEligibility.setText(courseModel.getElligibility());
        }

        if(getIntent().hasExtra("subjects")){
            ArrayList<SubjectModel> subjectModels = (ArrayList<SubjectModel>) getIntent().getExtras().get("subjects");
            rvSubjects.setAdapter(new SubjectsAdapter(SubjectsActivity.this, subjectModels, libraryId, databaseName, libraryResponseModel));
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
