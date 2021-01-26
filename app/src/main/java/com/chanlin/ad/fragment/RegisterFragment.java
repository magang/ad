package com.chanlin.ad.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.chanlin.ad.R;
import com.chanlin.ad.base.BaseFragment;
import com.chanlin.ad.config.PushConfig;
import com.chanlin.ad.fragment.home.HomeFragment;
import com.chanlin.ad.view.button.CircularProgressButton;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.leancloud.AVUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class RegisterFragment extends BaseFragment {
    public static final String TAG = RegisterFragment.class.getName();

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;

    @BindView(R.id.register_phone)
    EditText mPhoneField;

    @BindView(R.id.register_password)
    EditText mPasswordField;

    @BindView(R.id.register_password_confirm)
    EditText mPasswordConfirmField;

    @BindView(R.id.register_invitor_code)
    EditText mInvitorCodeField;

    @BindView(R.id.register_submit)
    CircularProgressButton mSubmitButton;

    private String mPassword;
    private String mPasswordConfirm;
    private String mInviteCode;
    private String mPhone;
    private String mStatus = "on";
    private String mTag = "";
    private Dialog mProgressDialog;
    private PushConfig mPush;

    @Override
    protected View onCreateView() {
        mPush = PushConfig.get(getActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_register, null);
        ButterKnife.bind(this, root);

        initTopBar();
        initView();

        return root;
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack(HomeFragment.class);
            }
        });

        mTopBar.setTitle("注册账号");
    }

    private void initView() {
        mPhoneField.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                mPhone = c.toString();
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
            }

            public void afterTextChanged(Editable c) {
            }
        });

        mPasswordField.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                mPassword = c.toString();
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
            }

            public void afterTextChanged(Editable c) {
            }
        });

        mPasswordConfirmField.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                mPasswordConfirm = c.toString();
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
            }

            public void afterTextChanged(Editable c) {
            }
        });

        mInvitorCodeField.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                mInviteCode = c.toString();
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
            }

            public void afterTextChanged(Editable c) {
            }
        });

        mSubmitButton.setIndeterminateProgressMode(true);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (mPhone == null || mPhone.equals("")) {
                    Toast.makeText(getActivity(), "手机号码不能为空！", Toast.LENGTH_SHORT).show();
                } else if (mPhone.length() > 11) {
                    Toast.makeText(getActivity(), "手机号码不能多于11位！", Toast.LENGTH_SHORT).show();
                } else if (mPassword == null || mPassword.equals("")) {
                    Toast.makeText(getActivity(), "密码不能为空！", Toast.LENGTH_SHORT).show();
                } else if (mPassword.length() > 30) {
                    Toast.makeText(getActivity(), "密码不能多于30个字符！", Toast.LENGTH_SHORT).show();
                } else if (mPasswordConfirm == null || mPasswordConfirm.equals("")) {
                    Toast.makeText(getActivity(), "密码确认不能为空！", Toast.LENGTH_SHORT).show();
                } else if (mPasswordConfirm.length() > 30) {
                    Toast.makeText(getActivity(), "密码确认不能多于30个字符！", Toast.LENGTH_SHORT).show();
                } else if (!mPasswordConfirm.equals(mPassword)) {
                    Toast.makeText(getActivity(), "两次输入的密码不一致！", Toast.LENGTH_SHORT).show();
                } else if (mInviteCode == null || mInviteCode.equals("")) {
                    Toast.makeText(getActivity(), "邀请码不能为空！", Toast.LENGTH_SHORT).show();
                } else if (mInviteCode.length() != 6) {
                    Toast.makeText(getActivity(), "邀请码是6位数字！", Toast.LENGTH_SHORT).show();
                } else {

                    AVUser mUser = new AVUser();
                    mUser.setUsername(mPhone.trim());
                    mUser.setPassword(mPassword);
                    mUser.setMobilePhoneNumber(mPhone.trim());
                    mUser.put("status", mStatus);
                    mUser.put("tag", mTag);
                    mUser.put("invitor", mInviteCode);

                    mProgressDialog =
                            ProgressDialog.show(getActivity(), "", "正在注册，请稍候...", true);

                    mUser.signUpInBackground().subscribe(new Observer<AVUser>() {
                        public void onSubscribe(Disposable disposable) {}
                        public void onNext(AVUser user) {
                            // 注册成功
                            mProgressDialog.dismiss();
                            mSubmitButton.setProgress(100);
                            Log.d(TAG, "User signup successfully.");
                            mPush.setVerifiedPhone(mPhone.trim());
//                                Toast.makeText(getActivity(), "注册成功", Toast.LENGTH_SHORT).show();
                            startFragment(new PhoneVerifyFragment());
                        }
                        public void onError(Throwable throwable) {
                            // 注册失败（通常是因为用户名已被使用）
                            mProgressDialog.dismiss();
                            Toast.makeText(getActivity(), "手机号码已被注册！", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "User signup failed: " + throwable.getMessage());
                        }
                        public void onComplete() {}
                    });
                }
            }
        });

    }
}