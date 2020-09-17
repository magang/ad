package com.chanlin.ad.fragment.home;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chanlin.ad.R;
import com.chanlin.ad.base.BaseFragment;
import com.chanlin.ad.base.BaseRecyclerAdapter;
import com.chanlin.ad.base.RecyclerViewHolder;
import com.chanlin.ad.data.Trade;
import com.chanlin.ad.data.TradeLab;
import com.chanlin.ad.fragment.QDAboutFragment;
import com.chanlin.ad.model.QDItemDescription;
import com.qmuiteam.qmui.util.QMUIViewHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout;
import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author cginechen
 * @date 2016-10-20
 */

public abstract class HomeController extends QMUIWindowInsetLayout {

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.pull_to_refresh)
    QMUIPullRefreshLayout mPullRefreshLayout;

    private HomeControlListener mHomeControlListener;
    private ItemAdapter mItemAdapter;
    private int mDiffRecyclerViewSaveStateId = QMUIViewHelper.generateViewId();
    private BaseRecyclerAdapter<Trade> mAdapter;
    private List<Trade> mTrades;

    public HomeController(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.home_layout, this);
        ButterKnife.bind(this);
        initTopBar();
//        initRecyclerView();
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
            onDataLoaded();
//            if (mTrades == null || mTrades.isEmpty()) {
//                blankPage();
//            }
//            newAwardNotice();
//            newCommentNotice();
//            newTicketNotice();
        }
    }

    private void fetchData () {
        new RemoteDataTask().execute();
    }

    protected void startFragment(BaseFragment fragment) {
        if (mHomeControlListener != null) {
            mHomeControlListener.startFragment(fragment);
        }
    }

    public void setHomeControlListener(HomeControlListener homeControlListener) {
        mHomeControlListener = homeControlListener;
    }

    protected abstract String getTitle();

    private void initTopBar() {
        mTopBar.setTitle(getTitle());

        mTopBar.addRightImageButton(R.mipmap.icon_topbar_about, R.id.topbar_right_about_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                QDAboutFragment fragment = new QDAboutFragment();
                startFragment(fragment);
            }
        });
    }

    private void initRecyclerView() {
        mItemAdapter = getItemAdapter();
        mItemAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                QDItemDescription item = mItemAdapter.getItem(pos);
//                try {
//                    BaseFragment fragment = item.getDemoClass().newInstance();
//                    startFragment(fragment);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });
        mRecyclerView.setAdapter(mItemAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration( new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL));
        mRecyclerView.setItemAnimator( new DefaultItemAnimator());
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
                holder.setText(android.R.id.text1, item.getDetails());
            }
        };
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                Toast.makeText(getContext(), "click position=" + pos, Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
//        onDataLoaded();
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

    private void onDataLoaded() {
        mAdapter.setData(mTrades);
    }

    protected abstract ItemAdapter getItemAdapter();

    public interface HomeControlListener {
        void startFragment(BaseFragment fragment);
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

    static class ItemAdapter extends BaseRecyclerAdapter<QDItemDescription> {

        public ItemAdapter(Context ctx, List<QDItemDescription> data) {
            super(ctx, data);
        }

        @Override
        public int getItemLayoutId(int viewType) {
            return R.layout.home_item_layout;
        }

        @Override
        public void bindData(RecyclerViewHolder holder, int position, QDItemDescription item) {
            holder.getTextView(R.id.item_name).setText(item.getName());
//            if (item.getIconRes() != 0) {
//                holder.getImageView(R.id.item_icon).setImageResource(item.getIconRes());
//            }
        }
    }
}
