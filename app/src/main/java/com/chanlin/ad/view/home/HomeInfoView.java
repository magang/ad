package com.chanlin.ad.view.home;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chanlin.ad.R;
import com.chanlin.ad.base.BaseFragment;
import com.chanlin.ad.base.BaseRecyclerAdapter;
import com.chanlin.ad.base.RecyclerViewHolder;
import com.chanlin.ad.data.Trade;
import com.chanlin.ad.data.TradeLab;
import com.chanlin.ad.fragment.QDAboutFragment;
import com.chanlin.ad.listener.HomeViewListener;
import com.qmuiteam.qmui.util.QMUIViewHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout;
import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author dustforest
 */
public class HomeInfoView extends QMUIWindowInsetLayout {

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.pull_to_refresh)
    QMUIPullRefreshLayout mPullRefreshLayout;

    private HomeViewListener mHomeViewListener;
    private int mDiffRecyclerViewSaveStateId = QMUIViewHelper.generateViewId();
    private BaseRecyclerAdapter<Trade> mAdapter;
    private List<Trade> mTrades;

    public HomeInfoView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.home_layout, this);
        ButterKnife.bind(this);
        initTopBar();
        initData();
    }

    @TargetApi(11)
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            mTrades = TradeLab.get(getContext()).findTrades();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            loadData();
        }
    }

    private void fetchData () {
        new RemoteDataTask().execute();
    }

    private void loadData() {
        mAdapter.setData(mTrades);
    }

    protected void startFragment(BaseFragment fragment) {
        if (mHomeViewListener != null) {
            mHomeViewListener.startFragment(fragment);
        }
    }

    public void setHomeViewListener(HomeViewListener homeViewListener) {
        mHomeViewListener = homeViewListener;
    }

    private void initTopBar() {
        mTopBar.setTitle("我");

        mTopBar.addRightImageButton(R.mipmap.icon_topbar_about, R.id.topbar_right_about_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                QDAboutFragment fragment = new QDAboutFragment();
                startFragment(fragment);
            }
        });
    }

    private void initData() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });

        mAdapter = new BaseRecyclerAdapter<Trade>(getContext(), null) {
            @Override
            public int getItemLayoutId(int viewType) {
                return android.R.layout.simple_list_item_1;
            }

            @Override
            public void bindData(RecyclerViewHolder holder, int position, Trade item) {
                holder.setText(android.R.id.text1, item.getTitle());
            }
        };
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                Toast.makeText(getContext(), "click position=" + pos, Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        fetchData();
        mPullRefreshLayout.setOnPullListener(new QMUIPullRefreshLayout.OnPullListener() {
            @Override
            public void onMoveTarget(int offset) {

            }

            @Override
            public void onMoveRefreshView(int offset) {

            }

            @Override
            public void onRefresh() {
                mPullRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fetchData();
                        mPullRefreshLayout.finishRefresh();
                    }
                }, 2000);
            }
        });
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        int id = mRecyclerView.getId();
        mRecyclerView.setId(mDiffRecyclerViewSaveStateId);
        super.dispatchSaveInstanceState(container);
        mRecyclerView.setId(id);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        int id = mRecyclerView.getId();
        mRecyclerView.setId(mDiffRecyclerViewSaveStateId);
        super.dispatchRestoreInstanceState(container);
        mRecyclerView.setId(id);
    }
}
