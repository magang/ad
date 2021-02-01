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

import org.apache.commons.lang3.StringUtils;

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
        if (!StringUtils.isEmpty(strInviteCode)) {
            mInviteCodeField.setText(strInviteCode + "（点击复制）");
        }
        mInviteLinkField.setText("点击复制");

        mInviteCodeField.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    CommonUtils.copyContentToClipboard(mInviteCodeField.getText().toString().split("（")[0], getActivity());
                    Toast.makeText(getActivity(), "已复制到粘贴板", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    ;
                }
            }
        });

        mInviteLinkField.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String inviteCode = "";
                try {
                    inviteCode = mInviteCodeField.getText().toString().split("（")[0];
                } catch (Exception e) {
                    ;
                }
                String inviteText = "〔寻街APP〕优质资源发布平台。用户可对APP上发布的消息进行投票，每投一票，可获得一元代金券。代金券可用于在APP上团购商品。每个用户每天可免费获得10张投票券，每邀请一名新用户，每天可以额外获得10张投票券。可以在APP上我的账户中复制自己的邀请链接，并发送给朋友们。安卓版本下载：http://d.firim.top/xunjie"
                        + " 注册账号时请填写邀请码: " + inviteCode;
                CommonUtils.copyContentToClipboard(inviteText, getActivity());
                Toast.makeText(getActivity(), "已复制到粘贴板", Toast.LENGTH_SHORT).show();
            }
        });
    }
}