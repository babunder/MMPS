package com.mmps.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mmps.R;
import com.mmps.adapters.AwardsListAdapter;
import com.mmps.models.AwardsModel;
import com.mmps.models.SmartLibraryResponseModel;
import com.mmps.utils.SOAPUtils;
import com.mmps.utils.URLConstants;
import com.mmps.worker.LibraryTasks;

import java.util.ArrayList;
import java.util.HashMap;

public class AwardActivity extends BaseActivity {

    private Toolbar toolbar;
    private RecyclerView rvAwardsList;
    private EditText etSearchBook;
    private Button btnSearch;
    private SmartLibraryResponseModel libraryResponseModel;
    private int libraryId;
    private String databaseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award);

        initToolbar();
        initui();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.tv_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        title.setText(getString(R.string.vividha_patrakarita_puraskar));
    }

    private void initui() {
        etSearchBook = findViewById(R.id.etSeachBook);
        btnSearch = findViewById(R.id.btnSearch);
        rvAwardsList = findViewById(R.id.rvAwardsLisr);
        rvAwardsList.setLayoutManager(new LinearLayoutManager(AwardActivity.this));

        if (getIntent().hasExtra("library")) {
            libraryResponseModel = (SmartLibraryResponseModel) getIntent().getExtras().get("library");
            libraryId = libraryResponseModel.getLibraryId();
            databaseName = libraryResponseModel.getDatabaseName();
            final int regionId = libraryResponseModel.getRegionId();

            btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SOAPUtils.isNetworkConnected(AwardActivity.this)) {
                        if (!etSearchBook.getText().toString().isEmpty()) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put(URLConstants.PARAM_SEARCH, etSearchBook.getText().toString());
                            map.put(URLConstants.PARAM_ORG_ID, libraryId);
                            map.put(URLConstants.PARAM_REGION, regionId);
                            map.put(URLConstants.PARAM_DB_NAME, databaseName);
                            map.put("library", libraryResponseModel);
                            new LibraryTasks(URLConstants.ACTION_SEARCH_BOOK, map, AwardActivity.this).execute();
                        } else {
                            etSearchBook.setError(getString(R.string.cannot_be_empty));
                        }
                    } else {
                        Toast.makeText(AwardActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        if (getIntent().hasExtra("awards")) {
            ArrayList<AwardsModel> awardsModels = (ArrayList<AwardsModel>) getIntent().getExtras().get("awards");
            rvAwardsList.setAdapter(new AwardsListAdapter(AwardActivity.this, awardsModels, libraryId, databaseName, libraryResponseModel));
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
