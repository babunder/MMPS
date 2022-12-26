package com.mmps.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mmps.R;
import com.mmps.adapters.RecyclerAdapter;
import com.mmps.models.RestHouseModel;
import com.mmps.models.RoomModel;
import com.mmps.models.SmartLibraryResponseModel;
import com.mmps.utils.SOAPUtils;
import com.mmps.utils.URLConstants;
import com.mmps.worker.LibraryTasks;

import java.util.ArrayList;
import java.util.HashMap;

public class RoomsActivity extends BaseActivity {

	private Toolbar toolbar;
	private RecyclerView rvRooms;
	private SmartLibraryResponseModel libraryResponseModel;
	private TextView title, tvRestHouseName, tvRestHouseAddress;
	private EditText etSearchBook;
	private Button btnSearch;
	private ImageView ivRestHouseLogo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rooms);

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
		title.setText(getString(R.string.vishramgriha_sabhagriha_nondni));
		title.setSelected(true);
		etSearchBook = findViewById(R.id.etSeachBook);
		btnSearch = findViewById(R.id.btnSearch);

		tvRestHouseAddress = findViewById(R.id.tv_rest_house_address);
		tvRestHouseName = findViewById(R.id.tv_rest_house_name);
		ivRestHouseLogo = findViewById(R.id.ic_rest_house_logo);

		if(getIntent().hasExtra("restHouse")){
			RestHouseModel restHouseModel = (RestHouseModel) getIntent().getExtras().get("restHouse");
			tvRestHouseName.setText(restHouseModel.getHouseName());
			tvRestHouseAddress.setText(restHouseModel.getAddress1() + " " + restHouseModel.getAddress2() + "\n" + restHouseModel.getCity() + " PIN - " + restHouseModel.getPin());
			Glide.with(this).load(restHouseModel.getLogoImage()).into(ivRestHouseLogo);
		}

		if (getIntent().hasExtra("rooms")) {
			ArrayList<RoomModel> roomModels = (ArrayList<RoomModel>) getIntent().getExtras().get("rooms");
			Log.e("TAG", "initui: rooms size: " + roomModels.size());
			rvRooms = findViewById(R.id.rv_rooms);
			rvRooms.setLayoutManager(new LinearLayoutManager(RoomsActivity.this));
			rvRooms.setAdapter(new RecyclerAdapter(roomModels, RoomsActivity.this, 0, 0));
		}

		if (getIntent().hasExtra("library")) {
			libraryResponseModel = (SmartLibraryResponseModel) getIntent().getExtras().get("library");
			final int libraryId = libraryResponseModel.getLibraryId();
			final String databaseName = libraryResponseModel.getDatabaseName();
			final int regionId = libraryResponseModel.getRegionId();
			btnSearch.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if (SOAPUtils.isNetworkConnected(RoomsActivity.this)) {
						if (!etSearchBook.getText().toString().isEmpty()) {
							HashMap<String, Object> map = new HashMap<>();
							map.put("SearchParam", etSearchBook.getText().toString());
							map.put("LibraryId", libraryId);
							map.put("RegionId", regionId);
							map.put("DatabaseName", databaseName);
							map.put("library", libraryResponseModel);
							new LibraryTasks(URLConstants.ACTION_SEARCH_BOOK, map, RoomsActivity.this).execute();
						} else {
							etSearchBook.setError(getString(R.string.cannot_be_empty));
						}
					} else {
						Toast.makeText(RoomsActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
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