package com.chanlin.ad;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chanlin.ad.base.BaseFragmentActivity;
import com.chanlin.ad.fragment.QDPullRefreshFragment;
import com.chanlin.ad.fragment.QDWebExplorerFragment;
import com.chanlin.ad.fragment.home.HomeFragment;
import com.chanlin.ad.manager.QDSkinManager;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.arch.QMUIFragmentActivity;
import com.qmuiteam.qmui.arch.annotation.DefaultFirstFragment;
import com.qmuiteam.qmui.arch.annotation.FirstFragments;
import com.qmuiteam.qmui.arch.annotation.LatestVisitRecord;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import static com.chanlin.ad.fragment.QDWebExplorerFragment.EXTRA_TITLE;
import static com.chanlin.ad.fragment.QDWebExplorerFragment.EXTRA_URL;

@FirstFragments(
        value = {
                HomeFragment.class,
                QDPullRefreshFragment.class
        })
@DefaultFirstFragment(HomeFragment.class)
@LatestVisitRecord
public class QDMainActivity extends BaseFragmentActivity {

    private QMUISkinManager.OnSkinChangeListener mOnSkinChangeListener = new QMUISkinManager.OnSkinChangeListener() {
        @Override
        public void onSkinChange(QMUISkinManager skinManager, int oldSkin, int newSkin) {
            if (newSkin == QDSkinManager.SKIN_WHITE) {
                QMUIStatusBarHelper.setStatusBarLightMode(QDMainActivity.this);
            } else {
                QMUIStatusBarHelper.setStatusBarDarkMode(QDMainActivity.this);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUISkinManager skinManager = QMUISkinManager.defaultInstance(this);
        setSkinManager(skinManager);
        mOnSkinChangeListener.onSkinChange(skinManager, -1, skinManager.getCurrentSkin());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getSkinManager() != null) {
            getSkinManager().addSkinChangeListener(mOnSkinChangeListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (getSkinManager() != null) {
            getSkinManager().removeSkinChangeListener(mOnSkinChangeListener);
        }
    }

    public static Intent createWebExplorerIntent(Context context, String url, String title) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_URL, url);
        bundle.putString(EXTRA_TITLE, title);
        return of(context, QDWebExplorerFragment.class, bundle);
    }

    public static Intent of(@NonNull Context context,
                            @NonNull Class<? extends QMUIFragment> firstFragment) {
        return QMUIFragmentActivity.intentOf(context, QDMainActivity.class, firstFragment);
    }

    public static Intent of(@NonNull Context context,
                            @NonNull Class<? extends QMUIFragment> firstFragment,
                            @Nullable Bundle fragmentArgs) {
        return QMUIFragmentActivity.intentOf(context, QDMainActivity.class, firstFragment, fragmentArgs);
    }
}
