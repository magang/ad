package com.chanlin.ad.fragment.home;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.chanlin.ad.R;
import com.chanlin.ad.base.BaseFragment;
import com.chanlin.ad.listener.HomeViewListener;
import com.chanlin.ad.model.CustomEffect;
import com.chanlin.ad.view.home.HomeCloudView;
import com.chanlin.ad.view.home.HomeInfoView;
import com.qmuiteam.qmui.arch.effect.MapEffect;
import com.qmuiteam.qmui.arch.effect.QMUIFragmentEffectHandler;
import com.qmuiteam.qmui.arch.effect.QMUIFragmentMapEffectHandler;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout;
import com.qmuiteam.qmui.widget.tab.QMUITab;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author cginechen
 * @date 2016-10-19
 */

public class HomeFragment extends BaseFragment {
    private final static String TAG = HomeFragment.class.getSimpleName();

    @BindView(R.id.pager)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    QMUITabSegment mTabSegment;
    private HashMap<Pager, QMUIWindowInsetLayout> mPages;
    private PagerAdapter mPagerAdapter = new PagerAdapter() {

        private int mChildCount = 0;

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return mPages.size();
        }

        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            QMUIWindowInsetLayout page = mPages.get(Pager.getPagerFromPosition(position));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(page, params);
            return page;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            if (mChildCount == 0) {
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }

        @Override
        public void notifyDataSetChanged() {
            mChildCount = getCount();
            super.notifyDataSetChanged();
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        registerEffect(this, new QMUIFragmentMapEffectHandler() {
            @Override
            public boolean shouldHandleEffect(@NonNull MapEffect effect) {
                return effect.getValue("interested_type_key") != null;
            }

            @Override
            public void handleEffect(@NonNull MapEffect effect) {
                Object value = effect.getValue("interested_value_key");
                if(value instanceof String){
                    Toast.makeText(context, ((String)value), Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerEffect(this, new QMUIFragmentEffectHandler<CustomEffect>() {
            @Override
            public boolean shouldHandleEffect(@NonNull CustomEffect effect) {
                return true;
            }

            @Override
            public void handleEffect(@NonNull CustomEffect effect) {
                Toast.makeText(context, effect.getContent(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void handleEffect(@NonNull List<CustomEffect> effects) {
               // we can only handle the last effect.
               handleEffect(effects.get(effects.size() - 1));
            }
        });
    }

    @Override
    protected View onCreateView() {
        FrameLayout layout = (FrameLayout) LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home, null);
        ButterKnife.bind(this, layout);
        initTabs();
        initPagers();
        return layout;
    }

    private void initTabs() {
        QMUITabBuilder builder = mTabSegment.tabBuilder();
        builder.setTypeface(null, Typeface.DEFAULT_BOLD);
        builder.setSelectedIconScale(1.2f)
                .setTextSize(QMUIDisplayHelper.sp2px(getContext(), 13), QMUIDisplayHelper.sp2px(getContext(), 15))
                .setDynamicChangeIconColor(false);
        QMUITab cloud = builder
                .setNormalDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.icon_tabbar_cloud))
                .setSelectedDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.icon_tabbar_cloud_selected))
                .setText("消息")
                .build(getContext());
        QMUITab info = builder
                .setNormalDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.icon_tabbar_info))
                .setSelectedDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.icon_tabbar_info_selected))
                .setText("账户")
                .build(getContext());

        mTabSegment.addTab(cloud).addTab(info);
    }

    private void initPagers() {
        HomeViewListener homeViewListener = new HomeViewListener() {
            @Override
            public void startFragment(BaseFragment fragment) {
                HomeFragment.this.startFragment(fragment);
            }
        };

        mPages = new HashMap<>();

        HomeCloudView homeCloudView = new HomeCloudView(getActivity());
        homeCloudView.setHomeViewListener(homeViewListener);
        mPages.put(Pager.CLOUD, homeCloudView);

        HomeInfoView homeInfoView = new HomeInfoView(getActivity());
        homeInfoView.setHomeViewListener(homeViewListener);
        mPages.put(Pager.INFO, homeInfoView);

        mViewPager.setAdapter(mPagerAdapter);
        mTabSegment.setupWithViewPager(mViewPager, false);
    }

    enum Pager {
        CLOUD, INFO;

        public static Pager getPagerFromPosition(int position) {
            switch (position) {
                case 0:
                    return CLOUD;
                case 1:
                    return INFO;
                default:
                    return CLOUD;
            }
        }
    }

    @Override
    protected boolean canDragBack() {
        return false;
    }

    @Override
    public Object onLastFragmentFinish() {
        return null;
    }
}