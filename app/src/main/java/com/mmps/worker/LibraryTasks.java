package com.mmps.worker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.mmps.R;
import com.mmps.models.AwardsModel;
import com.mmps.models.BookModel;
import com.mmps.models.ComplementModel;
import com.mmps.models.CourseModel;
import com.mmps.models.ManagementBodyModel;
import com.mmps.models.MembershipModel;
import com.mmps.models.ObjectiveModel;
import com.mmps.models.RestHouseModel;
import com.mmps.models.RoomModel;
import com.mmps.models.SmartLibraryResponseModel;
import com.mmps.models.SocialProjectModel;
import com.mmps.models.SubjectModel;
import com.mmps.models.YouTubeModel;
import com.mmps.ui.AwardActivity;
import com.mmps.ui.AwardInfoActivity;
import com.mmps.ui.BookResultActivity;
import com.mmps.ui.ComplementsActivity;
import com.mmps.ui.CourseActivity;
import com.mmps.ui.GranthSampadaActivity;
import com.mmps.ui.HistoryActivity;
import com.mmps.ui.ManagementBodyActivity;
import com.mmps.ui.MemberActivity;
import com.mmps.ui.MembershipPlansActivity;
import com.mmps.ui.MyMembershipActivity;
import com.mmps.ui.NavigationControlPanelActivity;
import com.mmps.ui.NewBooksActivity;
import com.mmps.ui.ObjectiveActivity;
import com.mmps.ui.ReadersChoiceActivity;
import com.mmps.ui.RegionsActivity;
import com.mmps.ui.RoomsActivity;
import com.mmps.ui.SeachResultActivity;
import com.mmps.ui.SocialProjectActivity;
import com.mmps.ui.SubjectsActivity;
import com.mmps.ui.TopReadersActivity;
import com.mmps.ui.TypeWiseCountActivity;
import com.mmps.ui.TypeWisePurchaseActivity;
import com.mmps.ui.VideosActivity;
import com.mmps.ui.VishramGrihaControlPanel;
import com.mmps.utils.ActionTypes;
import com.mmps.utils.ProgressDialogUtil;
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
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Aditya Kulkarni
 */

public class LibraryTasks extends AsyncTask<Void, Void, JSONArray> {

    private String ACTION;
    private HashMap<String, Object> map;
    private ProgressDialog progressDialog;
    private Context context;
    private String TAG = getClass().getSimpleName();
    private JSONArray resultJSONArray;
    private ArrayList<SmartLibraryResponseModel> libraryResponseModels;
    private ArrayList<ManagementBodyModel> managementBodyModels;
    private ArrayList<SocialProjectModel> socialProjectModels;
    private ArrayList<ComplementModel> complementModels;
    private ArrayList<BookModel> bookModels;
    private ArrayList<MembershipModel> membershipModels;
    private ArrayList<YouTubeModel> youTubeModels;
    private ArrayList<AwardsModel> awardsModels;
    private ArrayList<CourseModel> courseModels;
    private ArrayList<SubjectModel> subjectModels;
    private ArrayList<ObjectiveModel> objectiveModels;
    private ArrayList<RestHouseModel> restHouseModels;
    private ArrayList<RoomModel> roomsModels;
    private int code;

    public LibraryTasks(String ACTION, HashMap<String, Object> map, Context context) {
        this.ACTION = ACTION;
        this.map = map;
        this.context = context;
        this.progressDialog = ProgressDialogUtil.config(context);
    }

