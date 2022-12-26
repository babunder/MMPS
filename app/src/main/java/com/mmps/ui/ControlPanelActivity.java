package com.mmps.ui;

import android.content.Intent;
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

public class ControlPanelActivity extends BaseActivity {

    private Toolbar toolbar;
    private SmartLibraryResponseModel libraryResponseModel;
    //private ImageView ivLibraryLogo;
    private TextView title;/*, tvFinancialYear, tvLibraryAddress1, tvLibraryAddress2, tvLibraryMCity, tvLibraryPin, tvLibraryContact;*/
    private EditText etSearchBook;
    private Button btnSearch;
    private RecyclerView rvContaolPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);
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
        /*ivLibraryLogo = findViewById(R.id.ivLibraryLogo);
        tvLibraryAddress1 = findViewById(R.id.tvLibraryAddress1);
        tvLibraryAddress2 = findViewById(R.id.tvLibraryAddress2);
        tvLibraryMCity = findViewById(R.id.tvLibraryMCity);
        tvLibraryPin = findViewById(R.id.tvLibraryPin);
        tvLibraryContact = findViewById(R.id.tvLibraryContact);
        tvFinancialYear = findViewById(R.id.tvLibraryFinancialYear);*/

        final String[] strings = {
                getString(R.string.new_books),
                getString(R.string.rare_books),
                getString(R.string.numerology),
        };

        final int[] iconIds = new int[]{
                R.drawable.ic_readers_choice,
                R.drawable.ic_readers_choice,
                R.drawable.ic_readers_choice,

        };

        final ArrayList<StatisticsModel> libraryResponseModels = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            StatisticsModel model = new StatisticsModel();
            model.setTitle(strings[i]);
            model.setIconId(iconIds[i]);
            model.setType(ActionTypes.TYPE_STATISTICS);
            libraryResponseModels.add(model);
        }

        rvContaolPanel = findViewById(R.id.rvControlPanel);
        rvContaolPanel.setLayoutManager(new LinearLayoutManager(ControlPanelActivity.this));
        rvContaolPanel.setAdapter(new RecyclerAdapter(libraryResponseModels, ControlPanelActivity.this, true, null));

        if (getIntent().hasExtra("library")) {
            libraryResponseModel = (SmartLibraryResponseModel) getIntent().getExtras().get("library");
            title.setText(libraryResponseModel.getLibraryName());
            final int libraryId = libraryResponseModel.getLibraryId();
            final String databaseName = libraryResponseModel.getDatabaseName();
            final int regionId = libraryResponseModel.getRegionId();
            try {
                /*Glide.with(this).load(libraryResponseModel.getLogoImage()).into(ivLibraryLogo);
                tvLibraryAddress1.setText(libraryResponseModel.getAddress1());
                tvLibraryAddress2.setText(libraryResponseModel.getAddress2());
                tvLibraryMCity.setText(libraryResponseModel.getM_CityName());
                tvLibraryPin.setText(" - " + libraryResponseModel.getPIN());
                String contact = String.format(getResources().getString(R.string.contact), libraryResponseModel.getPhoneNo1());
                tvLibraryContact.setText(contact);*/

                btnSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (SOAPUtils.isNetworkConnected(ControlPanelActivity.this)) {
                            if (!etSearchBook.getText().toString().isEmpty()) {
                                HashMap<String, Object> map = new HashMap<>();
                                map.put(URLConstants.PARAM_SEARCH, etSearchBook.getText().toString());
                                map.put(URLConstants.PARAM_ORG_ID, libraryId);
                                map.put(URLConstants.PARAM_REGION, regionId);
                                map.put(URLConstants.PARAM_DB_NAME, databaseName);
                                map.put("library", libraryResponseModel);
                                new LibraryTasks(URLConstants.ACTION_SEARCH_BOOK, map, ControlPanelActivity.this).execute();
                            } else {
                                etSearchBook.setError(getString(R.string.cannot_be_empty));
                            }
                        } else {
                            Toast.makeText(ControlPanelActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                        }
                    }
                });

                rvContaolPanel.addOnItemTouchListener(new RecyclerViewItemListener(ControlPanelActivity.this,
                        rvContaolPanel, new RecyclerViewItemListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (SOAPUtils.isNetworkConnected(ControlPanelActivity.this)) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("LibraryId", libraryResponseModel.getSrNo());
                            map.put("DatabaseName", libraryResponseModel.getDatabaseName());
                            if (strings[position].equalsIgnoreCase(getString(R.string.new_books))) {
                                map.put("library", libraryResponseModel);
                                new LibraryTasks(URLConstants.NEW_BOOKS_ACTION, map, ControlPanelActivity.this).execute();
                            }
                            if (strings[position].equalsIgnoreCase(getString(R.string.rare_books))) {
                                map.put("library", libraryResponseModel);
                                new LibraryTasks(URLConstants.RARE_BOOKS_ACTION, map, ControlPanelActivity.this).execute();
                            }
                            if (strings[position].equalsIgnoreCase(getString(R.string.numerology))) {
                                Intent intent = new Intent(ControlPanelActivity.this, StatisticsActivity.class);
                                intent.putExtra("library", libraryResponseModel);
                                startActivity(intent);
                            }
                            if (strings[position].equalsIgnoreCase(getString(R.string.my_membership))) {
                                Intent intent = new Intent(ControlPanelActivity.this, MyMembershipActivity.class);
                                intent.putExtra("library", libraryResponseModel);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(ControlPanelActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));
            } catch (Exception e) {
                e.printStackTrace();
            }
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
