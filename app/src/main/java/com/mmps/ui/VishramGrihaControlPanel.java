package com.mmps.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mmps.R;
import com.mmps.adapters.RecyclerAdapter;
import com.mmps.models.RestHouseModel;
import com.mmps.models.SmartLibraryResponseModel;
import com.mmps.utils.SOAPUtils;
import com.mmps.utils.URLConstants;
import com.mmps.worker.LibraryTasks;

import java.util.ArrayList;
import java.util.HashMap;

public class VishramGrihaControlPanel extends BaseActivity {

	private Toolbar toolbar;
	private RecyclerView rvVishramGriha;
	private SmartLibraryResponseModel libraryResponseModel;
	private TextView title;
	private EditText etSearchBook;
	private Button btnSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vishram_griha_control_panel);
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
		title.setText(getString(R.string.rest_house));
		title.setSelected(true);
		etSearchBook = findViewById(R.id.etSeachBook);
		btnSearch = findViewById(R.id.btnSearch);

		if (getIntent().hasExtra("restHouses")) {
			ArrayList<RestHouseModel> restHouseModels = (ArrayList<RestHouseModel>) getIntent().getExtras().get("restHouses");
			rvVishramGriha = findViewById(R.id.rvVishramGriha);
			rvVishramGriha.setLayoutManager(new LinearLayoutManager(VishramGrihaControlPanel.this));
			rvVishramGriha.setAdapter(new RecyclerAdapter(restHouseModels, VishramGrihaControlPanel.this, true, new RecyclerAdapter.VishramgrihaClickListener() {
				@Override
				public void onVishramgrihaClicked(RestHouseModel restHouseModel) {
					HashMap<String, Object> map = new HashMap();
					map.put("RestHouseId", restHouseModel.getSrNo());
					map.put("LibraryId", libraryResponseModel.getLibraryId());
					map.put("DatabaseName", libraryResponseModel.getDatabaseName());
					map.put("library", libraryResponseModel);
					map.put("restHouse", restHouseModel);
					new LibraryTasks(URLConstants.ACTION_GET_ROOMS, (HashMap<String, Object>) map, VishramGrihaControlPanel.this).execute();
				}
			}));
		}

		if (getIntent().hasExtra("library")) {
			libraryResponseModel = (SmartLibraryResponseModel) getIntent().getExtras().get("library");
			final int libraryId = libraryResponseModel.getLibraryId();
			final String databaseName = libraryResponseModel.getDatabaseName();
			final int regionId = libraryResponseModel.getRegionId();
			btnSearch.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if (SOAPUtils.isNetworkConnected(VishramGrihaControlPanel.this)) {
						if (!etSearchBook.getText().toString().isEmpty()) {
							HashMap<String, Object> map = new HashMap<>();
							map.put("SearchParam", etSearchBook.getText().toString());
							map.put("LibraryId", libraryId);
							map.put("RegionId", regionId);
							map.put("DatabaseName", databaseName);
							map.put("library", libraryResponseModel);
							new LibraryTasks(URLConstants.ACTION_SEARCH_BOOK, map, VishramGrihaControlPanel.this).execute();
						} else {
							etSearchBook.setError(getString(R.string.cannot_be_empty));
						}
					} else {
						Toast.makeText(VishramGrihaControlPanel.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
					}
				}
			});
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
