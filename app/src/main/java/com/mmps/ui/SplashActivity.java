package com.mmps.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mmps.R;
import com.mmps.models.SmartLibraryResponseModel;
import com.mmps.models.YouTubeModel;
import com.mmps.utils.ActionTypes;
import com.mmps.utils.SOAPUtils;
import com.mmps.utils.URLConnectionUtil;
import com.mmps.utils.URLConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class SplashActivity extends BaseActivity {

    private Handler handler;
    private Runnable runnable;
    private TextView tvAddress1, tvAddress2, tvMCity, tvLibraryName, tvLibraryEst;
    private ProgressBar pbLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tvAddress1 = findViewById(R.id.tvLibraryAddress1);
        tvLibraryName= findViewById(R.id.tvLibraryName);
        tvLibraryEst= findViewById(R.id.tvLibraryEst);
        tvAddress2 = findViewById(R.id.tvLibraryAddress2);
        tvMCity = findViewById(R.id.tvLibraryMCity);
        pbLoading = findViewById(R.id.pb_loading);

        HashMap<String, Object> map = new HashMap<>();
        map.put(URLConstants.PARAM_ORG_ID, 5);
        new SplashTask(URLConstants.LIBRARY_ACTION, map).execute();
    }

    private class SplashTask extends AsyncTask<Void, Void, JSONArray> {
        String ACTION;
        HashMap<String, Object> map;
        String TAG = getClass().getSimpleName();
        int code;
        Intent intent;
        JSONArray resultJSONArray;
        SmartLibraryResponseModel model;
        ArrayList<SmartLibraryResponseModel> libraryResponseModels;

        SplashTask(String ACTION, HashMap<String, Object> map){
            this.ACTION = ACTION;
            this.map = map;
        }

        @Override
        protected void onPreExecute() {
            pbLoading.setVisibility(View.VISIBLE);
        }


        @Override
        protected JSONArray doInBackground(Void... voids) {
            HttpURLConnection urlConnection;
            OutputStream outputStream;
            InputStream inputStream;
            code = -1;

            try {
                urlConnection = URLConnectionUtil.config(ACTION, map);
                urlConnection.connect();
                outputStream = urlConnection.getOutputStream();
                if (outputStream != null) {
                    outputStream.write(SOAPUtils.getData(ACTION, map));
                }
                outputStream.flush();
                code = urlConnection.getResponseCode();
                if (code == 200) {
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
                    return resultJSONArray;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            JSONObject object;
            pbLoading.setVisibility(View.GONE);
            if(jsonArray != null){
                try {
                    libraryResponseModels = new ArrayList();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        model = new SmartLibraryResponseModel();
                        object = jsonArray.getJSONObject(i);
                        model.setSrNo(object.getInt("SrNo"));
                        model.setLibraryId(object.getInt("SrNo"));
                        model.setLogoImage(URLConstants.COMPANY_BASE_URL + "CP/Uploads/LibraryInfo/" + object.getString("LogoImage"));
                        model.setLibraryName(object.getString("Organization"));
                        model.setEstablishmentYear(object.getInt("EstablishYear"));
                        model.setDatabaseName("MMPS");
                        model.setAddress1(object.getString("Address1"));
                        model.setAddress2(object.getString("Address2"));
                        model.setWebsite(object.getString("Website"));
                        model.setM_CityName(object.getString("City"));
                        model.setPIN(object.getString("PIN"));
                        model.setPhoneNo1(object.getString("PhoneNo1"));
                        model.setType(ActionTypes.TYPE_REGION_LIBRARIES);
                        libraryResponseModels.add(model);
                    }
                    tvLibraryName.setText(model.getLibraryName());
                    tvLibraryEst.setText(String.format("%s %s", getString(R.string.established), NumberFormat.getInstance().format(model.getEstablishmentYear()).replace(",", "")));
                    tvAddress1.setText(model.getAddress1());
                    tvAddress2.setText(model.getAddress2());
                    int pin = Integer.parseInt(model.getPIN());
                    tvMCity.setText(String.format("%s - %s", model.getM_CityName(), NumberFormat.getInstance().format(pin).replace(",", "")));
                    intent = new Intent(SplashActivity.this, NavigationControlPanelActivity.class);
                    intent.putExtra("library", model);
                    intent.putExtra("book_name", "");
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}


