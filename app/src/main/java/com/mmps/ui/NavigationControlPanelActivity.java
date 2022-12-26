package com.mmps.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.mmps.R;
import com.mmps.adapters.RecyclerAdapter;
import com.mmps.listeners.RecyclerViewItemListener;
import com.mmps.models.SmartLibraryResponseModel;
import com.mmps.utils.ActionTypes;
import com.mmps.utils.SOAPUtils;
import com.mmps.utils.URLConstants;
import com.mmps.worker.LibraryTasks;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class NavigationControlPanelActivity extends BaseActivity
		implements NavigationView.OnNavigationItemSelectedListener {

	private Toolbar toolbar;
	//private ImageButton imgBtnSearch;
	//private CardView rlSearchBooks;
	private NavigationView navigationView;
	private SmartLibraryResponseModel libraryResponseModel;
	//private ImageView ivLibraryLogo;
	private TextView title, tvLibraryName, tvLibraryEst, tvAddress1, tvAddress2;/*, tvFinancialYear, tvLibraryContact;*/
	private EditText etSearchBook;
	private Button btnSearch;
	private RecyclerView rvContaolPanel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
		setContentView(R.layout.activity_navigation_control_panel);
		initToolbar();
		initui();
	}

	private void initToolbar() {
		toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("");
		//imgBtnSearch = toolbar.findViewById(R.id.imgBtnSearch);
		//rlSearchBooks = findViewById(R.id.rlSearchBooks);

        /*imgBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rlSearchBooks.getVisibility() == View.GONE) {
                    rlSearchBooks.setVisibility(View.VISIBLE);
                }else if(rlSearchBooks.getVisibility() == View.VISIBLE){
                    rlSearchBooks.setVisibility(View.GONE);
                }
            }
        });*/
	}

	private void initui() {

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();

		navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

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
		tvLibraryName = navigationView.getHeaderView(0).findViewById(R.id.tvLibraryName);
		tvLibraryEst = navigationView.getHeaderView(0).findViewById(R.id.tvLibraryEst);
		tvAddress1 = navigationView.getHeaderView(0).findViewById(R.id.tvLibraryAddress1);
		tvAddress2 = navigationView.getHeaderView(0).findViewById(R.id.tvLibraryAddress2);

		final String[] strings = {
				getString(R.string.patrakar_sangha),
				getString(R.string.management),
				getString(R.string.social_projects),
				getString(R.string.syllabus),
				getString(R.string.awards),
				getString(R.string.videos),
				getString(R.string.library),
				getString(R.string.rest_house)
		};

        /*final int[] iconIds = new int[]{
                R.drawable.ic_patrakar_sangha,
                R.drawable.ic_mgmt_body,
                R.drawable.ic_upakram,
                R.drawable.ic_abhyaskram,
                R.drawable.ic_awards,
                R.drawable.ic_videos,
                R.drawable.ic_rest_house,
                R.drawable.ic_library
        };*/

		final int[] iconIds = new int[]{
				R.drawable.ic_new_patrakar_sangh,
				R.drawable.ic_new_mgmt_body,
				R.drawable.ic_new_social_services,
				R.drawable.ic_new_syllsbus,
				R.drawable.ic_new_awards,
				R.drawable.ic_new_pictures,
				R.drawable.ic_new_library,
				R.drawable.ic_new_guest_house
		};

		final ArrayList<SmartLibraryResponseModel> libraryResponseModels = new ArrayList<>();
		for (int i = 0; i < 8; i++) {
			SmartLibraryResponseModel model = new SmartLibraryResponseModel();
			model.setRegionName(strings[i]);
			model.setIconId(iconIds[i]);
			model.setType(ActionTypes.TYPE_NAV_CONTROL_PANEL);
			libraryResponseModels.add(model);
		}

		rvContaolPanel = findViewById(R.id.rvControlPanel);
		GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
		rvContaolPanel.setLayoutManager(gridLayoutManager);
		rvContaolPanel.setAdapter(new RecyclerAdapter(libraryResponseModels, NavigationControlPanelActivity.this, ""));

		if (getIntent().hasExtra("library")) {
			libraryResponseModel = (SmartLibraryResponseModel) getIntent().getExtras().get("library");
			title.setText(libraryResponseModel.getLibraryName());
			final int libraryId = libraryResponseModel.getLibraryId();
			final String databaseName = libraryResponseModel.getDatabaseName();
			final int regionId = libraryResponseModel.getRegionId();
			try {
				tvLibraryName.setText(libraryResponseModel.getLibraryName());
				tvLibraryEst.setText(String.format("%s %s", getString(R.string.established), NumberFormat.getInstance().format(libraryResponseModel.getEstablishmentYear()).replace(",", "")));
				tvAddress1.setText(libraryResponseModel.getAddress1());
				tvAddress2.setText(String.format("%s %s", libraryResponseModel.getAddress2(), libraryResponseModel.getM_CityName() + "\n" + libraryResponseModel.getPIN()));
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
						if (SOAPUtils.isNetworkConnected(NavigationControlPanelActivity.this)) {
							if (!etSearchBook.getText().toString().isEmpty()) {
								HashMap<String, Object> map = new HashMap<>();
								map.put(URLConstants.PARAM_SEARCH, etSearchBook.getText().toString());
								map.put(URLConstants.PARAM_ORG_ID, libraryId);
								map.put(URLConstants.PARAM_REGION, regionId);
								map.put(URLConstants.PARAM_DB_NAME, databaseName);
								map.put("library", libraryResponseModel);
								new LibraryTasks(URLConstants.ACTION_SEARCH_BOOK, map, NavigationControlPanelActivity.this).execute();
							} else {
								etSearchBook.setError(getString(R.string.cannot_be_empty));
							}
						} else {
							Toast.makeText(NavigationControlPanelActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
						}
					}
				});

				rvContaolPanel.addOnItemTouchListener(new RecyclerViewItemListener(NavigationControlPanelActivity.this,
						rvContaolPanel, new RecyclerViewItemListener.OnItemClickListener() {
					@Override
					public void onItemClick(View view, int position) {
						if (SOAPUtils.isNetworkConnected(NavigationControlPanelActivity.this)) {
							HashMap<String, Object> map = new HashMap<>();
							map.put(URLConstants.PARAM_ORG_ID, libraryResponseModel.getSrNo());
							map.put(URLConstants.PARAM_DB_NAME, libraryResponseModel.getDatabaseName());
							if (strings[position].equalsIgnoreCase(getString(R.string.library))) {
								Intent intent = new Intent(NavigationControlPanelActivity.this, ControlPanelActivity.class);
								intent.putExtra("library", libraryResponseModel);
								startActivity(intent);
							}
							if (strings[position].equalsIgnoreCase(getString(R.string.management))) {
								map.put("library", libraryResponseModel);
								new LibraryTasks(URLConstants.MANAGEMENT_ACTION, map, NavigationControlPanelActivity.this).execute();
							}
							if (strings[position].equalsIgnoreCase(getString(R.string.social_projects))) {
								map.put("library", libraryResponseModel);
								new LibraryTasks(URLConstants.SOCIAL_PROJECT_ACTION, map, NavigationControlPanelActivity.this).execute();
							}
							if (strings[position].equalsIgnoreCase(getString(R.string.patrakar_sangha))) {
								Intent intent = new Intent(NavigationControlPanelActivity.this, PatrakarSanghaControlPanel.class);
								intent.putExtra("library", libraryResponseModel);
								startActivity(intent);
							}
							if (strings[position].equalsIgnoreCase(getString(R.string.rest_house))) {
								getRestHouses(libraryResponseModel);
							}
							if (strings[position].equalsIgnoreCase(getString(R.string.videos))) {
								map.put("library", libraryResponseModel);
								new LibraryTasks(URLConstants.ACTION_YOUTUBE_LINKS, map, NavigationControlPanelActivity.this).execute();
                                /*Intent intent = new Intent(NavigationControlPanelActivity.this, VideosActivity.class);
                                intent.putExtra("library", libraryResponseModel);
                                startActivity(intent);*/
							}
							if (strings[position].equalsIgnoreCase(getString(R.string.awards))) {
								map.put("library", libraryResponseModel);
								map.put(URLConstants.PARAM_ORG_ID, libraryId);
								map.put(URLConstants.PARAM_DB_NAME, libraryResponseModel.getDatabaseName());
								new LibraryTasks(URLConstants.AWARD_LIST_ACTION, map, NavigationControlPanelActivity.this).execute();
							}
							if (strings[position].equalsIgnoreCase(getString(R.string.syllabus))) {
								map.put("library", libraryResponseModel);
								map.put(URLConstants.PARAM_ORG_ID, libraryId);
								map.put(URLConstants.PARAM_DB_NAME, libraryResponseModel.getDatabaseName());
								new LibraryTasks(URLConstants.SYLLABUS_ACTION, map, NavigationControlPanelActivity.this).execute();
							}
                            /*if (strings[position].equalsIgnoreCase(getString(R.string.rare_books))) {
                                map.put("library", libraryResponseModel);
                                new LibraryTasks(URLConstants.RARE_BOOKS_ACTION, map, NavigationControlPanelActivity.this).execute();
                            }
                            if (strings[position].equalsIgnoreCase(getString(R.string.numerology))) {
                                Intent intent = new Intent(NavigationControlPanelActivity.this, StatisticsActivity.class);
                                intent.putExtra("library", libraryResponseModel);
                                startActivity(intent);
                            }
                            if (strings[position].equalsIgnoreCase(getString(R.string.my_membership))) {
                                Intent intent = new Intent(NavigationControlPanelActivity.this, MyMembershipActivity.class);
                                intent.putExtra("library", libraryResponseModel);
                                startActivity(intent);
                            }*/
						} else {
							Toast.makeText(NavigationControlPanelActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
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

	private void getRestHouses(SmartLibraryResponseModel libraryResponseModel){
		HashMap<String, Object> map = new HashMap<>();
		map.put("RestHouseId", 5);
		map.put("DatabaseName", "MMPS");
		map.put("library", libraryResponseModel);
		new LibraryTasks(URLConstants.ACTION_GET_RESTHOUSES, map, NavigationControlPanelActivity.this).execute();
	}

	@SuppressLint("NewApi")
	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			finishAffinity();
		}
	}

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_control_panel, menu);
        return true;
    }*/

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();
        /*if (id == R.id.patrakarita) {
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}
}
