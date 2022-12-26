package com.mmps.ui;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

public class StatisticsActivity extends BaseActivity {

    private Toolbar toolbar;
    private SmartLibraryResponseModel libraryResponseModel;
    private ArrayList<SmartLibraryResponseModel> libraryResponseModels;
    private ArrayList<StatisticsModel> statisticsModels;
    private ImageView ivLibraryLogo;
    private TextView title, tvFinancialYear, tvLibraryAddress1, tvLibraryAddress2, tvLibraryMCity, tvLibraryPin, tvLibraryContact;
    private EditText etSearchBook;
    private Button btnSearch;
    private RecyclerView rvStatistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
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
        statisticsModels = new ArrayList<>();
        ivLibraryLogo = findViewById(R.id.ivLibraryLogo);
        tvLibraryAddress1 = findViewById(R.id.tvLibraryAddress1);
        tvLibraryAddress2 = findViewById(R.id.tvLibraryAddress2);
        tvLibraryMCity = findViewById(R.id.tvLibraryMCity);
        tvLibraryPin = findViewById(R.id.tvLibraryPin);
        tvLibraryContact = findViewById(R.id.tvLibraryContact);
        rvStatistics = findViewById(R.id.rvStatistics);
        rvStatistics.setLayoutManager(new LinearLayoutManager(this));
        tvFinancialYear = findViewById(R.id.tvLibraryFinancialYear);

        if (getIntent().hasExtra("library")) {
            libraryResponseModel = (SmartLibraryResponseModel) getIntent().getExtras().get("library");
            //title.setText(libraryResponseModel.getLibraryName() + " - " + getString(R.string.numerology));
            title.setText(getString(R.string.numerology));
            final int libraryId = libraryResponseModel.getLibraryId();
            final String databaseName = libraryResponseModel.getDatabaseName();
            final int regionId = libraryResponseModel.getRegionId();
            try {
                Glide.with(this).load(libraryResponseModel.getLogoImage()).into(ivLibraryLogo);
                tvLibraryAddress1.setText(libraryResponseModel.getAddress1());
                tvLibraryAddress2.setText(libraryResponseModel.getAddress2());
                tvLibraryMCity.setText(libraryResponseModel.getM_CityName());
                tvLibraryPin.setText(" - " + libraryResponseModel.getPIN());
                String contact = String.format(getResources().getString(R.string.contact), libraryResponseModel.getPhoneNo1());
                tvLibraryContact.setText(contact);
                tvFinancialYear.setText(libraryResponseModel.getFinancialYear());

                btnSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (SOAPUtils.isNetworkConnected(StatisticsActivity.this)) {
                            if (!etSearchBook.getText().toString().isEmpty()) {
                                HashMap<String, Object> map = new HashMap<>();
                                map.put(URLConstants.PARAM_SEARCH, etSearchBook.getText().toString());
                                map.put(URLConstants.PARAM_ORG_ID, libraryId);
                                map.put(URLConstants.PARAM_REGION, regionId);
                                map.put(URLConstants.PARAM_DB_NAME, databaseName);
                                map.put("library", libraryResponseModel);
                                new LibraryTasks(URLConstants.ACTION_SEARCH_BOOK, map, StatisticsActivity.this).execute();
                            } else {
                                etSearchBook.setError(getString(R.string.cannot_be_empty));
                            }
                        } else {
                            Toast.makeText(StatisticsActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                        }
                    }
                });

                final String[] strings = new String[]{
                        getString(R.string.granthsampada),
                        getString(R.string.current_year_book_purchase),
                        getString(R.string.type_wise_reading),
                        getString(R.string.popular_10_books),
                        getString(R.string.top_10_readers),
                        getString(R.string.membership_schemes),
                        getString(R.string.member)
                };

                /*final int[] iconIds = new int[]{
                        R.drawable.ic_granthsampada,
                        R.drawable.ic_granth_kharedi,
                        R.drawable.ic_popular_book_type,
                        R.drawable.ic_10_popular_books,
                        R.drawable.ic_10_readers,
                        R.drawable.ic_member_schemes,
                        R.drawable.ic_member
                };*/