    @Override
    protected void onPreExecute() {
        if (!(this.ACTION.equalsIgnoreCase(URLConstants.LIBRARY_ACTION))) {
            progressDialog.show();
        }
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
            Log.e(TAG + " response code", String.valueOf(code));
            if (code == 200) {
                inputStream = urlConnection.getInputStream();
                StringBuilder builder = new StringBuilder();
                String line = null;
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }
                String response = builder.toString();

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
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (jsonArray != null) {
            Log.e(TAG, jsonArray.toString());
            Intent intent = null;
            SmartLibraryResponseModel model = null;
            JSONObject object = null;
            try {
                switch (ACTION) {
                    case "http://patrakarsangh.in/SmartSearch":
                        libraryResponseModels = new ArrayList();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            model = new SmartLibraryResponseModel();
                            object = jsonArray.getJSONObject(i);
                            model.setLogoImage("http://www.tantraved.in/CP/Uploads/LibraryInfo/" + object.getString("LogoImage"));
                            model.setLibraryId(object.getInt("LibraryId"));
                            model.setDatabaseName(object.getString("DatabaseName"));
                            model.setLibraryName(object.getString("LibraryName"));
                            model.setResultCount(object.getInt("ResultCount"));
                            model.setType(ActionTypes.TYPE_SMART_SEARCH);
                            libraryResponseModels.add(model);
                        }
                        intent = new Intent(context, SeachResultActivity.class);
                        intent.putExtra("book_name", map.get("SearchParam").toString());
                        break;
                    case "http://patrakarsangh.in/GetRegions":
                        libraryResponseModels = new ArrayList();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            model = new SmartLibraryResponseModel();
                            object = jsonArray.getJSONObject(i);
                            model.setSrNo(object.getInt("SrNo"));
                            model.setRegionName(object.getString("RegionName"));
                            model.setDescription(object.getString("Description"));
                            model.setCreatedBy(object.getString("CreatedBy"));
                            model.setCreatedDate(object.getString("CreatedDate"));
                            //model.setDatabaseName(object.getString("DatabaseName"));
                            model.setLastModifiedBy(object.getString("LastModifiedBy"));
                            model.setLastModifiedDate(object.getString("LastModifiedDate"));
                            model.setType(ActionTypes.TYPE_REGIONS);
                            libraryResponseModels.add(model);
                        }
                        intent = new Intent(context, RegionsActivity.class);
                        intent.putExtra("book_name", "");
                        break;
                    case "http://patrakarsangh.in/GetSmartLibraries":
                        libraryResponseModels = new ArrayList();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            model = new SmartLibraryResponseModel();
                            object = jsonArray.getJSONObject(i);
                            model.setSrNo(object.getInt("SrNo"));
                            model.setLibraryId(object.getInt("LibraryId"));
                            model.setLogoImage(URLConstants.COMPANY_BASE_URL + "CP/Uploads/LibraryInfo/" + object.getString("LogoImage"));
                            model.setLibraryName(object.getString("LibraryName"));
                            model.setAddress1(object.getString("Address1"));
                            model.setAddress2(object.getString("Address2"));
                            model.setRegionId(object.getInt("RegionId"));
                            model.setWebsite(object.getString("Website"));
                            model.setDatabaseName(object.getString("DatabaseName"));
                            model.setM_CityName(object.getString("M_CityName"));
                            model.setPIN(object.getString("PIN"));
                            model.setPhoneNo1(object.getString("PhoneNo1"));
                            model.setType(ActionTypes.TYPE_REGION_LIBRARIES);
                            libraryResponseModels.add(model);
                        }
                        intent = new Intent(context, NavigationControlPanelActivity.class);
                        intent.putExtra("library", model);
                        intent.putExtra("book_name", "");

                        break;

                    case "http://patrakarsangh.in/GetHistory":
                        libraryResponseModels = new ArrayList();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            model = new SmartLibraryResponseModel();
                            object = jsonArray.getJSONObject(i);
                            if (i == 0) {
                                model = (SmartLibraryResponseModel) map.get("library");
                                object = jsonArray.getJSONObject(i);
                                model.setFinancialYear(object.getString("Financial Year"));
                            }
                            if (i > 0) {
                                model.setSrNo(object.getInt("SrNo"));
                                model.setHeadline(object.getString("Headline"));
                                model.setLibraryId(object.getInt("LibraryId"));
                                model.setHistory(object.getString("History"));
                                libraryResponseModels.add(model);
                            }
                        }
                        intent = new Intent(context, HistoryActivity.class);
                        intent.putExtra("library", (SmartLibraryResponseModel) map.get("library"));
                        break;
                    case "http://patrakarsangh.in/GetManagementBody":
                        managementBodyModels = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ManagementBodyModel bodyModel = new ManagementBodyModel();
                            object = jsonArray.getJSONObject(i);
                            if (i == 00) {
                                model = (SmartLibraryResponseModel) map.get("library");
                                object = jsonArray.getJSONObject(i);
                                model.setFinancialYear(object.getString("Financial Year"));
                            }
                            if (i > 0) {
                                bodyModel.setSrNo(object.getInt("SrNo"));
                                bodyModel.setLibraryId(object.getInt("LibraryId"));
                                bodyModel.setTitle(object.getString("Title"));
                                bodyModel.setFirstName(object.getString("FirstName"));
                                bodyModel.setMiddleName(object.getString("MiddleName"));
                                bodyModel.setLastName(object.getString("LastName"));
                                bodyModel.setManagementBodyName(object.getString("ManagementBodyName"));
                                bodyModel.setGender(object.getInt("Gender"));
                                bodyModel.setWebsite(model.getWebsite());
                                bodyModel.setProfileIdentity(object.getString("ProfileIdentity"));
                                bodyModel.setDateOfBirth(object.getString("DateOfBirth"));
                                bodyModel.setMobileNo1(object.getString("MobileNo1"));
                                bodyModel.setMobileNo2(object.getString("MobileNo2"));
                                bodyModel.setFromDate(object.getString("FromDate"));
                                bodyModel.setToDate(object.getString("ToDate"));
                                bodyModel.setLogoImage(object.getString("LogoImage"));
                                bodyModel.setIsContactPerson(object.getBoolean("IsContactPerson"));
                                bodyModel.setType(ActionTypes.TYPE_MANAGEMENT_BODY);
                                managementBodyModels.add(bodyModel);
                            }
                        }
                        intent = new Intent(context, ManagementBodyActivity.class);
                        intent.putExtra("management", managementBodyModels);
                        intent.putExtra("library", (SmartLibraryResponseModel) map.get("library"));
                        break;
                    case "http://patrakarsangh.in/GetSocialProjects":
                        socialProjectModels = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            SocialProjectModel socialProjectModel = new SocialProjectModel();
                            object = jsonArray.getJSONObject(i);
                            if (i == 0) {
                                model = (SmartLibraryResponseModel) map.get("library");
                                object = jsonArray.getJSONObject(i);
                                model.setFinancialYear(object.getString("Financial Year"));
                            }
                            if (i > 0) {
                                socialProjectModel.setSrNo(object.getInt("SrNo"));
                                socialProjectModel.setLibraryId(object.getInt("LibraryId"));
                                socialProjectModel.setProjectTitle(object.getString("ProjectTitle"));
                                socialProjectModel.setProjectDate(object.getString("ProjectDate"));
                                socialProjectModel.setShortDesc(object.getString("ShortDesc"));
                                socialProjectModel.setLongDesc(object.getString("LongDesc"));
                                socialProjectModel.setWebsite(model.getWebsite());
                                socialProjectModel.setPhotoImage(object.getString("PhotoImage"));
                                socialProjectModel.setShowPromo(object.getBoolean("ShowPromo"));
                                socialProjectModel.setCreatedBy(object.getString("CreatedBy"));
                                socialProjectModel.setCreatedDate(object.getString("CreatedDate"));
                                socialProjectModel.setLastModifiedBy(object.getString("LastModifiedBy"));
                                socialProjectModel.setLastModifiedDate(object.getString("LastModifiedDate"));
                                socialProjectModel.setIsActive(object.getBoolean("IsActive"));
                                socialProjectModel.setType(ActionTypes.TYPE_SOCIAL_PROJECTS);
                                socialProjectModels.add(socialProjectModel);
                            }
                        }
                        intent = new Intent(context, SocialProjectActivity.class);
                        intent.putExtra("social", socialProjectModels);
                        intent.putExtra("library", (SmartLibraryResponseModel) map.get("library"));
                        break;
                    case "http://patrakarsangh.in/GetComplements":
                        complementModels = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ComplementModel complementModel = new ComplementModel();
                            object = jsonArray.getJSONObject(i);
                            if (i == 0) {
                                model = (SmartLibraryResponseModel) map.get("library");
                                object = jsonArray.getJSONObject(i);
                                model.setFinancialYear(object.getString("Financial Year"));
                            }
                            if (i > 0) {
                                complementModel.setSrNo(object.getInt("SrNo"));
                                complementModel.setLibraryId(object.getInt("LibraryId"));
                                complementModel.setComplementDate(object.getString("ComplementDate"));
                                complementModel.setComplementsBy(object.getString("ComplementsBy"));
                                complementModel.setAboutThePerson(object.getString("AboutThePerson"));
                                complementModel.setDetails(object.getString("Details"));
                                complementModel.setScannedCopy(object.getString("ScannedCopy"));
                                complementModel.setDescription(object.getString("Description"));
                                complementModel.setCreatedBy(object.getString("CreatedBy"));
                                complementModel.setCreatedDate(object.getString("CreatedDate"));
                                complementModel.setLastModifiedBy(object.getString("LastModifiedBy"));
                                complementModel.setLastModifiedDate(object.getString("LastModifiedDate"));
                                complementModel.setType(ActionTypes.TYPE_COMPLEMENTS);
                                complementModels.add(complementModel);
                            }
                        }
                        intent = new Intent(context, ComplementsActivity.class);
                        intent.putExtra("complements", complementModels);
                        intent.putExtra("library", (SmartLibraryResponseModel) map.get("library"));
                        break;
                    case "http://patrakarsangh.in/GetNewBooks":
                        bookModels = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            BookModel bookModel = new BookModel();
                            object = jsonArray.getJSONObject(i);
                            if (i == 0) {
                                model = (SmartLibraryResponseModel) map.get("library");
                                object = jsonArray.getJSONObject(i);
                                model.setFinancialYear(object.getString("Financial Year"));
                            }
                            if (i > 0) {
                                bookModel.setLibraryId(object.getInt("LibraryId"));
                                bookModel.setAuthor(object.getString("Author"));
                                bookModel.setBookImage(object.getString("BookImage"));
                                bookModel.setAvailable(object.getBoolean("IsAvailable"));
                                bookModel.setBookType(object.getString("BookType"));
                                bookModel.setBookInwardNo(object.getString("BookInwardNo"));
                                bookModel.setBookNo(object.getString("BookNo"));
                                bookModel.setBookTitle(object.getString("BookTitle"));
                                bookModel.setPublisherName(object.getString("PublisherName"));
                                bookModel.setType(ActionTypes.TYPE_NEW_BOOKS);
                                bookModels.add(bookModel);
                            }
                        }
                        intent = new Intent(context, NewBooksActivity.class);
                        intent.putExtra("library", (SmartLibraryResponseModel) map.get("library"));
                        intent.putExtra("books", bookModels);
                        break;
                    case "http://patrakarsangh.in/GetRareBooks":
                        bookModels = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            BookModel bookModel = new BookModel();
                            object = jsonArray.getJSONObject(i);
                            if (i == 0) {
                                model = (SmartLibraryResponseModel) map.get("library");
                                object = jsonArray.getJSONObject(i);
                                model.setFinancialYear(object.getString("Financial Year"));
                            }
                            if (i > 0) {
                                bookModel.setLibraryId(object.getInt("LibraryId"));
                                bookModel.setAuthor(object.getString("Author"));
                                bookModel.setBookImage(object.getString("BookImage"));
                                bookModel.setBookType(object.getString("BookType"));
                                bookModel.setBookInwardNo(object.getString("BookInwardNo"));
                                bookModel.setBookNo(object.getString("BookNo"));
                                bookModel.setBookTitle(object.getString("BookTitle"));
                                bookModel.setPublisherName(object.getString("PublisherName"));
                                bookModel.setType(ActionTypes.TYPE_RARE_BOOKS);
                                bookModels.add(bookModel);
                            }
                        }
                        intent = new Intent(context, NewBooksActivity.class);
                        intent.putExtra("library", (SmartLibraryResponseModel) map.get("library"));
                        intent.putExtra("books", bookModels);
                        break;
                    case "http://patrakarsangh.in/GetFinancialYear":
                        for (int i = 0; i < jsonArray.length(); i++) {
                            model = (SmartLibraryResponseModel) map.get("library");
                            object = jsonArray.getJSONObject(i);
                            model.setFinancialYearId(object.getInt("SrNo"));
                            model.setFomYear(object.getString("FromYear"));
                            model.setFinancialYear(object.getString("Financial Year"));
                        }
                        if (map.get("requiredAction").toString().equalsIgnoreCase(context.getString(R.string.type_wise_reading))) {
                            intent = new Intent(context, TypeWiseCountActivity.class);
                            intent.putExtra("library", model);
                        } else if (map.get("requiredAction").toString().equalsIgnoreCase(context.getString(R.string.top_10_readers))) {
                            intent = new Intent(context, TopReadersActivity.class);
                            intent.putExtra("library", model);
                        } else if (map.get("requiredAction").toString().equalsIgnoreCase(context.getString(R.string.popular_10_books))) {
                            intent = new Intent(context, ReadersChoiceActivity.class);
                            intent.putExtra("library", model);
                        } else {
                            intent = new Intent(context, MyMembershipActivity.class);
                            intent.putExtra("library", model);
                        }
                        break;
                    case "http://patrakarsangh.in/GetBookStatistics":
                        bookModels = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            BookModel bookModel = new BookModel();
                            object = jsonArray.getJSONObject(i);
                            if (i == 0) {
                                model = (SmartLibraryResponseModel) map.get("library");
                                object = jsonArray.getJSONObject(i);
                                model.setFinancialYear(object.getString("Financial Year"));
                            }
                            if (i > 0) {
                                bookModel.setBookType(object.getString("BookType"));
                                bookModel.setBookCount(object.getInt("BookCount"));
                                bookModel.setType(ActionTypes.TYPE_GRANTHSAMPADA);
                                bookModels.add(bookModel);
                            }
                        }
                        intent = new Intent(context, GranthSampadaActivity.class);
                        intent.putExtra("library", (SmartLibraryResponseModel) map.get("library"));
                        intent.putExtra("statistics", bookModels);
                        break;
                    case "http://patrakarsangh.in/GetReadersChoice":
                        bookModels = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            BookModel bookModel = new BookModel();
                            object = jsonArray.getJSONObject(i);
                            if (i == 0) {
                                model = (SmartLibraryResponseModel) map.get("library");
                                object = jsonArray.getJSONObject(i);
                                model.setFinancialYear(object.getString("Financial Year"));
                            }
                            if (i > 0) {

                                bookModel.setBookTitle(object.getString("BookTitle"));
                                bookModel.setBookInwardNo(object.getString("BookInwardNo"));
                                bookModel.setBookNo(object.getString("BookNo"));
                                bookModel.setBookImage(object.getString("BookImage"));
                                bookModel.setBookType(object.getString("BookType"));
                                bookModel.setAuthor(object.getString("Author"));
                                bookModel.setBookCount(object.getInt("BookCount"));
                                bookModel.setType(ActionTypes.TYPE_READERS_CHOICE);
                                bookModels.add(bookModel);
                            }
                        }
                        intent = new Intent(context, ReadersChoiceActivity.class);
                        intent.putExtra("library", (SmartLibraryResponseModel) map.get("library"));
                        intent.putExtra("books", bookModels);
                        break;
                    case "http://patrakarsangh.in/SearchBooksWithLibraryDetails":
                        bookModels = new ArrayList<>();
                        SmartLibraryResponseModel libraryResponseModel = new SmartLibraryResponseModel();
                        /*libraryResponseModel.setSrNo(jsonArray.getJSONObject(0).getInt("SrNo"));
                        libraryResponseModel.setLibraryId(jsonArray.getJSONObject(0).getInt("SrNo"));
                        libraryResponseModel.setLogoImage(URLConstants.COMPANY_BASE_URL + "CP/Uploads/LibraryInfo/" + jsonArray.getJSONObject(0).getString("LogoImage"));
                        libraryResponseModel.setLibraryName(jsonArray.getJSONObject(0).getString("LibraryName"));
                        libraryResponseModel.setAddress1(jsonArray.getJSONObject(0).getString("Address1"));
                        libraryResponseModel.setAddress2(jsonArray.getJSONObject(0).getString("Address2"));
                        libraryResponseModel.setDatabaseName(jsonArray.getJSONObject(0).getString("DatabaseName"));
                        libraryResponseModel.setM_CityName(jsonArray.getJSONObject(0).getString("M_CityName"));
                        libraryResponseModel.setPIN(jsonArray.getJSONObject(0).getString("PIN"));
                        libraryResponseModel.setPhoneNo1(jsonArray.getJSONObject(0).getString("PhoneNo1"));*/
                        for (int i = 0; i < jsonArray.length(); i++) {
                            BookModel bookModel = new BookModel();
                            object = jsonArray.getJSONObject(i);
                            if (i == 0) {
                                object = jsonArray.getJSONObject(i);
                                libraryResponseModel.setFinancialYear(object.getString("Financial Year"));
                            }
                            if (i != 0) {
                                libraryResponseModel.setSrNo(jsonArray.getJSONObject(i).getInt("SrNo"));
                                libraryResponseModel.setLibraryId(jsonArray.getJSONObject(i).getInt("SrNo"));
                                libraryResponseModel.setLibraryName(jsonArray.getJSONObject(i).getString("LibraryName"));
                                libraryResponseModel.setDatabaseName("MMPS");
                                bookModel.setLibraryId(object.getInt("LibraryId"));
                                bookModel.setAuthor(object.getString("Author"));
                                bookModel.setBookImage(object.getString("BookImage"));
                                bookModel.setBookType(object.getString("BookType"));
                                bookModel.setBookInwardNo(object.getString("BookInwardNo"));
                                bookModel.setBookNo(object.getString("BookNo"));
                                bookModel.setBookTitle(object.getString("BookTitle"));
                                bookModel.setPublisherName(object.getString("PublisherName"));
                                bookModel.setType(ActionTypes.TYPE_NEW_BOOKS);
                                bookModels.add(bookModel);
                            }
                        }
                        intent = new Intent(context, BookResultActivity.class);
                        intent.putExtra("library", libraryResponseModel);
                        intent.putExtra("books", bookModels);
                        break;
                    case "http://patrakarsangh.in/BookTypewiseReadingCount":
                        bookModels = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            BookModel bookModel = new BookModel();
                            object = jsonArray.getJSONObject(i);

                            if (i > 0) {
                                bookModel.setType(ActionTypes.TYPE_WISE_COUNT);
                                bookModel.setBookType(object.getString("BookType"));
                                bookModel.setBookCount(object.getInt("BookCount"));
                                bookModels.add(bookModel);
                            }
                        }
                        intent = new Intent(context, TypeWiseCountActivity.class);
                        intent.putExtra("library", (SmartLibraryResponseModel) map.get("library"));
                        intent.putExtra("types", bookModels);
                        break;
                    case "http://patrakarsangh.in/GetTop10Readers":
                        bookModels = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            BookModel bookModel = new BookModel();
                            object = jsonArray.getJSONObject(i);
                            if (i == 0) {
                                model = (SmartLibraryResponseModel) map.get("library");
                                object = jsonArray.getJSONObject(i);
                                model.setFinancialYear(object.getString("Financial Year"));
                            }
                            if (i > 0) {
                                bookModel.setMemberId(object.getInt("MemberId"));
                                bookModel.setMember(object.getString("Member"));
                                bookModel.setMemberImage(object.getString("MemberImage"));
                                bookModel.setUserName(object.getString("UserName"));
                                bookModel.setBookCount(object.getInt("BookCount"));
                                bookModel.setType(ActionTypes.TOP_10_READERS);
                                bookModels.add(bookModel);
                            }
                        }
                        intent = new Intent(context, TopReadersActivity.class);
                        intent.putExtra("library", (SmartLibraryResponseModel) map.get("library"));
                        intent.putExtra("readers", bookModels);
                        break;
                    case "http://patrakarsangh.in/GetMembershipPlans":
                        membershipModels = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (i == 0) {
                                model = (SmartLibraryResponseModel) map.get("library");
                                object = jsonArray.getJSONObject(i);
                                model.setFinancialYear(object.getString("Financial Year"));
                            }
                            if (i > 0) {
                                MembershipModel membershipModel = new MembershipModel();
                                object = jsonArray.getJSONObject(i);
                                membershipModel.setMembershipPlan(object.getString("MembershipPlan"));
                                membershipModel.setMaxBooks(object.getInt("MaxBooks"));
                                membershipModel.setMaxMagazines(object.getInt("MaxMagazines"));
                                membershipModel.setDuration(object.getInt("Duration"));
                                membershipModel.setType(ActionTypes.MEMBERSHIP_PLANS);
                                membershipModels.add(membershipModel);
                            }
                        }
                        intent = new Intent(context, MembershipPlansActivity.class);
                        intent.putExtra("plans", membershipModels);
                        intent.putExtra("library", (SmartLibraryResponseModel) map.get("library"));
                        break;

                    case "http://patrakarsangh.in/GetMemberStatistics":
                        membershipModels = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (i == 0) {
                                model = (SmartLibraryResponseModel) map.get("library");
                                object = jsonArray.getJSONObject(i);
                                model.setFinancialYear(object.getString("Financial Year"));
                            }
                            if (i > 0) {
                                MembershipModel membershipModel = new MembershipModel();
                                object = jsonArray.getJSONObject(i);
                                membershipModel.setMembershipPlan(object.getString("MembershipPlan"));
                                membershipModel.setMeberCount(object.getInt("MemberCount"));
                                membershipModel.setType(ActionTypes.MEMBER_STATISTICS);
                                membershipModels.add(membershipModel);
                            }
                        }
                        intent = new Intent(context, MemberActivity.class);
                        intent.putExtra("memberStatistics", membershipModels);
                        intent.putExtra("library", (SmartLibraryResponseModel) map.get("library"));
                        break;

                    case "http://patrakarsangh.in/GetBookTypewisePurchase":
                        bookModels = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            BookModel bookModel = new BookModel();
                            object = jsonArray.getJSONObject(i);
                            if (i == 0) {
                                model = (SmartLibraryResponseModel) map.get("library");
                                object = jsonArray.getJSONObject(i);
                                model.setFinancialYear(object.getString("Financial Year"));
                            }
                            if (i > 0) {
                                bookModel.setTotalAmount(object.getInt("TotalAmount"));
                                bookModel.setBookType(object.getString("BookType"));
                                bookModel.setBookCount(object.getInt("TotalBooks"));
                                bookModel.setType(ActionTypes.TYPE_WISE_PURCHASE);
                                bookModels.add(bookModel);
                            }
                        }
                        intent = new Intent(context, TypeWisePurchaseActivity.class);
                        intent.putExtra("library", (SmartLibraryResponseModel) map.get("library"));
                        intent.putExtra("purchase", bookModels);
                        break;

                    case "http://patrakarsangh.in/GetRandomYouTubeLink":
                        youTubeModels = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            YouTubeModel youTubeModel = new YouTubeModel();
                            object = jsonArray.getJSONObject(i);
                            if (i == 0) {
                                model = (SmartLibraryResponseModel) map.get("library");
                                object = jsonArray.getJSONObject(i);
                                model.setFinancialYear(object.getString("Financial Year"));
                            }
                            if (i > 0) {
                                youTubeModel.setTitle(object.getString("Title"));
                                youTubeModel.setDescription(object.getString("Description"));
                                youTubeModels.add(youTubeModel);
                            }
                        }
                        intent = new Intent(context, VideosActivity.class);
                        intent.putExtra("library", (SmartLibraryResponseModel) map.get("library"));
                        intent.putExtra("youtube", youTubeModels);
                        break;
                    case "http://patrakarsangh.in/GetAwards":
                        awardsModels = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            AwardsModel awardsModel = new AwardsModel();
                            object = jsonArray.getJSONObject(i);
                            if (i == 0) {
                                model = (SmartLibraryResponseModel) map.get("library");
                                object = jsonArray.getJSONObject(i);
                                model.setFinancialYear(object.getString("Financial Year"));
                            }
                            if (i > 0) {
                                awardsModel.setSrNo(object.getInt("SrNo"));
                                awardsModel.setOrganizationId(object.getInt("OrganizationId"));
                                awardsModel.setOrganization(object.getString("Organization"));
                                awardsModel.setAwardTitle(object.getString("AwardTitle"));
                                awardsModel.setAwardPrice(object.getString("AwardPrice"));
                                awardsModel.setAwardCriteria(object.getString("AwardCriteria"));
                                awardsModel.setDescription(object.getString("Description"));
                                awardsModel.setCreatedBy(object.getString("CreatedBy"));
                                awardsModel.setCreatedDate(object.getString("CreatedDate"));
                                awardsModel.setLastModifiedBy(object.getString("LastModifiedBy"));
                                awardsModel.setLastModifiedDate(object.getString("LastModifiedDate"));
                                awardsModel.setActive(object.getBoolean("IsActive"));
                                awardsModels.add(awardsModel);
                            }
                        }
                        intent = new Intent(context, AwardActivity.class);
                        intent.putExtra("library", (SmartLibraryResponseModel) map.get("library"));
                        intent.putExtra("awards", awardsModels);
                        break;

                    case "http://patrakarsangh.in/GetAwardWinners":
                        awardsModels = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            AwardsModel awardsModel = new AwardsModel();
                            object = jsonArray.getJSONObject(i);
                            if (i == 0) {
                                model = (SmartLibraryResponseModel) map.get("library");
                                object = jsonArray.getJSONObject(i);
                                model.setFinancialYear(object.getString("Financial Year"));
                            }

                            if (i == 1) {
                                awardsModel.setSrNo(object.getInt("SrNo"));
                                awardsModel.setOrganizationId(object.getInt("OrganizationId"));
                                awardsModel.setOrganization(object.getString("Organization"));
                                awardsModel.setAwardTitle(object.getString("AwardTitle"));
                                awardsModel.setAwardPrice(object.getString("AwardPrice"));
                                awardsModel.setAwardCriteria(object.getString("AwardCriteria"));
                                awardsModel.setDescription(object.getString("Description"));
                                awardsModel.setCreatedBy(object.getString("CreatedBy"));
                                awardsModel.setCreatedDate(object.getString("CreatedDate"));
                                awardsModel.setLastModifiedBy(object.getString("LastModifiedBy"));
                                awardsModel.setLastModifiedDate(object.getString("LastModifiedDate"));
                                awardsModel.setActive(object.getBoolean("IsActive"));
                                awardsModels.add(awardsModel);
                            }

                            if (i > 1) {
                                awardsModel.setSrNo(object.getInt("SrNo"));
                                awardsModel.setAwardId(object.getInt("AwardId"));
                                awardsModel.setAwardWinner(object.getString("AwardWinner"));
                                awardsModel.setPhotoImage(object.getString("PhotoImage"));
                                awardsModel.setAwardYear(object.getString("AwardYear"));
                                awardsModel.setDescription(object.getString("Description"));
                                awardsModel.setCreatedBy(object.getString("CreatedBy"));
                                awardsModel.setCreatedDate(object.getString("CreatedDate"));
                                awardsModel.setLastModifiedBy(object.getString("LastModifiedBy"));
                                awardsModel.setLastModifiedDate(object.getString("LastModifiedDate"));
                                awardsModel.setActive(object.getBoolean("IsActive"));
                                awardsModels.add(awardsModel);
                            }
                        }
                        intent = new Intent(context, AwardInfoActivity.class);
                        intent.putExtra("library", (SmartLibraryResponseModel) map.get("library"));
                        intent.putExtra("awardWinners", awardsModels);
                        break;

                    case "http://patrakarsangh.in/GetCourses":
                        courseModels = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CourseModel courseModel = new CourseModel();
                            object = jsonArray.getJSONObject(i);
                            if (i == 0) {
                                model = (SmartLibraryResponseModel) map.get("library");
                                object = jsonArray.getJSONObject(i);
                                model.setFinancialYear(object.getString("Financial Year"));
                            }

                            if (i > 0) {
                                courseModel.setSrNo(object.getInt("SrNo"));
                                courseModel.setOrganizationId(object.getInt("OrganizationId"));
                                courseModel.setCourseTitle(object.getString("CourseTitle"));
                                courseModel.setOrganization(object.getString("Organization"));
                                courseModel.setDescription(object.getString("Description"));
                                courseModel.setDuration(object.getString("Duration"));
                                courseModel.setElligibility(object.getString("Elligibility"));
                                courseModel.setCreatedBy(object.getString("CreatedBy"));
                                courseModel.setCreatedDate(object.getString("CreatedDate"));
                                courseModel.setModifiedBy(object.getString("ModifiedBy"));
                                courseModel.setModifiedDate(object.getString("ModifiedDate"));
                                courseModel.setActive(object.getBoolean("IsActive"));
                                courseModels.add(courseModel);
                            }
                        }
                        intent = new Intent(context, CourseActivity.class);
                        intent.putExtra("library", (SmartLibraryResponseModel) map.get("library"));
                        intent.putExtra("courses", courseModels);
                        break;

                    case "http://patrakarsangh.in/GetSubjects":
                        subjectModels = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            SubjectModel subjectModel = new SubjectModel();
                            object = jsonArray.getJSONObject(i);
                            if (i == 0) {
                                model = (SmartLibraryResponseModel) map.get("library");
                                object = jsonArray.getJSONObject(i);
                                model.setFinancialYear(object.getString("Financial Year"));
                            }

                            if (i > 0) {
                                subjectModel.setSrNo(object.getInt("SrNo"));
                                subjectModel.setCourseId(object.getInt("CourseId"));
                                subjectModel.setSubject(object.getString("Subject"));
                                subjectModel.setDescription(object.getString("Description"));
                                subjectModel.setCreatedBy(object.getString("CreatedBy"));
                                subjectModel.setCreateDate(object.getString("CreateDate"));
                                subjectModel.setLastModifiedBy(object.getString("LastModifiedBy"));
                                subjectModel.setLastModifiedDate(object.getString("LastModifiedDate"));
                                subjectModel.setActive(object.getBoolean("IsActive"));
                                subjectModels.add(subjectModel);
                            }
                        }
                        intent = new Intent(context, SubjectsActivity.class);
                        intent.putExtra("library", (SmartLibraryResponseModel) map.get("library"));
                        intent.putExtra("course", (CourseModel) map.get("course"));
                        intent.putExtra("subjects", subjectModels);
                        break;
                    case "http://patrakarsangh.in/GetSubjectObjectives":
                        objectiveModels = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ObjectiveModel objectiveModel = new ObjectiveModel();
                            object = jsonArray.getJSONObject(i);
                            if (i == 0) {
                                model = (SmartLibraryResponseModel) map.get("library");
                                object = jsonArray.getJSONObject(i);
                                model.setFinancialYear(object.getString("Financial Year"));
                            }

                            if (i > 0) {
                                objectiveModel.setSrNo(object.getInt("SrNo"));
                                objectiveModel.setSubjectId(object.getInt("SubjectId"));
                                objectiveModel.setObjective(object.getString("Objective"));
                                objectiveModel.setCreatedBy(object.getString("CreatedBy"));
                                objectiveModel.setCreateDate(object.getString("CreateDate"));
                                objectiveModel.setLastModifiedBy(object.getString("LastModifiedBy"));
                                objectiveModel.setLastModifiedDate(object.getString("LastModifiedDate"));
                                objectiveModel.setActive(object.getBoolean("IsActive"));
                                objectiveModels.add(objectiveModel);
                            }
                        }
                        intent = new Intent(context, ObjectiveActivity.class);
                        intent.putExtra("library", (SmartLibraryResponseModel) map.get("library"));
                        intent.putExtra("subject", (SubjectModel) map.get("subject"));
                        intent.putExtra("objectives", objectiveModels);
                        break;
                    case "http://patrakarsangh.in/GetRestHouses":
                        restHouseModels = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            RestHouseModel restHouseModel = new RestHouseModel();
                            object = jsonArray.getJSONObject(i);
                            if (i == 0) {
                                model = (SmartLibraryResponseModel) map.get("library");
                                object = jsonArray.getJSONObject(i);
                                model.setFinancialYear(object.getString("Financial Year"));
                            }

                            if (i > 0) {
                                restHouseModel.setSrNo(object.getInt("SrNo"));
                                restHouseModel.setHouseName(object.getString("HouseName"));
                                restHouseModel.setAddress1(object.getString("Address1"));
                                restHouseModel.setAddress2(object.getString("Address2"));
                                restHouseModel.setLogoImage(URLConstants.COMPANY_BASE_URL + "CP/Uploads/RestHouseImages/" + object.getString("LogoImage"));
                                restHouseModel.setCity(object.getString("City"));
                                restHouseModel.setPin(object.getString("PIN"));
                                restHouseModels.add(restHouseModel);
                            }
                        }
                        intent = new Intent(context, VishramGrihaControlPanel.class);
                        intent.putExtra("library", (SmartLibraryResponseModel)map.get("library"));
                        intent.putExtra("restHouses", restHouseModels);
                        break;
                    case "http://patrakarsangh.in/GetRooms":
                        roomsModels = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            RoomModel roomModel = new RoomModel();
                            object = jsonArray.getJSONObject(i);
                            if (i == 0) {
                                model = (SmartLibraryResponseModel) map.get("library");
                                object = jsonArray.getJSONObject(i);
                                model.setFinancialYear(object.getString("Financial Year"));
                            }

                            if (i > 0) {
                                roomModel.setSrNo(object.getInt("SrNo"));
                                roomModel.setRoomNo(object.getString("RoomNo"));
                                roomModel.setRoomCapacity(object.getInt("RoomCapacity"));
                                roomModel.setRoomRent(object.getInt("RoomRent"));
                                roomModel.setRoomDeposit(object.getInt("RoomDeposit"));
                                roomModel.setRoomType(object.getString("RoomType"));
                                roomModel.setTaxDescription(object.getString("TaxDescription"));
                                roomModel.setDescription(object.getString("Description"));
                                roomModel.setStatus(object.getBoolean("Status"));
                                roomsModels.add(roomModel);
                            }
                        }
                        intent = new Intent(context, RoomsActivity.class);
                        intent.putExtra("library", (SmartLibraryResponseModel) map.get("library"));
                        intent.putExtra("restHouse", (RestHouseModel) map.get("restHouse"));
                        intent.putExtra("rooms", roomsModels);
                        break;
                }
                if (intent != null) {
                    intent.putExtra("list", libraryResponseModels);
                    context.startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (code == 200) {
            Toast.makeText(context, context.getString(R.string.nothing_to_show), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, context.getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
        }
    }
}