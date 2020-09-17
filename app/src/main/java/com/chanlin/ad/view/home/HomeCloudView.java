package com.chanlin.ad.view.home;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chanlin.ad.R;
import com.chanlin.ad.adapter.ImageAdapter;
import com.chanlin.ad.base.BaseFragment;
import com.chanlin.ad.base.BaseRecyclerAdapter;
import com.chanlin.ad.base.RecyclerViewHolder;
import com.chanlin.ad.config.PushConfig;
import com.chanlin.ad.data.Trade;
import com.chanlin.ad.data.TradeLab;
import com.chanlin.ad.data.User;
import com.chanlin.ad.fragment.QDAboutFragment;
import com.chanlin.ad.listener.HomeViewListener;
import com.kevin.photo_browse.ImageBrowseIntent;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qmuiteam.qmui.util.QMUIViewHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout;
import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.leancloud.AVUser;


/**
 * @author dustforest
 */
public class HomeCloudView extends QMUIWindowInsetLayout {
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
    private Context mContext;
    private PushConfig mPush;

    public HomeCloudView(Context context) {
        super(context);
        mContext = context;
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
        mTopBar.setTitle("首页");

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
                return R.layout.list_item_trade;
            }

            @Override
            public void bindData(RecyclerViewHolder holder, int position, Trade item) {
                TextView tradeAge = (TextView)holder.getView(R.id.trade_age);
                TextView userName = (TextView)holder.getView(R.id.user_name);
                TextView tradeDetails = (TextView)holder.getView(R.id.trade_details);
                ImageView tradeImage = (ImageView)holder.getView(R.id.trade_image);
                GridView gridImage = (GridView)holder.getView(R.id.grid_image);
                GridView gridImageTwoColumns = (GridView)holder.getView(R.id.grid_image_two_columns);
                ImageView adminImage = (ImageView)holder.getView(R.id.admin_image);
                ImageView adImage = (ImageView)holder.getView(R.id.ad_image);
                ImageView tradeUserPhoto = (ImageView)holder.getView(R.id.user_image);
                Button stickButton = (Button) holder.getView(R.id.btn_stick);
                Button likeButton = (Button) holder.getView(R.id.btn_like);
                Button closeButton = (Button) holder.getView(R.id.btn_close);
                Button deleteButton = (Button) holder.getView(R.id.btn_delete);
                Button reportButton = (Button) holder.getView(R.id.btn_report);

                tradeDetails.setText(item.getDetails());
                userName.setText(item.getUserName());
                tradeAge.setText(item.getAge());

                String btnLikeText = item.getVoteCount() == 0 ? mContext.getString(R.string.trade_like) :
                        mContext.getString(R.string.trade_like) + " " + item.getVoteCount();
                likeButton.setText(btnLikeText);

                // 重置所有元素
                stickButton.setVisibility(View.VISIBLE);
                closeButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
                reportButton.setVisibility(View.VISIBLE);
                adminImage.setVisibility(View.VISIBLE);
                adImage.setVisibility(View.VISIBLE);
                tradeImage.setVisibility(View.VISIBLE);
                gridImage.setVisibility(View.VISIBLE);
                gridImageTwoColumns.setVisibility(View.VISIBLE);

                // 投票是必须功能，默认保留
                stickButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
                closeButton.setVisibility(View.GONE);
                adminImage.setVisibility(View.GONE);
                adImage.setVisibility(View.GONE);

                // 管理员图标
                if (item.isAdmin()) {
                    adminImage.setVisibility(View.VISIBLE);
                }

                // 管理员，可以置顶任何消息
                if (User.isAdmin()) {
                    stickButton.setVisibility(View.VISIBLE);
                }

                // 自己发布的消息，可以删除
                AVUser currUser = AVUser.getCurrentUser();
                if (currUser != null && currUser.getBoolean("mobilePhoneVerified")) {
                    String phone = currUser.getString("mobilePhoneNumber");
                    if (phone.equals(item.getUser())) {
                        deleteButton.setVisibility(View.VISIBLE);
                        reportButton.setVisibility(View.GONE);
                        closeButton.setVisibility(View.GONE);
                    }
                }

                // 置顶消息，不允许举报、删除，可以忽略
                if (item.isAd()) {
                    deleteButton.setVisibility(View.GONE);
                    reportButton.setVisibility(View.GONE);
                    closeButton.setVisibility(View.VISIBLE);
                    adImage.setVisibility(View.VISIBLE);
                }

                // 用户头像
                String photoUrl = item.getUserImageThumbnailUrlSmall(mContext);
                if (photoUrl != null) {
                    ImageLoader.getInstance().displayImage(photoUrl, tradeUserPhoto);
                }

                // 消息图片
                int count = item.getImageCount();
                if (count <= 0) {
                    tradeImage.setVisibility(View.GONE);
                    gridImage.setVisibility(View.GONE);
                    gridImageTwoColumns.setVisibility(View.GONE);
                } else if (count == 1) {
                    gridImage.setVisibility(View.GONE);
                    gridImageTwoColumns.setVisibility(View.GONE);

                    String imageUrl = item.getImageThumbnailUrlMedium(mContext, 0);
                    if (imageUrl != null) {
                        ImageLoader.getInstance().displayImage(imageUrl, tradeImage);
                    } else {
                        tradeImage.setVisibility(View.GONE);
                    }

                    final List<String> newImagesUrls = new ArrayList<>();
                    for(int i = 0; i < count; i++) {
                        newImagesUrls.add(item.getImageThumbnailUrl(mContext, i));
                    }
                    tradeImage.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                            ImageBrowseIntent.showUrlImageBrowse(mContext, newImagesUrls, 0);
                        }
                    });
                }else if (count == 4) {
                    tradeImage.setVisibility(View.GONE);
                    gridImage.setVisibility(View.GONE);

                    List<String> imageUrls = new ArrayList<>();
                    for(int i = 0; i < count; i++) {
                        imageUrls.add(item.getImageThumbnailUrlSmall(mContext, i));
                    }

                    ImageAdapter adapter = new ImageAdapter(mContext, imageUrls);
                    gridImageTwoColumns.setAdapter(adapter);

                    final List<String> newImagesUrls = new ArrayList<>();
                    for(int i = 0; i < count; i++) {
                        newImagesUrls.add(item.getImageThumbnailUrl(mContext, i));
                    }
                    gridImageTwoColumns.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ImageBrowseIntent.showUrlImageBrowse(mContext, newImagesUrls, position);
                        }
                    });
                } else {
                    tradeImage.setVisibility(View.GONE);
                    gridImageTwoColumns.setVisibility(View.GONE);

                    List<String> imageUrls = new ArrayList<>();
                    for(int i = 0; i < count; i++) {
                        imageUrls.add(item.getImageThumbnailUrlSmall(mContext, i));
                    }

                    ImageAdapter adapter = new ImageAdapter(mContext, imageUrls);
                    gridImage.setAdapter(adapter);

                    final List<String> newImagesUrls = new ArrayList<>();
                    for(int i = 0; i < count; i++) {
                        newImagesUrls.add(item.getImageThumbnailUrl(mContext, i));
                    }
                    gridImage.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ImageBrowseIntent.showUrlImageBrowse(mContext, newImagesUrls, position);
                        }
                    });
                }

            }
        };
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
//                Toast.makeText(getContext(), "click position=" + pos, Toast.LENGTH_SHORT).show();
//                startFragment(new QDPullRefreshFragment());
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        fetchData();
        mPullRefreshLayout.setOnPullListener(new QMUIPullRefreshLayout.OnPullListener() {
            @Override
            public void onMoveTarget(int offset) {}

            @Override
            public void onMoveRefreshView(int offset) {}

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
