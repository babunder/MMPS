package com.mmps.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mmps.R;
import com.mmps.models.SmartLibraryResponseModel;
import com.mmps.utils.ProgressDialogUtil;
import com.mmps.utils.SOAPUtils;
import com.mmps.utils.URLConnectionUtil;
import com.mmps.utils.URLConstants;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.HashMap;

public class AboutUsActivity extends BaseActivity {

    private Toolbar toolbar;
    private ImageView ivLibraryLogo;
    private TextView tvLibraryAboutUS, tvFinancialYear, title, tvLibraryAddress1, tvLibraryAddress2, tvLibraryMCity, tvLibraryPin, tvLibraryContact;
    private String aboutUs = "";
    private SmartLibraryResponseModel libraryResponseModel;
    private int srno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initToolbar();

        initui();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toobar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_back);
    }

    private void initui() {
        title = toolbar.findViewById(R.id.tv_title);
        tvLibraryAboutUS = findViewById(R.id.tvLibraryAboutUs);
        tvLibraryAboutUS.setMovementMethod(new ScrollingMovementMethod());
        ivLibraryLogo = findViewById(R.id.ivLibraryLogo);
        tvLibraryAddress1 = findViewById(R.id.tvLibraryAddress1);
        tvLibraryAddress2 = findViewById(R.id.tvLibraryAddress2);
        tvLibraryMCity = findViewById(R.id.tvLibraryMCity);
        tvLibraryPin = findViewById(R.id.tvLibraryPin);
        tvLibraryContact = findViewById(R.id.tvLibraryContact);
        tvFinancialYear = findViewById(R.id.tvLibraryFinancialYear);
        if (getIntent().hasExtra("library")) {
            libraryResponseModel = (SmartLibraryResponseModel) getIntent().getExtras().get("library");
            title.setText(libraryResponseModel.getLibraryName() + " - " + getString(R.string.about_us));
            try {
                Glide.with(this).load(libraryResponseModel.getLogoImage()).into(ivLibraryLogo);
                tvLibraryAddress1.setText(libraryResponseModel.getAddress1());
                tvLibraryAddress2.setText(libraryResponseModel.getAddress2());
                tvLibraryMCity.setText(libraryResponseModel.getM_CityName());
                tvLibraryPin.setText(" - " + libraryResponseModel.getPIN());
                String contact = String.format(getResources().getString(R.string.contact), libraryResponseModel.getPhoneNo1());
                tvLibraryContact.setText(contact);
                srno = libraryResponseModel.getLibraryId();
                if (SOAPUtils.isNetworkConnected(AboutUsActivity.this)) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put(URLConstants.PARAM_ORG_ID, libraryResponseModel.getSrNo());
                    map.put(URLConstants.PARAM_DB_NAME, libraryResponseModel.getDatabaseName());
                    new GetAboutUsTask(URLConstants.ABOUT_US_ACTION, map, this).execute();
                } else {
                    Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class GetAboutUsTask extends AsyncTask<Void, Void, JSONArray> {

        private String ACTION;
        private HashMap<String, Object> map;
        private ProgressDialog progressDialog;
        private Context context;
        private String TAG = getClass().getSimpleName();
        private JSONArray resultJSONArray;
        private int code;

        public GetAboutUsTask(String ACTION, HashMap<String, Object> map, Context context) {
            this.ACTION = ACTION;
            this.map = map;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialogUtil.config(context);
        }

        @Override
        protected JSONArray doInBackground(Void... voids) {
            HttpURLConnection urlConnection;
            OutputStream outputStream;
            InputStream inputStream;
            code = -1;

            try {
                urlConnection = URLConnectionUtil.config(URLConstants.ABOUT_US_ACTION, map);
                urlConnection.connect();
                outputStream = urlConnection.getOutputStream();
                if (outputStream != null) {
                    outputStream.write(SOAPUtils.getData(ACTION, map));
                    outputStream.flush();
                }
                code = urlConnection.getResponseCode();
                Log.e(TAG + " response code", String.valueOf(code));

                inputStream = urlConnection.getInputStream();
                StringBuilder builder = new StringBuilder();
                String line = null;
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }
                String response = builder.toString();
                Log.e(TAG, response);
                resultJSONArray = new JSONArray(response);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return resultJSONArray;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            String financialYear = "";
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (jsonArray != null) {
                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        if(i == 1){
                            financialYear = jsonArray.getJSONObject(i).getString("Financial Year");
                        }
                        if(i > 1) {
                            if (srno == jsonArray.getJSONObject(i).getInt("LibraryId")) {
                                aboutUs += jsonArray.getJSONObject(i).getString("AboutUsNote") + "\n";
                            }
                        }
                    }
                    tvLibraryAboutUS.setText(aboutUs);
                    tvFinancialYear.setText(financialYear);
                } catch (Exception e) {

                }
            } else if (code == 200) {
                Toast.makeText(context, context.getString(R.string.nothing_to_show), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, context.getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
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
