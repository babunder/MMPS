package com.mmps.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.bumptech.glide.Glide;
import com.mmps.R;
import com.mmps.models.BookModel;
import com.mmps.models.ComplementModel;
import com.mmps.models.ManagementBodyModel;
import com.mmps.models.MembershipModel;
import com.mmps.models.MyMembershipModel;
import com.mmps.models.RestHouseModel;
import com.mmps.models.RoomModel;
import com.mmps.models.SmartLibraryResponseModel;
import com.mmps.models.SocialProjectModel;
import com.mmps.models.StatisticsModel;
import com.mmps.ui.AboutUsActivity;
import com.mmps.ui.ControlPanelActivity;
import com.mmps.utils.ActionTypes;
import com.mmps.utils.SOAPUtils;
import com.mmps.utils.URLConstants;
import com.mmps.worker.LibraryTasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * @author Aditya Kulkarni
 */

public class RecyclerAdapter extends RecyclerView.Adapter {

    private ArrayList<SmartLibraryResponseModel> libraryResponseModels;
    private ArrayList<ManagementBodyModel> managementBodyModels;
    private ArrayList<SocialProjectModel> socialProjectModels;
    private ArrayList<ComplementModel> complementModels;
    private ArrayList<BookModel> bookModels;
    private ArrayList<MembershipModel> membershipModels;
    private ArrayList<MyMembershipModel> myMembershipModels;
    private ArrayList<StatisticsModel> statsList;
    private ArrayList<RestHouseModel> restHouseModels;
    private ArrayList<RoomModel> roomModels;
    private Context context;
    private String book_name, imageBaseUrl;
    private StatsCLickListener listener;
    private MyMembershipClickListener myMembershipClickListener;
    private VishramgrihaClickListener vishramgrihaClickListener;

    public RecyclerAdapter(ArrayList<SmartLibraryResponseModel> libraryResponseModels, Context context, String book_name) {
        this.libraryResponseModels = libraryResponseModels;
        this.context = context;
        this.book_name = book_name;
    }

    public RecyclerAdapter(ArrayList<ManagementBodyModel> managementBodyModels, Context context) {
        this.managementBodyModels = managementBodyModels;
        this.context = context;
    }

    public RecyclerAdapter(Context context, ArrayList<SocialProjectModel> socialProjectModels) {
        this.socialProjectModels = socialProjectModels;
        this.context = context;
    }

    public RecyclerAdapter(Context context, ArrayList<ComplementModel> complementModels, String imageBaseUrl) {
        this.complementModels = complementModels;
        this.context = context;
        this.imageBaseUrl = imageBaseUrl;
    }

    public RecyclerAdapter(Context context, String imageBaseUrl, ArrayList<BookModel> bookModels) {
        this.bookModels = bookModels;
        this.context = context;
        this.imageBaseUrl = imageBaseUrl;
    }

    public RecyclerAdapter(ArrayList<MembershipModel> membershipModels, Context context, int dummy) {
        this.membershipModels = membershipModels;
        this.context = context;
    }

    public RecyclerAdapter(ArrayList<StatisticsModel> statsList, Context context, boolean flag, StatsCLickListener listener) {
        this.statsList = statsList;
        this.context = context;
        this.listener = listener;
    }

    public RecyclerAdapter(ArrayList<MyMembershipModel> myMembershipModelList, Context context, boolean flag, MyMembershipClickListener listener) {
        this.myMembershipModels = myMembershipModelList;
        this.context = context;
        this.myMembershipClickListener = listener;
    }

    public RecyclerAdapter(ArrayList<RestHouseModel> restHouseModels, Context context, boolean flag, VishramgrihaClickListener listener) {
        this.restHouseModels = restHouseModels;
        this.context = context;
        this.vishramgrihaClickListener = listener;
    }

