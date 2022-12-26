package com.mmps.ui;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mmps.R;
import com.mmps.adapters.RecyclerAdapter;
import com.mmps.models.SmartLibraryResponseModel;
import com.mmps.utils.ActionTypes;
import com.mmps.utils.SOAPUtils;
import com.mmps.utils.URLConstants;
import com.mmps.worker.LibraryTasks;

import java.util.ArrayList;
import java.util.HashMap;

public class HistoryActivity extends BaseActivity {

    private Toolbar toolbar;
    private SmartLibraryResponseModel libraryResponseModel;
    private ArrayList<SmartLibraryResponseModel> libraryResponseModels;
    private RecyclerView rvhistory;
    private String history = "", headline;
    private ImageView ivLibraryLogo;
    private TextView title; /*tvHistory, tvHeadline, tvFinancialYear, tvLibraryAddress1, tvLibraryAddress2, tvLibraryMCity, tvLibraryPin, tvLibraryContact*/;
    private EditText etSearchBook;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

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
        etSearchBook = findViewById(R.id.etSeachBook);
        btnSearch = findViewById(R.id.btnSearch);
        rvhistory = findViewById(R.id.rvHistory);
        rvhistory.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
        /*tvHeadline = findViewById(R.id.tvHeadline);
        ivLibraryLogo = findViewById(R.id.ivLibraryLogo);
        tvLibraryAddress1 = findViewById(R.id.tvLibraryAddress1);
        tvLibraryAddress2 = findViewById(R.id.tvLibraryAddress2);
        tvLibraryMCity = findViewById(R.id.tvLibraryMCity);
        tvLibraryPin = findViewById(R.id.tvLibraryPin);
        tvLibraryContact = findViewById(R.id.tvLibraryContact);
        tvHistory = findViewById(R.id.tvHistory);
        tvHistory.setMovementMethod(new ScrollingMovementMethod());
        tvFinancialYear = findViewById(R.id.tvLibraryFinancialYear);*/

        if (getIntent().hasExtra("library") && getIntent().hasExtra("list")) {
            Log.e("History", String.valueOf(getIntent().hasExtra("list")));
            libraryResponseModel = (SmartLibraryResponseModel) getIntent().getExtras().get("library");
            libraryResponseModels = (ArrayList<SmartLibraryResponseModel>) getIntent().getExtras().get("list");
            int libraryId1 = libraryResponseModel.getLibraryId();
            //title.setText(libraryResponseModel.getLibraryName() + " - " + getString(R.string.history));
            title.setText(getString(R.string.history));
            final int libraryId = libraryResponseModel.getLibraryId();
            final String databaseName = libraryResponseModel.getDatabaseName();
            final int regionId = libraryResponseModel.getRegionId();
            try {
               /* Glide.with(this).load(libraryResponseModel.getLogoImage()).into(ivLibraryLogo);
                tvLibraryAddress1.setText(libraryResponseModel.getAddress1());
                tvLibraryAddress2.setText(libraryResponseModel.getAddress2());
                tvLibraryMCity.setText(libraryResponseModel.getM_CityName());
                tvLibraryPin.setText(" - " + libraryResponseModel.getPIN());
                String contact = String.format(getResources().getString(R.string.contact), libraryResponseModel.getPhoneNo1());
                tvLibraryContact.setText(contact);
                tvFinancialYear.setText(libraryResponseModel.getFinancialYear());*/

                btnSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (SOAPUtils.isNetworkConnected(HistoryActivity.this)) {
                            if (!etSearchBook.getText().toString().isEmpty()) {
                                HashMap<String, Object> map = new HashMap<>();
                                map.put(URLConstants.PARAM_SEARCH, etSearchBook.getText().toString());
                                map.put(URLConstants.PARAM_ORG_ID, libraryId);
                                map.put(URLConstants.PARAM_REGION, regionId);
                                map.put(URLConstants.PARAM_DB_NAME, databaseName);
                                map.put("library", libraryResponseModel);
                                new LibraryTasks(URLConstants.ACTION_SEARCH_BOOK, map, HistoryActivity.this).execute();
                            } else {
                                etSearchBook.setError(getString(R.string.cannot_be_empty));
                            }
                        } else {
                            Toast.makeText(HistoryActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                        }
                    }
                });
                ArrayList<SmartLibraryResponseModel> historyModels = new ArrayList<>();
                for (int i = 0; i < libraryResponseModels.size(); i++) {
                    if (libraryId == libraryResponseModels.get(i).getLibraryId()) {
                        SmartLibraryResponseModel smartLibraryResponseModel = new SmartLibraryResponseModel();
                        /*Log.e("Library id", libraryResponseModel.getLibraryId() + libraryResponseModels.get(i).getLibraryId() + "");
                        headline = libraryResponseModels.get(i).getHeadline();
                        history += libraryResponseModels.get(i).getHistory() + "\n";*/
                        if(libraryResponseModels.get(i).getHeadline().equalsIgnoreCase("null") || libraryResponseModels.get(i).getHeadline() == null){
                            smartLibraryResponseModel.setHeadline("");
                        }else{
                            smartLibraryResponseModel.setHeadline(libraryResponseModels.get(i).getHeadline());
                        }
                        smartLibraryResponseModel.setHistory(libraryResponseModels.get(i).getHistory());
                        smartLibraryResponseModel.setType(ActionTypes.TYPE_HISTORY);
                        historyModels.add(smartLibraryResponseModel);
                    }
                }

                rvhistory.setAdapter(new RecyclerAdapter(historyModels, HistoryActivity.this, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }
}
