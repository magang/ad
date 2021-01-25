package com.chanlin.ad.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chanlin.ad.R;
import com.chanlin.ad.base.BaseFragment;
import com.chanlin.ad.config.PushConfig;
import com.chanlin.ad.data.User;
import com.chanlin.ad.util.CommonUtils;
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

    @BindView(R.id.tv_inviteNum_value)
    TextView mInviteNumField;

    @BindView(R.id.tv_inviteCode_value)
    TextView mInviteCodeField;

    @BindView(R.id.tv_inviteLink_value)
    TextView mInviteLinkField;

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
        String strInviteCode = "";

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
            strInviteCode = currUser.getString("inviteCode");
        }

        mPhoneField.setText(strPhone);
        mStatusField.setText(strStatus);
        mXjcField.setText(strXjc);
        mTicketField.setText(strTicket);
        mInviteNumField.setText(strInviteNum);
        mInviteCodeField.setText(strInviteCode);
        mInviteLinkField.setText("点击复制");

        mInviteCodeField.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CommonUtils.copyContentToClipboard(mInviteCodeField.getText().toString(), getActivity());
                Toast.makeText(getActivity(), "已复制到粘贴板", Toast.LENGTH_SHORT).show();
            }
        });

        mInviteLinkField.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String inviteText = "『寻街APP』优质资源聚合平台。安卓版本下载：http://d.firim.top/xunjie";
                CommonUtils.copyContentToClipboard(inviteText, getActivity());
                Toast.makeText(getActivity(), "已复制到粘贴板", Toast.LENGTH_SHORT).show();
            }
        });
    }
}