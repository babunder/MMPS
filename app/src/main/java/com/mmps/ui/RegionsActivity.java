package com.mmps.ui;

import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mmps.R;
import com.mmps.adapters.RecyclerAdapter;
import com.mmps.listeners.RecyclerViewItemListener;
import com.mmps.models.SmartLibraryResponseModel;
import com.mmps.utils.SOAPUtils;
import com.mmps.utils.URLConstants;
import com.mmps.worker.LibraryTasks;

import java.util.ArrayList;
import java.util.HashMap;

public class RegionsActivity extends BaseActivity {

    private RecyclerView rvRegions;
    private ArrayList<SmartLibraryResponseModel> libraryResponseModels;
    private EditText etRegionSearchBook;
    private Button btnAllLibraries, btnRegionSearchBook;
    private Toolbar toolbar;
    HashMap<String, Object> map = new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regions);
        initToolbar();
        initui();
    }

    private void initui() {
        String book_name = null;
        if (getIntent().hasExtra("list") && getIntent().hasExtra("book_name")) {
            libraryResponseModels = (ArrayList<SmartLibraryResponseModel>) getIntent().getExtras().get("list");
            book_name = getIntent().getStringExtra("book_name");
        }
        etRegionSearchBook = findViewById(R.id.etRegionSearchBook);
        btnAllLibraries = findViewById(R.id.btnAllLibraries);
        btnRegionSearchBook = findViewById(R.id.btnRegionSearchBook);
        rvRegions = findViewById(R.id.rvRegions);
        rvRegions.setAdapter(new RecyclerAdapter(libraryResponseModels, this, book_name));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        rvRegions.setLayoutManager(gridLayoutManager);

        rvRegions.addOnItemTouchListener(new RecyclerViewItemListener(this, rvRegions, new RecyclerViewItemListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (SOAPUtils.isNetworkConnected(RegionsActivity.this)) {
                    map = new HashMap();
                    map.put(URLConstants.PARAM_REGION, libraryResponseModels.get(position).getSrNo());
                    new LibraryTasks(URLConstants.LIBRARY_ACTION, map, RegionsActivity.this).execute();
                } else {
                    Toast.makeText(RegionsActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        btnAllLibraries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SOAPUtils.isNetworkConnected(RegionsActivity.this)) {
                    map = new HashMap<>();
                    map.put(URLConstants.PARAM_REGION, 0);
                    new LibraryTasks(URLConstants.LIBRARY_ACTION, map, RegionsActivity.this).execute();
                } else {
                    Toast.makeText(RegionsActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                }
            }
        });

        btnRegionSearchBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SOAPUtils.isNetworkConnected(RegionsActivity.this)) {
                    if (!etRegionSearchBook.getText().toString().isEmpty()) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put(URLConstants.PARAM_SEARCH, etRegionSearchBook.getText().toString());
                        new LibraryTasks(URLConstants.SEARCH_ACTION, map, RegionsActivity.this)
                                .execute();
                    } else {
                        etRegionSearchBook.setError(getString(R.string.cannot_be_empty));
                    }
                } else {
                    Toast.makeText(RegionsActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toobar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_back);
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
