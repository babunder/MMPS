package com.mmps.ui;

import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.mmps.R;
import com.mmps.adapters.RecyclerAdapter;
import com.mmps.models.SmartLibraryResponseModel;

import java.util.ArrayList;

public class RegionSpecificLibrariesActivity extends BaseActivity {

    private RecyclerView rvRegionLibraries;
    private ArrayList<SmartLibraryResponseModel> libraryResponseModels;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_specific_libraries);
        initToolbar();

        String book_name = null;
        if (getIntent().hasExtra("list") && getIntent().hasExtra("book_name")) {
            libraryResponseModels = (ArrayList<SmartLibraryResponseModel>) getIntent().getExtras().get("list");
            book_name = getIntent().getStringExtra("book_name");
        }
        rvRegionLibraries = findViewById(R.id.rvRegionLibraries);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvRegionLibraries.getContext(),
                new LinearLayoutManager(this).getOrientation());
        rvRegionLibraries.addItemDecoration(dividerItemDecoration);
        rvRegionLibraries.setLayoutManager(new LinearLayoutManager(this));
        rvRegionLibraries.setAdapter(new RecyclerAdapter(libraryResponseModels, this, book_name));
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
