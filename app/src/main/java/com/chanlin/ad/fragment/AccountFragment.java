package com.chanlin.ad.fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chanlin.ad.R;
import com.chanlin.ad.base.BaseFragment;
import com.chanlin.ad.config.PushConfig;
import com.chanlin.ad.data.User;
import com.chanlin.ad.util.CommonUtils;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.leancloud.AVObject;
import cn.leancloud.AVRelation;
import cn.leancloud.AVUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

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

    @BindView(R.id.tv_withdrawAddress_value)
    TextView mWithdrawAddressField;

    @BindView(R.id.tv_ticket_value)
    TextView mTicketField;

    @BindView(R.id.tv_inviteNum_value)
    TextView mInviteNumField;

    @BindView(R.id.tv_inviteCode_value)
    TextView mInviteCodeField;

    @BindView(R.id.tv_inviteLink_value)
    TextView mInviteLinkField;

    @BindView(R.id.tv_zhifubao_value)
    TextView mZhifubaoField;

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
        String strWithdrawAddress = "";
        String strZhifubao = "";

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
            strXjc = String.format("%.0f", currUser.getDouble("xjc")) + "（点击提取）";
            strTicket = String.format("%.0f", currUser.getDouble("ticket")) + " 张";
            strInviteNum = String.format("%.0f", currUser.getDouble("inviteNum")) + " 人";
            strInviteCode = currUser.getString("inviteCode");
            strWithdrawAddress = currUser.getString("adcAddress");
            strZhifubao = currUser.getString("zhifubao");
        }

        mPhoneField.setText(strPhone);
        mStatusField.setText(strStatus);
        mZhifubaoField.setText(strZhifubao);
        mXjcField.setText(strXjc);
        mWithdrawAddressField.setText(strWithdrawAddress);
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
                String inviteText = "〔注意力银行〕优质资源发布平台。用户可对APP上发布的消息进行投票，每投一票，可获得一枚ADC。ADC是注意力银行APP中的广告代币，用于支付在APP上发布广告的费用。每个用户每天可免费获得一定数量的投票券，每邀请一名新用户，每天可以额外获得1倍的免费投票券。可以在APP上我的账户中复制自己的邀请链接，并发送给朋友们。安卓版本下载：http://d.firim.top/xunjie"
                        + " 注册账号时请填写邀请码: " + inviteCode;
                CommonUtils.copyContentToClipboard(inviteText, getActivity());
                Toast.makeText(getActivity(), "已复制到粘贴板", Toast.LENGTH_SHORT).show();
            }
        });

        mXjcField.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
                final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(getActivity());
                builder.setTitle("提取ADC")
                        .setSkinManager(QMUISkinManager.defaultInstance(getContext()))
                        .setPlaceholder("提取数量")
                        .setInputType(InputType.TYPE_CLASS_NUMBER)
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction("确定", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                String mWithdrawCount = builder.getEditText().getText().toString();
                                AVUser currUser = AVUser.getCurrentUser();
                                String mWithdrawTo = "";
                                if (currUser != null && currUser.getBoolean("mobilePhoneVerified")) {
                                    mWithdrawTo = currUser.getString("adcAddress");
                                }

                                if (StringUtils.isBlank(mWithdrawTo)) {
                                    Toast.makeText(getActivity(), "提取地址为空，请联系管理员添加！", Toast.LENGTH_SHORT).show();
                                } else if (!User.isMemberUser()) {
                                    Toast.makeText(getActivity(), "只有会员才能提币，请联系管理员添加会员！", Toast.LENGTH_SHORT).show();
                                } else if (StringUtils.isBlank(mWithdrawCount)) {
                                    Toast.makeText(getActivity(), "提取数量不能为空！", Toast.LENGTH_SHORT).show();
                                } else if (!isInteger(mWithdrawCount)) {
                                    Toast.makeText(getActivity(), "提取数量必须是整数！", Toast.LENGTH_SHORT).show();
                                } else if (Double.parseDouble(mWithdrawCount) <= 0) {
                                    Toast.makeText(getActivity(), "提取数量不能为零！", Toast.LENGTH_SHORT).show();
                                } else if (Double.parseDouble(mWithdrawCount) > currUser.getDouble("xjc")) {
                                    Toast.makeText(getActivity(), "ADC不足！", Toast.LENGTH_SHORT).show();
                                } else {
                                    dialog.dismiss();
                                    String mLocal = "null";
                                    String mLocalName = "";

                                    final AVObject withdraw = new AVObject("AdcWithdraw");
                                    withdraw.put("status", "submit");
                                    withdraw.put("to", mWithdrawTo);
                                    withdraw.put("count", Double.parseDouble(mWithdrawCount));

                                    if (currUser != null && currUser.getBoolean("mobilePhoneVerified")) {
                                        mLocal = currUser.getString("mobilePhoneNumber");
                                        mLocalName = currUser.getString("nickname");
                                    }

                                    withdraw.put("from", mLocal.trim());
                                    withdraw.put("fromName", mLocalName);

                                    AVRelation<AVObject> xjcWithdrawRelationToUser = withdraw.getRelation("userRelation");
                                    xjcWithdrawRelationToUser.add(currUser);

                                    withdraw.saveInBackground().subscribe(new Observer<AVObject>() {
                                        public void onSubscribe(Disposable disposable) {}
                                        public void onNext(AVObject todo) {
                                            // 成功保存之后，执行其他逻辑
                                            Log.d(TAG, "ADC withdraw successfully.");
                                            Toast.makeText(getActivity(), "已提交提取申请！", Toast.LENGTH_SHORT).show();
                                        }
                                        public void onError(Throwable throwable) {
                                            // 异常处理
                                            Log.e(TAG, "ADC withdraw failed: " + throwable.getMessage());
                                        }
                                        public void onComplete() {}
                                    });
                                }
                            }
                        })
                        .create(mCurrentDialogStyle).show();
            }
        });
    }

    //判断整数（int）
    private boolean isInteger(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    //判断浮点数（double和float）
    private boolean isDouble(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();
    }
}