package com.chanlin.ad.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chanlin.ad.R;
import com.chanlin.ad.base.BaseFragment;
import com.chanlin.ad.config.PushConfig;
import com.chanlin.ad.data.User;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.leancloud.AVUser;

public class AccountFragment extends BaseFragment {
    public static final String TAG = AccountFragment.class.getName();

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;

    @BindView(R.id.tv_phone_value)
    TextView mPhoneField;

    @BindView(R.id.tv_status_value)
    TextView mStatusField;

    @BindView(R.id.tv_xjc_value)
    TextView mXjcField;

    @BindView(R.id.tv_ticket_value)
    TextView mTicketField;

    @BindView(R.id.tv_invite_value)
    TextView mInviteField;

    private PushConfig mPush;
    private AVUser currUser;

    @Override
    protected View onCreateView() {
        mPush = PushConfig.get(getActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_account, null);
        ButterKnife.bind(this, root);

        initTopBar();
        initView();

        return root;
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        mTopBar.setTitle("我的账户");
    }

    private void initView() {
        String strPhone = "";
        String strStatus = "";
        String strXjc = "";
        String strTicket = "";
        String strPhoneVerified = "";
        String strInviteNum = "";

        currUser = AVUser.getCurrentUser();
        if (currUser != null) {

            if (currUser.getBoolean("mobilePhoneVerified")) {
                strPhoneVerified = "已验证";
            } else {
                strPhoneVerified = "未验证";
            }

            String adEnd = "（至 " + User.getAdEnd(".") + "）";
            String memberEnd = "（至 " + User.getMemberEnd(".") + "）";

            int rank = currUser.getInt("rank");
            if (rank == 3) {
//                strStatus = "普通会员";
                strStatus = "用户";
            } else if (rank == 4) {
//                strStatus = "高级会员"  + memberEnd;
                strStatus = "会员";
            } else if (rank == 5) {
                strStatus = "超级会员"  + memberEnd;
            } else if (rank == 6) {
                strStatus = "钻石会员"  + memberEnd;
            } else if (rank == 7) {
                strStatus = "超级会员"  + memberEnd;
            } else if (rank == 8) {
                strStatus = "城市经理";
            } else if (rank == 9) {
                strStatus = "合伙人";
            } else if (rank == 10) {
                strStatus = "管理员";
            } else {
                strStatus = "异常";
            }

            if (currUser.getInt("report") >= 10) {
                strStatus = "禁言";
            }

            strPhone = currUser.getString("mobilePhoneNumber") + "（" + strPhoneVerified + "）";
            strXjc = String.format("%.0f", currUser.getDouble("xjc")) + " 元";
            strTicket = String.format("%.0f", currUser.getDouble("ticket")) + " 张";
            strInviteNum = String.format("%.0f", currUser.getDouble("inviteNum")) + " 人";
        }

        mPhoneField.setText(strPhone);
        mStatusField.setText(strStatus);
        mXjcField.setText(strXjc);
        mTicketField.setText(strTicket);
        mInviteField.setText(strInviteNum);
    }
}