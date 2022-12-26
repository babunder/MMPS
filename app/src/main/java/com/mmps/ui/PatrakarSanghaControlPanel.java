package com.mmps.ui;

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
import com.mmps.adapters.RecyclerAdapter;
import com.mmps.listeners.RecyclerViewItemListener;
import com.mmps.models.SmartLibraryResponseModel;
import com.mmps.models.StatisticsModel;
import com.mmps.utils.ActionTypes;
import com.mmps.utils.SOAPUtils;
import com.mmps.utils.URLConstants;
import com.mmps.worker.LibraryTasks;

import java.util.ArrayList;
import java.util.HashMap;

public class PatrakarSanghaControlPanel extends BaseActivity {

    private Toolbar toolbar;
    private RecyclerView rvPatrakarSangha;
    private SmartLibraryResponseModel libraryResponseModel;
    private TextView title;
    private EditText etSearchBook;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patrakar_sangha_control_panel);

        initToolbar();
        initui();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toobar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
    }

    private void initui() {
        title = toolbar.findViewById(R.id.tv_title);
        title.setSelected(true);
        etSearchBook = findViewById(R.id.etSeachBook);
        btnSearch = findViewById(R.id.btnSearch);

        final String[] strings = {
                getString(R.string.history),
                getString(R.string.opinion_of_dignitaries)
        };

        final int[] iconIds = new int[]{
                R.drawable.ic_itihas,
                R.drawable.ic_opinion_of_dignitories
        };

        final ArrayList<StatisticsModel> statisticsModels = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            StatisticsModel model = new StatisticsModel();
            model.setTitle(strings[i]);
            model.setIconId(iconIds[i]);
            model.setType(ActionTypes.TYPE_STATISTICS);
            statisticsModels.add(model);
        }

        rvPatrakarSangha = findViewById(R.id.rvPatrakarSangha);
        rvPatrakarSangha.setLayoutManager(new LinearLayoutManager(PatrakarSanghaControlPanel.this));
        rvPatrakarSangha.setAdapter(new RecyclerAdapter(statisticsModels, PatrakarSanghaControlPanel.this, true, null));

        rvPatrakarSangha.addOnItemTouchListener(new RecyclerViewItemListener(PatrakarSanghaControlPanel.this,
                rvPatrakarSangha, new RecyclerViewItemListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (SOAPUtils.isNetworkConnected(PatrakarSanghaControlPanel.this)) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("LibraryId", libraryResponseModel.getSrNo());
                    map.put("DatabaseName", libraryResponseModel.getDatabaseName());
                    if (strings[position].equalsIgnoreCase(getString(R.string.history))) {
                        map.put("library", libraryResponseModel);
                        new LibraryTasks(URLConstants.HISTORY_ACTION, map, PatrakarSanghaControlPanel.this).execute();
                    }
                    if (strings[position].equalsIgnoreCase(getString(R.string.opinion_of_dignitaries))) {
                        map.put("library", libraryResponseModel);
                        new LibraryTasks(URLConstants.COMPLEMENTS_ACTION, map, PatrakarSanghaControlPanel.this).execute();
                    }
                } else {
                    Toast.makeText(PatrakarSanghaControlPanel.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        if (getIntent().hasExtra("library")) {
            libraryResponseModel = (SmartLibraryResponseModel) getIntent().getExtras().get("library");
            title.setText(libraryResponseModel.getLibraryName());
            final int libraryId = libraryResponseModel.getLibraryId();
            final String databaseName = libraryResponseModel.getDatabaseName();
            final int regionId = libraryResponseModel.getRegionId();
            btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SOAPUtils.isNetworkConnected(PatrakarSanghaControlPanel.this)) {
                        if (!etSearchBook.getText().toString().isEmpty()) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put(URLConstants.PARAM_SEARCH, etSearchBook.getText().toString());
                            map.put(URLConstants.PARAM_ORG_ID, libraryId);
                            map.put(URLConstants.PARAM_REGION, regionId);
                            map.put(URLConstants.PARAM_DB_NAME, databaseName);
                            map.put("library", libraryResponseModel);
                            new LibraryTasks(URLConstants.ACTION_SEARCH_BOOK, map, PatrakarSanghaControlPanel.this).execute();
                        } else {
                            etSearchBook.setError(getString(R.string.cannot_be_empty));
                        }
                    } else {
                        Toast.makeText(PatrakarSanghaControlPanel.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return false;
    }
}
