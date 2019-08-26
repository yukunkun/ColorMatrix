package com.matrix.yukun.matrix.tool_module.calarder;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.selfview.calendar.OnCalendarClickListener;
import com.matrix.yukun.matrix.selfview.calendar.month.MonthCalendarView;
import com.matrix.yukun.matrix.selfview.calendar.schedule.ScheduleLayout;
import com.matrix.yukun.matrix.selfview.calendar.schedule.ScheduleRecyclerView;
import com.matrix.yukun.matrix.selfview.calendar.week.WeekCalendarView;
import com.matrix.yukun.matrix.tool_module.calarder.activity.CalandarActivity;
import com.matrix.yukun.matrix.tool_module.calarder.adapter.RVCalandarAdapter;
import com.matrix.yukun.matrix.tool_module.calarder.contant.CalandarBean;
import com.matrix.yukun.matrix.tool_module.calarder.dialog.EditCalandarDialog;
import com.matrix.yukun.matrix.tool_module.calarder.fragment.CalandarDetailFragment;
import com.matrix.yukun.matrix.util.DataUtils;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 备忘录
 */
public class TripartiteActivity extends BaseActivity implements OnCalendarClickListener, RVCalandarAdapter.OnSelectCallBack {

    @BindView(R.id.iv_all)
    ImageView mIvAll;
    @BindView(R.id.iv_today)
    ImageView mIvToday;
    @BindView(R.id.tv_calandar)
    TextView mTvCalandar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;
    @BindView(R.id.mcvCalendar)
    MonthCalendarView mMcvCalendar;
    @BindView(R.id.rlMonthCalendar)
    RelativeLayout mRlMonthCalendar;
    @BindView(R.id.wcvCalendar)
    WeekCalendarView mWcvCalendar;
    @BindView(R.id.rvScheduleList)
    ScheduleRecyclerView mRvScheduleList;
    @BindView(R.id.rlNoTask)
    RelativeLayout mRlNoTask;
    @BindView(R.id.rlScheduleList)
    RelativeLayout mRlScheduleList;
    @BindView(R.id.slSchedule)
    ScheduleLayout mSlSchedule;
    @BindView(R.id.fab_edit)
    FloatingActionButton mFabEdit;
    private List<CalandarBean> mCalandarBeans=new ArrayList<>();
    private RVCalandarAdapter mRvCalandarAdapter;

    @Override
    public int getLayout() {
        return R.layout.activity_tripartite;
    }

    @Override
    public void initView() {
        mRvCalandarAdapter = new RVCalandarAdapter(mCalandarBeans,this);
        mRvScheduleList.setLayoutManager(new LinearLayoutManager(this));
        mRvScheduleList.setAdapter(mRvCalandarAdapter);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date(System.currentTimeMillis());
        searchCalandar(DataUtils.getStringToDate(sdf.format(date),"yyyy/MM/dd"));
        mRvCalandarAdapter.setOnSelectCallBack(this);
    }

    @Override
    public void initListener() {
        mSlSchedule.setOnCalendarClickListener(this);
    }

    @OnClick({R.id.iv_all, R.id.iv_today, R.id.fab_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_all:
                Intent intent=new Intent(this, CalandarActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_today:
                mSlSchedule.getMonthCalendar().setTodayToView();
                break;
            case R.id.fab_edit:
                String time = mSlSchedule.getCurrentSelectYear() + "/" + (mSlSchedule.getCurrentSelectMonth()+1) + "/" + mSlSchedule.getCurrentSelectDay();
                EditCalandarDialog editCalandarDialog= EditCalandarDialog.getInstance(time);
                editCalandarDialog.show(getSupportFragmentManager(),"");
                break;
        }
    }

    @Override
    public void onClickDate(int year, int month, int day) {
        long stringToDate = DataUtils.getStringToDate(year + "/" + (month + 1) + "/" + day, "yyyy/MM/dd");
        searchCalandar(stringToDate);
    }

    private void searchCalandar(long timeStamp) {
        mCalandarBeans.clear();
        List<CalandarBean> calandarBeans = DataSupport.where("time = ?",timeStamp+"").find(CalandarBean.class);
        if(calandarBeans.size()>0){
            mRlNoTask.setVisibility(View.GONE);
            mCalandarBeans.addAll(calandarBeans);
        }else {
            mRlNoTask.setVisibility(View.VISIBLE);
        }
        mRvCalandarAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPageChange(int year, int month, int day) {
        String today=year+"年/"+(month+1)+"月";
        mTvCalandar.setText(today);
        long stringToDate = DataUtils.getStringToDate(year + "/" + (month + 1) + "/" + day, "yyyy/MM/dd");
        searchCalandar(stringToDate);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onSelectCallBack(CalandarBean calandarBean) {
        CalandarDetailFragment calandarDetailFragment= CalandarDetailFragment.getInstance(calandarBean);
        calandarDetailFragment.show(getFragmentManager(),"");
    }
}
