package com.chanlin.ad.view.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chanlin.ad.R;
import com.chanlin.ad.base.BaseFragment;
import com.chanlin.ad.config.PushConfig;
import com.chanlin.ad.data.User;
import com.chanlin.ad.fragment.AccountFragment;
import com.chanlin.ad.fragment.LoginFragment;
import com.chanlin.ad.fragment.RegisterFragment;
import com.chanlin.ad.listener.HomeViewListener;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author dustforest
 */
public class HomeInfoView extends QMUIWindowInsetLayout {
    private static final String TAG = HomeInfoView.class.getName();

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;

    @BindView(R.id.groupListView)
    QMUIGroupListView mGroupListView;

    private HomeViewListener mHomeViewListener;
    private Context mContext;
    private PushConfig mPush;

    public HomeInfoView(Context context) {
        super(context);
        mContext = context;
        mPush = PushConfig.get(mContext);
        LayoutInflater.from(context).inflate(R.layout.info_layout, this);
        ButterKnife.bind(this);
        initTopBar();
        initGroupListView();
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
        mTopBar.setTitle("账户");

//        mTopBar.addRightImageButton(R.mipmap.icon_topbar_about, R.id.topbar_right_about_button).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                QDAboutFragment fragment = new QDAboutFragment();
//                startFragment(fragment);
//            }
//        });
    }

    private void initGroupListView() {
        QMUICommonListItemView registerItem = mGroupListView.createItemView("注册");
        registerItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView loginItem = mGroupListView.createItemView("登录");
        loginItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView logoutItem = mGroupListView.createItemView("退出登录");
        loginItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView accountItem = mGroupListView.createItemView("账户");
        accountItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof QMUICommonListItemView) {
                    String text = ((QMUICommonListItemView) v).getText().toString();
//                    Toast.makeText(mContext, text + " is Clicked", Toast.LENGTH_SHORT).show();
                    switch(text) {
                        case "注册":
                            startFragment(new RegisterFragment());
                            break;
                        case "登录":
                            startFragment(new LoginFragment());
                            break;
                        case "账户":
                            User.syncUser();
                            startFragment(new AccountFragment());
                            break;
                        case "退出登录":
                            User.logout();
                            Toast.makeText(mContext, "已退出", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            break;
                    }

                }
            }
        };

        int size = QMUIDisplayHelper.dp2px(getContext(), 20);
        QMUIGroupListView.newSection(getContext())
                .setTitle(null)
                .setDescription(null)
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(registerItem, onClickListener)
                .addItemView(loginItem, onClickListener)
                .addItemView(accountItem, onClickListener)
                .setMiddleSeparatorInset(QMUIDisplayHelper.dp2px(getContext(), 16), 0)
                .addTo(mGroupListView);

        QMUIGroupListView.newSection(getContext())
                .setTitle(null)
                .setDescription(null)
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(logoutItem, onClickListener)
                .setMiddleSeparatorInset(QMUIDisplayHelper.dp2px(getContext(), 16), 0)
                .addTo(mGroupListView);
    }
}