    public RecyclerAdapter(ArrayList<RoomModel> roomModels, Context context, int flag, int flag1) {
        this.roomModels = roomModels;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case ActionTypes.TYPE_SMART_LIBRARY:
                view = LayoutInflater.from(context).inflate(R.layout.smart_libraries_rowlayout, parent, false);
                return new SmartLibrariesViewHolder(view);
            case ActionTypes.TYPE_SMART_SEARCH:
                view = LayoutInflater.from(context).inflate(R.layout.book_search_row_layout, parent, false);
                return new SearchViewHolder(view);
            case ActionTypes.TYPE_REGION_LIBRARIES:
                view = LayoutInflater.from(context).inflate(R.layout.region_library_layout, parent, false);
                return new RegionSpecificLibraryViewHolder(view);
            case ActionTypes.TYPE_MANAGEMENT_BODY:
                view = LayoutInflater.from(context).inflate(R.layout.management_body_row_layout, parent, false);
                return new ManagementBodyViewHolder(view);
            case ActionTypes.TYPE_SOCIAL_PROJECTS:
                view = LayoutInflater.from(context).inflate(R.layout.social_project_row_layout, parent, false);
                return new SocialProjectsViewHolder(view);
            case ActionTypes.TYPE_COMPLEMENTS:
                view = LayoutInflater.from(context).inflate(R.layout.complement_row_layout, parent, false);
                return new ComplementsViewHolder(view);
            case ActionTypes.TYPE_REGIONS:
                view = LayoutInflater.from(context).inflate(R.layout.regions_row_layout, parent, false);
                return new RegionsViewHolder(view);
            case ActionTypes.TYPE_CONTROL_PANEL:
                view = LayoutInflater.from(context).inflate(R.layout.old_control_panel, parent, false);
                return new ControlPanelViewHolder(view);
            case ActionTypes.TYPE_NAV_CONTROL_PANEL:
                view = LayoutInflater.from(context).inflate(R.layout.control_panel_row_layout, parent, false);
                return new NavControlPanelViewHolder(view);
            case ActionTypes.TYPE_NEW_BOOKS:
                view = LayoutInflater.from(context).inflate(R.layout.books_row_layout, parent, false);
                return new NewBooksViewHolder(view);
            case ActionTypes.TYPE_RARE_BOOKS:
                view = LayoutInflater.from(context).inflate(R.layout.books_row_layout, parent, false);
                return new NewBooksViewHolder(view);
            case ActionTypes.TYPE_GRANTHSAMPADA:
                view = LayoutInflater.from(context).inflate(R.layout.layout_list_item_row, parent, false);
                return new BookStatisticsViewHolder(view);
            case ActionTypes.TYPE_READERS_CHOICE:
                view = LayoutInflater.from(context).inflate(R.layout.books_row_layout, parent, false);
                return new NewBooksViewHolder(view);
            case ActionTypes.TOP_10_READERS:
                Log.e("Adapter", "readers");
                view = LayoutInflater.from(context).inflate(R.layout.table_row_layout, parent, false);
                return new TopReadersViewHolder(view);
            case ActionTypes.MEMBERSHIP_PLANS:
                view = LayoutInflater.from(context).inflate(R.layout.membership_plans_row_layout, parent, false);
                return new MembershipPlansViewHolder(view);
            case ActionTypes.TYPE_WISE_COUNT:
                view = LayoutInflater.from(context).inflate(R.layout.table_row_layout, parent, false);
                return new TypeWiseCount(view);
            case ActionTypes.MEMBER_STATISTICS:
                view = LayoutInflater.from(context).inflate(R.layout.table_row_layout, parent, false);
                return new MemberStatisticsHolder(view);
            case ActionTypes.TYPE_WISE_PURCHASE:
                view = LayoutInflater.from(context).inflate(R.layout.purchase_row_layout, parent, false);
                return new TypePurchaseViewHolder(view);
            case ActionTypes.TYPE_STATISTICS:
                view = LayoutInflater.from(context).inflate(R.layout.layout_list_item_row, parent, false);
                return new StatisticsViewHolder(view);
            /*case ActionTypes.TYPE_PATRAKAR_SANGHA:
                view = LayoutInflater.from(context).inflate(R.layout.layout_list_item_row, parent, false);
                return new PatrakarSanghaViewHolder(view);*/
            case ActionTypes.TYPE_HISTORY:
                view = LayoutInflater.from(context).inflate(R.layout.history_row_layout, parent, false);
                return new HistoryViewHolder(view);
            case ActionTypes.TYPE_MY_MEMBERSHIP:
                view = LayoutInflater.from(context).inflate(R.layout.my_membership_row, parent, false);
                return new MyMembershipViewHolder(view);

            case ActionTypes.TYPE_REST_HOUSE:
                view = LayoutInflater.from(context).inflate(R.layout.rest_house_row_layout, parent, false);
                return new VishramgrihaViewHolder(view);
            case ActionTypes.TYPE_ROOMS:
                view = LayoutInflater.from(context).inflate(R.layout.room_row_layout, parent, false);
                return new RoomViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (libraryResponseModels != null) {
            return libraryResponseModels.get(position).getType();
        }
        if (socialProjectModels != null) {
            return socialProjectModels.get(position).getType();
        }
        if (complementModels != null) {
            return complementModels.get(position).getType();
        }
        if (bookModels != null) {
            return bookModels.get(position).getType();
        }
        if (membershipModels != null) {
            return membershipModels.get(position).getType();
        }
        if (statsList != null) {
            return statsList.get(position).getType();
        }
        if (myMembershipModels != null) {
            return ActionTypes.TYPE_MY_MEMBERSHIP;
        }
        if (restHouseModels != null) {
            return ActionTypes.TYPE_REST_HOUSE;
        }
        if (roomModels != null) {
            return ActionTypes.TYPE_ROOMS;
        }

        return managementBodyModels.get(position).getType();
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case ActionTypes.TYPE_SMART_LIBRARY:
                ((SmartLibrariesViewHolder) holder).bindView(position);
                break;
            case ActionTypes.TYPE_SMART_SEARCH:
                ((SearchViewHolder) holder).bindView(position);
                break;

            case ActionTypes.TYPE_REGION_LIBRARIES:
                ((RegionSpecificLibraryViewHolder) holder).bindView(position);
                break;

            case ActionTypes.TYPE_MANAGEMENT_BODY:
                ((ManagementBodyViewHolder) holder).bindView(position);
                break;

            case ActionTypes.TYPE_SOCIAL_PROJECTS:
                ((SocialProjectsViewHolder) holder).bindView(position);
                break;

            case ActionTypes.TYPE_REGIONS:
                ((RegionsViewHolder) holder).bindView(position);
                break;

            case ActionTypes.TYPE_COMPLEMENTS:
                ((ComplementsViewHolder) holder).bindView(position);
                break;
            case ActionTypes.TYPE_CONTROL_PANEL:
                ((ControlPanelViewHolder) holder).bindView(position);
                break;
            case ActionTypes.TYPE_NAV_CONTROL_PANEL:
                ((NavControlPanelViewHolder) holder).bindView(position);
                break;
            case ActionTypes.TYPE_NEW_BOOKS:
                ((NewBooksViewHolder) holder).bindView(position);
                break;
            case ActionTypes.TYPE_RARE_BOOKS:
                ((NewBooksViewHolder) holder).bindView(position);
                break;
            case ActionTypes.TYPE_GRANTHSAMPADA:
                ((BookStatisticsViewHolder) holder).bindView(position);
                break;
            case ActionTypes.TYPE_READERS_CHOICE:
                ((NewBooksViewHolder) holder).bindView(position);
                break;
            case ActionTypes.TOP_10_READERS:
                Log.e("Adapter", "readers");
                ((TopReadersViewHolder) holder).bindView(position);
                break;
            case ActionTypes.MEMBERSHIP_PLANS:
                ((MembershipPlansViewHolder) holder).bindView(position);
                break;
            case ActionTypes.TYPE_WISE_COUNT:
                ((TypeWiseCount) holder).bindView(position);
                break;
            case ActionTypes.TYPE_WISE_PURCHASE:
                ((TypePurchaseViewHolder) holder).bindView(position);
                break;
            case ActionTypes.MEMBER_STATISTICS:
                ((MemberStatisticsHolder) holder).bindView(position);
                break;
            case ActionTypes.TYPE_STATISTICS:
                ((StatisticsViewHolder) holder).bindView(position);
                break;
            /*case ActionTypes.TYPE_PATRAKAR_SANGHA:
                ((PatrakarSanghaViewHolder) holder).bindView(position);
                break;*/
            case ActionTypes.TYPE_HISTORY:
                ((HistoryViewHolder) holder).bindView(position);
                break;
            case ActionTypes.TYPE_MY_MEMBERSHIP:
                ((MyMembershipViewHolder) holder).bindView(position);
                break;
            case ActionTypes.TYPE_REST_HOUSE:
                ((VishramgrihaViewHolder) holder).bindView(position);
                break;
            case ActionTypes.TYPE_ROOMS:
                ((RoomViewHolder) holder).bindView(position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (managementBodyModels != null) {
            return managementBodyModels.size();
        } else if (libraryResponseModels != null) {
            return libraryResponseModels.size();
        } else if (socialProjectModels != null) {
            return socialProjectModels.size();
        } else if (complementModels != null) {
            return complementModels.size();
        } else if (bookModels != null) {
            return bookModels.size();
        } else if (membershipModels != null) {
            return membershipModels.size();
        } else if (statsList != null) {
            return statsList.size();
        } else if (myMembershipModels != null) {
            return myMembershipModels.size();
        }else if (restHouseModels != null) {
            return restHouseModels.size();
        }else if (roomModels != null) {
            return roomModels.size();
        } else {
            return 0;
        }
    }

    class SmartLibrariesViewHolder extends RecyclerView.ViewHolder {

        ImageView ivLibraryLogo;
        TextView tvLibraryName, tvLibraryAddress1, tvLibraryAddress2, tvLibraryMCity, tvLibraryPin;

        public SmartLibrariesViewHolder(View itemView) {
            super(itemView);

            ivLibraryLogo = itemView.findViewById(R.id.ivLibraryLogo);
            tvLibraryName = itemView.findViewById(R.id.tvLibraryName);
            tvLibraryName.setSelected(true);
            tvLibraryAddress1 = itemView.findViewById(R.id.tvLibraryAddress1);
            tvLibraryAddress1.setSelected(true);
            tvLibraryAddress2 = itemView.findViewById(R.id.tvLibraryAddress2);
            tvLibraryAddress2.setSelected(true);
            tvLibraryMCity = itemView.findViewById(R.id.tvLibraryMCity);
            tvLibraryMCity.setSelected(true);
            tvLibraryPin = itemView.findViewById(R.id.tvLibraryPin);
        }

        public void bindView(int position) {
            try {
                Glide.with(context).load(libraryResponseModels.get(position).getLogoImage()).into(ivLibraryLogo);
                tvLibraryName.setText(libraryResponseModels.get(position).getLibraryName());
                tvLibraryAddress1.setText(libraryResponseModels.get(position).getAddress1());
                tvLibraryAddress2.setText(libraryResponseModels.get(position).getAddress2());
                tvLibraryMCity.setText(libraryResponseModels.get(position).getM_CityName());
                tvLibraryPin.setText(libraryResponseModels.get(position).getPIN());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    class SearchViewHolder extends RecyclerView.ViewHolder {

        ImageView ivLibraryLogo;
        TextView tvLibraryName, tvLibraryBookCount, tvLibraryBookName;
        View itemView;

        public SearchViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ivLibraryLogo = itemView.findViewById(R.id.ivSearchLibraryLogo);
            tvLibraryName = itemView.findViewById(R.id.tvSearchLibraryName);
            tvLibraryName.setSelected(true);
            tvLibraryBookCount = itemView.findViewById(R.id.tvSearchLibraryBookCount);
            tvLibraryBookCount.setSelected(true);
            tvLibraryBookName = itemView.findViewById(R.id.tvSearchBookName);
        }

        void bindView(final int position) {
            try {
                final int libraryId = libraryResponseModels.get(position).getLibraryId();
                final String databaseName = libraryResponseModels.get(position).getDatabaseName();
                final SmartLibraryResponseModel model = libraryResponseModels.get(position);
                Glide.with(context).load(libraryResponseModels.get(position).getLogoImage()).into(ivLibraryLogo);
                tvLibraryName.setText(libraryResponseModels.get(position).getLibraryName());
                tvLibraryBookName.setText("'" + book_name + "'");
                SpannableString spannableString = new SpannableString(context.getString(R.string.total) + " " + libraryResponseModels.get(position).getResultCount() + " " + context.getString(R.string.books));
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {

                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        ds.setColor(context.getResources().getColor(R.color.colorPrimary));
                    }
                };
                if (Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("english")) {
                    spannableString.setSpan(clickableSpan, 5, 7, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                } else if (Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("मराठी")) {
                    spannableString.setSpan(clickableSpan, 5, 5 + String.valueOf(libraryResponseModels.get(position).getResultCount()).length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                }
                tvLibraryBookCount.setText(spannableString);
                tvLibraryBookCount.setMovementMethod(new LinkMovementMethod());

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (SOAPUtils.isNetworkConnected(context)) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("LibraryId", libraryId);
                            map.put("DatabaseName", databaseName);
                            map.put("SearchParam", book_name);
                            map.put("library", model);
                            new LibraryTasks(URLConstants.ACTION_SEARCH_BOOK, map, context).execute();
                        } else {
                            Toast.makeText(context, context.getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class RegionsViewHolder extends RecyclerView.ViewHolder {
        TextView tvRegionName;

        public RegionsViewHolder(View itemView) {
            super(itemView);

            tvRegionName = itemView.findViewById(R.id.tvRegionName);
        }

        void bindView(int position) {
            tvRegionName.setText(libraryResponseModels.get(position).getRegionName());
        }
    }

    class ControlPanelViewHolder extends RecyclerView.ViewHolder {
        TextView tvRegionName;
        RelativeLayout relativeLayout;

        public ControlPanelViewHolder(View itemView) {
            super(itemView);
            tvRegionName = itemView.findViewById(R.id.tvRegionName);
            relativeLayout = itemView.findViewById(R.id.rlControlPanel);
        }

        void bindView(int position) {
            tvRegionName.setText(libraryResponseModels.get(position).getRegionName());
        }
    }

    class NavControlPanelViewHolder extends RecyclerView.ViewHolder {
        TextView tvRegionName;
        ImageView ivControlPanel;
        RelativeLayout relativeLayout;

        public NavControlPanelViewHolder(View itemView) {
            super(itemView);
            ivControlPanel = itemView.findViewById(R.id.ivControlPanel);
            tvRegionName = itemView.findViewById(R.id.tvRegionName);
            relativeLayout = itemView.findViewById(R.id.rlControlPanel);
        }

        @SuppressLint("NewApi")
        void bindView(int position) {
            if (position == 2 || position == 3 || position == 6 || position == 7) {
                tvRegionName.setTextColor(context.getResources().getColor(R.color.white));
                relativeLayout.setBackground(context.getResources().getDrawable(R.drawable.card_fill_red));
            }
           ivControlPanel.setImageDrawable(VectorDrawableCompat.create(context.getResources(), libraryResponseModels.get(position).getIconId(), null));
            //ivControlPanel.setImageDrawable(VectorDrawableCompat.create(context.getResources(), R.drawable.new_syllabus, null));
            tvRegionName.setText(libraryResponseModels.get(position).getRegionName());
        }
    }

    class RegionSpecificLibraryViewHolder extends RecyclerView.ViewHolder {

        ImageView ivLibraryLogo;
        TextView tvLibraryName, tvLibraryAddress1, tvLibraryAddress2, tvLibraryMCity, tvLibraryPin;

        public RegionSpecificLibraryViewHolder(View itemView) {
            super(itemView);

            ivLibraryLogo = itemView.findViewById(R.id.ivLibraryLogo);
            tvLibraryName = itemView.findViewById(R.id.tvLibraryName);
            tvLibraryName.setSelected(true);
            tvLibraryAddress1 = itemView.findViewById(R.id.tvLibraryAddress1);
            tvLibraryAddress1.setSelected(true);
            tvLibraryAddress2 = itemView.findViewById(R.id.tvLibraryAddress2);
            tvLibraryAddress2.setSelected(true);
            tvLibraryMCity = itemView.findViewById(R.id.tvLibraryMCity);
            tvLibraryMCity.setSelected(true);
            tvLibraryPin = itemView.findViewById(R.id.tvLibraryPin);
        }

        public void bindView(final int position) {
            try {
                Glide.with(context).load(libraryResponseModels.get(position).getLogoImage()).into(ivLibraryLogo);
                tvLibraryName.setText(libraryResponseModels.get(position).getLibraryName());
                tvLibraryAddress1.setText(libraryResponseModels.get(position).getAddress1());
                tvLibraryAddress2.setText(libraryResponseModels.get(position).getAddress2());
                tvLibraryMCity.setText(libraryResponseModels.get(position).getM_CityName());
                tvLibraryPin.setText(" - " + libraryResponseModels.get(position).getPIN());

                tvLibraryName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, AboutUsActivity.class);
                        intent.putExtra("library", libraryResponseModels.get(position));
                        Log.e("Intent", libraryResponseModels.get(position).getSrNo() + "");
                        context.startActivity(intent);
                    }
                });

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, ControlPanelActivity.class);
                        intent.putExtra("library", libraryResponseModels.get(position));
                        context.startActivity(intent);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class ManagementBodyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivManagementBodyPerson;
        TextView tvManagementBodyName, tvManagementBodyProfile;

        public ManagementBodyViewHolder(View itemView) {
            super(itemView);
            Log.e("holder", "management");
            ivManagementBodyPerson = itemView.findViewById(R.id.ivManagementBodyPerson);
            tvManagementBodyName = itemView.findViewById(R.id.tvManagementBodyName);
            tvManagementBodyProfile = itemView.findViewById(R.id.tvManagementBodyProfile);
        }

        public void bindView(int position) {
            try {
                Glide.with(context).load("http://" + managementBodyModels.get(position).getWebsite() + "/CP/Uploads/ManagementBodyImages/" + managementBodyModels.get(position).getLogoImage()).into(ivManagementBodyPerson);
                tvManagementBodyName.setText(managementBodyModels.get(position).getManagementBodyName());
                tvManagementBodyProfile.setText(managementBodyModels.get(position).getProfileIdentity());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class SocialProjectsViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProject;
        TextView tvProjectTitle, tvProjectDate, tvProjectDesc;

        public SocialProjectsViewHolder(View itemView) {
            super(itemView);

            ivProject = itemView.findViewById(R.id.ivProjectPhoto);
            tvProjectTitle = itemView.findViewById(R.id.tvProjectTitle);
            tvProjectDate = itemView.findViewById(R.id.tvProjectDate);
            tvProjectDesc = itemView.findViewById(R.id.tvProjectDesc);
        }

        public void bindView(final int position) {
            try {
                Glide.with(context).load(URLConstants.COMPANY_BASE_URL+ "CP/Uploads/ProjectImages/" + socialProjectModels.get(position).getPhotoImage()).into(ivProject);
                tvProjectTitle.setText(socialProjectModels.get(position).getProjectTitle());
                tvProjectDate.setText(socialProjectModels.get(position).getProjectDate());
                tvProjectDesc.setText(socialProjectModels.get(position).getLongDesc());


                ivProject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (SOAPUtils.isNetworkConnected(context)) {
                            View view1 = LayoutInflater.from(context).inflate(R.layout.image_zoom_layout, null, false);
                            Dialog dialog = new Dialog(context);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(view1);
                            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                            ImageView imageView = view1.findViewById(R.id.ivProjectPhoto);
                            Glide.with(context).load(URLConstants.COMPANY_BASE_URL+ "CP/Uploads/ProjectImages/" + socialProjectModels.get(position).getPhotoImage()).into(imageView);

                            dialog.show();
                        } else {
                            Toast.makeText(context, context.getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();

                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class ComplementsViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProject;
        ImageButton imgBtnPlus;
        TextView tvProjectTitle, tvProjectDate, tvProjectDesc;

        public ComplementsViewHolder(View itemView) {
            super(itemView);
            imgBtnPlus = itemView.findViewById(R.id.imgBtnPlus);
            ivProject = itemView.findViewById(R.id.ivProjectPhoto);
            tvProjectTitle = itemView.findViewById(R.id.tvProjectTitle);
            tvProjectDate = itemView.findViewById(R.id.tvProjectDate);
            tvProjectDesc = itemView.findViewById(R.id.tvProjectDesc);
        }

        public void bindView(final int position) {
            try {
                Glide.with(context).load(imageBaseUrl + complementModels.get(position).getScannedCopy()).into(ivProject);
                tvProjectTitle.setText(complementModels.get(position).getComplementsBy());
                tvProjectDate.setText(complementModels.get(position).getComplementDate());
                tvProjectDesc.setText(complementModels.get(position).getDetails());

                Layout l = tvProjectDesc.getLayout();
                if (l != null) {
                    int lines = l.getLineCount();
                    if (lines > 3) {
                        Toast.makeText(context, "true", Toast.LENGTH_LONG).show();
                        imgBtnPlus.setVisibility(View.VISIBLE);
                    } else {
                        imgBtnPlus.setVisibility(View.GONE);
                    }
                }

                ivProject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (SOAPUtils.isNetworkConnected(context)) {
                            View view1 = LayoutInflater.from(context).inflate(R.layout.image_zoom_layout, null, false);
                            Dialog dialog = new Dialog(context);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(view1);
                            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                            ImageView imageView = view1.findViewById(R.id.ivProjectPhoto);
                            Glide.with(context).load(imageBaseUrl + complementModels.get(position).getScannedCopy()).into(imageView);

                            dialog.show();
                        } else {
                            Toast.makeText(context, context.getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();

                        }
                    }
                });

                tvProjectDesc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(complementModels.get(position).getComplementsBy());
                        builder.setMessage(complementModels.get(position).getDetails());
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class NewBooksViewHolder extends RecyclerView.ViewHolder {

        ImageView ivBookCover;
        TextView tvBookTitle, tvAuthor, tvBookType, tvPublisher, tvBookNo, tvCount;

        public NewBooksViewHolder(View itemView) {
            super(itemView);

            ivBookCover = itemView.findViewById(R.id.ivBookCover);
            tvBookNo = itemView.findViewById(R.id.tvBookNumber);
            tvBookTitle = itemView.findViewById(R.id.tvBookTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvBookType = itemView.findViewById(R.id.tvBookType);
            tvPublisher = itemView.findViewById(R.id.tvPublisher);
            tvPublisher.setSelected(true);
            tvCount = itemView.findViewById(R.id.tvCount);
        }

        public void bindView(final int position) {
            try {
                Glide.with(context).load(imageBaseUrl + bookModels.get(position).getBookImage()).into(ivBookCover);
                tvBookNo.setText(bookModels.get(position).getBookInwardNo() + " / " + bookModels.get(position).getBookNo());
                tvBookTitle.setText(bookModels.get(position).getBookTitle());
                tvAuthor.setText(bookModels.get(position).getAuthor());
                tvBookType.setText(bookModels.get(position).getBookType());
                tvPublisher.setText(bookModels.get(position).getPublisherName());
                if (bookModels.get(position).getBookCount() != 0) {
                    tvPublisher.setText(bookModels.get(position).getBookCount() + " " + context.getString(R.string.members_has_read));
                }

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View view1 = LayoutInflater.from(context).inflate(R.layout.info_doialog_layout, null);
                        TextView tvtitle = view1.findViewById(R.id.tvDialogTitle);
                        TextView tvMessage = view1.findViewById(R.id.tvDialogMessage);
                        TextView tvAcailability = view1.findViewById(R.id.tvDialogAvailability);
                        tvtitle.setText(bookModels.get(position).getBookTitle());
                        tvMessage.setText(bookModels.get(position).getAuthor()
                                + "\n" + bookModels.get(position).getBookType()
                                + "\n" + bookModels.get(position).getPublisherName());
                        if (bookModels.get(position).isAvailable()) {
                            tvAcailability.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
                            tvAcailability.setText(context.getString(R.string.available));
                        } else {
                            tvAcailability.setTextColor(context.getResources().getColor(android.R.color.holo_red_light));
                            tvAcailability.setText(context.getString(R.string.not_available));
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setView(view1);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class BookStatisticsViewHolder extends RecyclerView.ViewHolder {
        TextView tvtitle, tvBookType, tvBookCount;
        ImageView ivStatLogo, ic_forward;

        public BookStatisticsViewHolder(View itemView) {
            super(itemView);

            ivStatLogo = itemView.findViewById(R.id.ic_stat_logo);
            ivStatLogo.setVisibility(View.GONE);
            ic_forward = itemView.findViewById(R.id.ic_forward);
            ic_forward.setVisibility(View.GONE);
            tvtitle = itemView.findViewById(R.id.text1);
            /*tvBookType = itemView.findViewById(R.id.tvBookType);
            tvBookCount = itemView.findViewById(R.id.tvBookCount);*/
        }

        public void bindView(int position) {
            try {
                tvtitle.setText(bookModels.get(position).getBookType()
                        + " (" + String.valueOf(bookModels.get(position).getBookCount()) + ")");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class TopReadersViewHolder extends RecyclerView.ViewHolder {
        TextView tv2, tv3, tv4;

        public TopReadersViewHolder(View itemView) {
            super(itemView);

            tv2 = itemView.findViewById(R.id.tv2);
            tv3 = itemView.findViewById(R.id.tv3);
            tv4 = itemView.findViewById(R.id.tv4);
        }

        public void bindView(int position) {
            try {
                tv2.setText(String.valueOf(position + 1));
                tv3.setText(bookModels.get(position).getMember());
                tv4.setText(String.valueOf(bookModels.get(position).getBookCount()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class TypeWiseCount extends RecyclerView.ViewHolder {
        TextView tv2, tv3, tv4;

        public TypeWiseCount(View itemView) {
            super(itemView);

            tv2 = itemView.findViewById(R.id.tv2);
            tv3 = itemView.findViewById(R.id.tv3);
            tv4 = itemView.findViewById(R.id.tv4);
        }

        public void bindView(int position) {
            try {
                tv2.setText(String.valueOf(position + 1));
                tv3.setText(bookModels.get(position).getBookType());
                tv4.setText(String.valueOf(bookModels.get(position).getBookCount()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class TypePurchaseViewHolder extends RecyclerView.ViewHolder {
        TextView tv2, tv3, tv4, tv5;

        public TypePurchaseViewHolder(View itemView) {
            super(itemView);

            tv2 = itemView.findViewById(R.id.tv2);
            tv3 = itemView.findViewById(R.id.tv3);
            tv4 = itemView.findViewById(R.id.tv4);
            tv5 = itemView.findViewById(R.id.tv5);
        }

        public void bindView(int position) {
            try {
                tv2.setText(String.valueOf(position + 1));
                tv3.setText(bookModels.get(position).getBookType());
                tv5.setText(String.valueOf(bookModels.get(position).getBookCount()));
                tv4.setText(String.valueOf(bookModels.get(position).getTotalAmount()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class MemberStatisticsHolder extends RecyclerView.ViewHolder {
        TextView tv2, tv3, tv4;

        public MemberStatisticsHolder(View itemView) {
            super(itemView);

            tv2 = itemView.findViewById(R.id.tv2);
            tv3 = itemView.findViewById(R.id.tv3);
            tv4 = itemView.findViewById(R.id.tv4);
        }

        public void bindView(int position) {
            try {
                tv2.setText(String.valueOf(position + 1));
                tv3.setText(membershipModels.get(position).getMembershipPlan());
                tv4.setText(String.valueOf(membershipModels.get(position).getMeberCount()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class MembershipPlansViewHolder extends RecyclerView.ViewHolder {

        TextView tvPlan, tvMaxBooks, tvDuration;

        public MembershipPlansViewHolder(View itemView) {
            super(itemView);

            tvPlan = itemView.findViewById(R.id.tvPlan);
            tvMaxBooks = itemView.findViewById(R.id.tvMaxBooks);
            tvDuration = itemView.findViewById(R.id.tvDuration);
        }

        public void bindView(int position) {
            tvPlan.setText(membershipModels.get(position).getMembershipPlan());
            tvMaxBooks.setText(context.getString(R.string.books) + " "
                    + membershipModels.get(position).getMaxBooks() + " / "
                    + context.getString(R.string.magazines) + " "
                    + membershipModels.get(position).getMaxMagazines());
            tvDuration.setText(context.getString(R.string.duration) + " - " + membershipModels.get(position).getDuration() + " " + context.getString(R.string.days));

        }
    }

    class StatisticsViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView ivStatLogo;

        public StatisticsViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.text1);
            ivStatLogo = itemView.findViewById(R.id.ic_stat_logo);
        }

        public void bindView(final int position) {
            tvTitle.setText(statsList.get(position).getTitle());
            ivStatLogo.setImageDrawable(context.getResources().getDrawable(statsList.get(position).getIconId()));
        }
    }

    /*class PatrakarSanghaViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView ivStatLogo;
        public PatrakarSanghaViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.text1);
            ivStatLogo = itemView.findViewById(R.id.ic_stat_logo);
        }
        public void bindView(final int position) {
            tvTitle.setText(statsList.get(position).getTitle());
            ivStatLogo.setImageDrawable(context.getResources().getDrawable(statsList.get(position).getIconId()));
            tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onStatsClicked(statsList.get(position).getTitle());
                }
            });
        }
    }*/

    class MyMembershipViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMembership;

        public MyMembershipViewHolder(View itemView) {
            super(itemView);
            ivMembership = itemView.findViewById(R.id.ic_membership);
        }

        public void bindView(final int position) {
            ivMembership.setBackgroundResource(myMembershipModels.get(position).getDrawable());
            ivMembership.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myMembershipClickListener.onMyMembershipClicked(String.valueOf(myMembershipModels.get(position).getLabel()));
                }
            });
        }
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvHeadline, tvHistory;

        public HistoryViewHolder(View itemView) {
            super(itemView);

            tvHeadline = itemView.findViewById(R.id.tvHeadline);
            tvHistory = itemView.findViewById(R.id.tvHistory);
        }

        public void bindView(int position) {
            SpannableString content = new SpannableString(libraryResponseModels.get(position).getHeadline());
            content.setSpan(new UnderlineSpan(), 0, libraryResponseModels.get(position).getHeadline().length(), 0);
            tvHeadline.setText(content);
            tvHistory.setText(libraryResponseModels.get(position).getHistory());
        }
    }

    class VishramgrihaViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAddress;
        ImageView ivRestHouseLogo;

        public VishramgrihaViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_rest_house_name);
            tvAddress = itemView.findViewById(R.id.tv_rest_house_address);
            ivRestHouseLogo = itemView.findViewById(R.id.ic_rest_house_logo);
        }

        public void bindView(final int position) {
            tvTitle.setText(restHouseModels.get(position).getHouseName());
            tvAddress.setText(restHouseModels.get(position).getAddress1() + "\n" + restHouseModels.get(position).getAddress2() + "\n" + restHouseModels.get(position).getCity() + " PIN - " + restHouseModels.get(position).getPin());
            Glide.with(context).load(restHouseModels.get(position).getLogoImage()).into(ivRestHouseLogo);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vishramgrihaClickListener.onVishramgrihaClicked(restHouseModels.get(position));
                }
            });
        }
    }

    class RoomViewHolder extends RecyclerView.ViewHolder{

        TextView tvRoomNumber, tvRoomType, tvRoomCapacity, tvRoomDeposit, tvRoomRent, tvTaxDescription, tvTaxValue, tvTotal;
        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomCapacity = itemView.findViewById(R.id.tv_room_capacity);
            tvRoomNumber = itemView.findViewById(R.id.tv_room_number);
            tvRoomType = itemView.findViewById(R.id.tv_room_type);
            tvRoomDeposit = itemView.findViewById(R.id.tv_room_deposit);
            tvRoomRent = itemView.findViewById(R.id.tv_room_rent);
            tvTaxDescription = itemView.findViewById(R.id.tv_room_tax_description);
            tvTaxValue = itemView.findViewById(R.id.tv_room_tax_value);
            tvTotal = itemView.findViewById(R.id.tv_total);
        }

        void bindView(final int position){
            double roomTax = roomModels.get(position).getRoomRent() * (0.09) * 2;
            double total = roomTax + roomModels.get(position).getRoomRent();
            tvRoomNumber.setText(String.format("%s - %s", context.getString(R.string.room_no), roomModels.get(position).getRoomNo()));
            tvRoomType.setText(roomModels.get(position).getRoomType());
            tvRoomCapacity.setText(String.format("%d %s", roomModels.get(position).getRoomCapacity(), context.getString(R.string.person)));
            tvRoomDeposit.setText(String.format("%d %s", roomModels.get(position).getRoomDeposit(), context.getString(R.string.rs)));
            tvRoomRent.setText(String.format("%d %s", roomModels.get(position).getRoomRent(), context.getString(R.string.rs)));
            tvTaxDescription.setText(roomModels.get(position).getTaxDescription());
            tvTaxValue.setText(String.format("%.2f %s", roomTax, context.getString(R.string.rs)));
            tvTotal.setText(String.format("%.2f %s", total, context.getString(R.string.rs)));
        }
    }

    public interface StatsCLickListener {
        void onStatsClicked(String stat);
    }

    public interface MyMembershipClickListener {
        void onMyMembershipClicked(String s);
    }

    public interface VishramgrihaClickListener{
        void onVishramgrihaClicked(RestHouseModel restHouseModel);
    }

/*
    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
*/
}