                final int[] iconIds = new int[]{
                        R.drawable.ic_granthsampada,
                        R.drawable.ic_current_year_purchase,
                        R.drawable.ic_readers_choice,
                        R.drawable.ic_popular_10_books,
                        R.drawable.ic_top_10_readers,
                        R.drawable.ic_membership_scheme,
                        R.drawable.ic_member
                };

                for(int i = 0; i < 6; i ++){
                    StatisticsModel statisticsModel = new StatisticsModel();
                    statisticsModel.setTitle(strings[i]);
                    statisticsModel.setIconId(iconIds[i]);
                    statisticsModel.setType(ActionTypes.TYPE_STATISTICS);
                    statisticsModels.add(statisticsModel);
                }

                RecyclerAdapter adapter = new RecyclerAdapter(statisticsModels,this, true, null);

                rvStatistics.setAdapter(adapter);

                //ArrayAdapter<String> adapter = new ArrayAdapter<>(StatisticsActivity.this, R.layout.layout_list_item_row, R.id.text1, strings);
                //lvStatistics.setAdapter(adapter);

                rvStatistics.addOnItemTouchListener(new RecyclerViewItemListener(StatisticsActivity.this, rvStatistics, new RecyclerViewItemListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (SOAPUtils.isNetworkConnected(StatisticsActivity.this)) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("LibraryId", libraryResponseModel.getSrNo());
                            map.put("DatabaseName", libraryResponseModel.getDatabaseName());
                            if (strings[position].equalsIgnoreCase(getString(R.string.granthsampada))) {
                                map.put("library", libraryResponseModel);
                                new LibraryTasks(URLConstants.BOOK_STATISTICS_ACTION, map, StatisticsActivity.this).execute();
                            }
                            if (strings[position].equalsIgnoreCase(getString(R.string.type_wise_reading))) {
                                map.put("library", libraryResponseModel);
                                map.put(URLConstants.PARAM_FINANCIAL_YEAR_ID, libraryResponseModel.getFinancialYearId());
                                new LibraryTasks(URLConstants.ACTION_TYPE_WISE_READING, map, StatisticsActivity.this).execute();
                            }
                            if (strings[position].equalsIgnoreCase(getString(R.string.popular_10_books))) {
                                map.put("library", libraryResponseModel);
                                map.put(URLConstants.PARAM_FINANCIAL_YEAR_ID, libraryResponseModel.getFinancialYearId());
                                new LibraryTasks(URLConstants.READERS_CHOICE_ACTION, map, StatisticsActivity.this).execute();
                            }
                            if (strings[position].equalsIgnoreCase(getString(R.string.top_10_readers))) {
                                map.put("library", libraryResponseModel);
                                map.put(URLConstants.PARAM_FINANCIAL_YEAR_ID, libraryResponseModel.getFinancialYearId());
                                new LibraryTasks(URLConstants.ACTION_TOP_10_READERS, map, StatisticsActivity.this).execute();
                            }
                            if (strings[position].equalsIgnoreCase(getString(R.string.membership_schemes))) {
                                map.put("library", libraryResponseModel);
                                new LibraryTasks(URLConstants.ACTION_MEMBERSHIP, map, StatisticsActivity.this).execute();
                            }
                            if (strings[position].equalsIgnoreCase(getString(R.string.member))) {
                                map.put("library", libraryResponseModel);
                                new LibraryTasks(URLConstants.ACTION_MEMBERSHIP_STATISTICS, map, StatisticsActivity.this).execute();
                            }
                            if (strings[position].equalsIgnoreCase(getString(R.string.current_year_book_purchase))) {
                                map.put("library", libraryResponseModel);
                                new LibraryTasks(URLConstants.ACTION_TYPE_PURCHASE, map, StatisticsActivity.this).execute();
                            }
                        } else {
                            Toast.makeText(StatisticsActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
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
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }
}
