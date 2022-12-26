package com.mmps.ui;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mmps.R;
import com.mmps.models.BookModel;
import com.mmps.models.SmartLibraryResponseModel;
import com.mmps.utils.SOAPUtils;
import com.mmps.utils.TableRowTextViewUtil;
import com.mmps.utils.URLConstants;
import com.mmps.worker.LibraryTasks;

import java.util.ArrayList;
import java.util.HashMap;

public class TypeWiseCountActivity extends BaseActivity {

    private Toolbar toolbar;
    private SmartLibraryResponseModel libraryResponseModel;
    private ImageView ivLibraryLogo;
    private TextView tvSrNo, tvBookType, tvBookCount, title, tvFinancialYear, tvLibraryAddress1, tvLibraryAddress2, tvLibraryMCity, tvLibraryPin, tvLibraryContact;
    private EditText etSearchBook;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_wise_count);
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
        ivLibraryLogo = findViewById(R.id.ivLibraryLogo);
        tvLibraryAddress1 = findViewById(R.id.tvLibraryAddress1);
        tvLibraryAddress2 = findViewById(R.id.tvLibraryAddress2);
        tvLibraryMCity = findViewById(R.id.tvLibraryMCity);
        tvLibraryPin = findViewById(R.id.tvLibraryPin);
        tvLibraryContact = findViewById(R.id.tvLibraryContact);
        tvFinancialYear = findViewById(R.id.tvLibraryFinancialYear);

        if (getIntent().hasExtra("library")) {
            libraryResponseModel = (SmartLibraryResponseModel) getIntent().getExtras().get("library");
            title.setText(getString(R.string.type_wise_reading));
            final int libraryId = libraryResponseModel.getLibraryId();
            final int yearId = libraryResponseModel.getFinancialYearId();
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

                if (getIntent().hasExtra("types")) {
                    ArrayList<BookModel> bookModels = (ArrayList<BookModel>) getIntent().getExtras().get("types");
                    for (int i = 0; i < bookModels.size(); i++) {
                        TableLayout table = findViewById(R.id.table);
                        TableRow row = new TableRow(this);

                        tvBookType = new TableRowTextViewUtil().config(this, R.color.colorPrimary, bookModels.get(i).getBookType());
                        tvSrNo = new TableRowTextViewUtil().config(this, R.color.black, String.valueOf(i + 1));
                        tvSrNo.setGravity(Gravity.END);
                        tvBookType.setGravity(Gravity.START);
                        tvBookType.setPadding(16, 8, 8, 8);
                        tvBookCount = new TableRowTextViewUtil().config(this, R.color.black, String.valueOf(bookModels.get(i).getBookCount()));
                        tvBookCount.setPadding(0,0,16,0);
                        tvBookCount.setGravity(Gravity.END);

                        row.addView(tvSrNo);
                        row.addView(tvBookType);
                        row.addView(tvBookCount);
                        table.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                    }

                }

                btnSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (SOAPUtils.isNetworkConnected(TypeWiseCountActivity.this)) {
                            if (!etSearchBook.getText().toString().isEmpty()) {
                                HashMap<String, Object> map = new HashMap<>();
                                map.put(URLConstants.PARAM_SEARCH, etSearchBook.getText().toString());
                                map.put(URLConstants.PARAM_ORG_ID, libraryId);
                                map.put(URLConstants.PARAM_REGION, regionId);
                                map.put(URLConstants.PARAM_DB_NAME, databaseName);
                                map.put("library", libraryResponseModel);
                                new LibraryTasks(URLConstants.ACTION_SEARCH_BOOK, map, TypeWiseCountActivity.this).execute();
                            } else {
                                etSearchBook.setError(getString(R.string.cannot_be_empty));
                            }
                        } else {
                            Toast.makeText(TypeWiseCountActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                        }
                    }
                });

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
