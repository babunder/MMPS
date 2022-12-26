package com.mmps.ui;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.mmps.R;
import com.mmps.models.AwardsModel;
import com.mmps.utils.TableRowTextViewUtil;

import java.text.NumberFormat;
import java.util.ArrayList;

public class AwardInfoActivity extends BaseActivity {

    private Toolbar toolbar;
    private TableLayout tableAwards;
    private TextView tvTitle, tvAwardPrice, tvAwardCriteria;
    private ArrayList<AwardsModel> awardsModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award_info);

        initToolbar();
        initui();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
    }

    private void initui() {
        tableAwards = findViewById(R.id.tableAwards);
        tvTitle = toolbar.findViewById(R.id.tv_title);
        tvAwardPrice = findViewById(R.id.tvAwardPrice);
        tvAwardCriteria = findViewById(R.id.tvAwardCriteria);

        if (getIntent().hasExtra("awardWinners")) {
            try {
                awardsModels = (ArrayList<AwardsModel>) getIntent().getExtras().get("awardWinners");
                tvTitle.setText(awardsModels.get(0).getAwardTitle());
                tvAwardPrice.setText(awardsModels.get(0).getAwardPrice());
                tvAwardCriteria.setText(awardsModels.get(0).getAwardCriteria());

                for (int i = 0; i < awardsModels.size(); i++) {
                    if (i != 0) {
                        TableRow tableRow = new TableRow(AwardInfoActivity.this);

                        TextView tvSrNo = new TableRowTextViewUtil().config(this, R.color.black, String.valueOf(NumberFormat.getInstance().format(i)));
                        TextView tvWinner = new TableRowTextViewUtil().config(this, R.color.black, awardsModels.get(i).getAwardWinner());
                        TextView tvYear = new TableRowTextViewUtil().config(this, R.color.black, String.valueOf(NumberFormat.getInstance().format(Integer.parseInt(awardsModels.get(i).getAwardYear())).replace(",", "")));

                        tvSrNo.setGravity(Gravity.CENTER_HORIZONTAL);
                        tvWinner.setGravity(Gravity.START);
                        tvWinner.setPadding(16, 8, 8, 8);
                        tvYear.setPadding(0, 0, 16, 0);
                        tvYear.setGravity(Gravity.END);

                        tableRow.addView(tvSrNo);
                        tableRow.addView(tvWinner);
                        tableRow.addView(tvYear);
                        tableAwards.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return false;
        }
    }
}
