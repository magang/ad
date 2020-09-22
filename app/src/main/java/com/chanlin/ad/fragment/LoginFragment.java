package com.chanlin.ad.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chanlin.ad.R;
import com.chanlin.ad.base.BaseFragment;
import com.chanlin.ad.config.PushConfig;
import com.chanlin.ad.data.User;
import com.chanlin.ad.fragment.home.HomeFragment;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.leancloud.AVUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LoginFragment extends BaseFragment {
    public static final String TAG = LoginFragment.class.getName();

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;

    @BindView(R.id.login_phone)
    EditText mPhoneField;

    @BindView(R.id.login_password)
    EditText mPasswordField;

    @BindView(R.id.login_submit)
    Button mSubmitButton;

    private String mPassword;
    private String mPhone;
    private Dialog mProgressDialog;
    private PushConfig mPush;

    @Override
    protected View onCreateView() {
        mPush = PushConfig.get(getActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_login, null);
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

        mTopBar.addRightTextButton("注册", R.id.topbar_right_register_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFragment(new RegisterFragment());
            }
        });

        mTopBar.setTitle("登录");
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

        String lastLoginPhone = mPush.getPhone();
        if (lastLoginPhone != null && !lastLoginPhone.equals("")) {
            mPhoneField.setText(lastLoginPhone);
            mPhone = lastLoginPhone;
        }

        mPasswordField.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                mPassword = c.toString();
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
            }

            public void afterTextChanged(Editable c) {
            }
        });

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
                } else {

                    mProgressDialog =
                            ProgressDialog.show(getActivity(), "", "正在登陆，请稍候...", true);
                    AVUser.loginByMobilePhoneNumber(mPhone, mPassword).subscribe(new Observer<AVUser>() {
                        public void onSubscribe(Disposable disposable) {}
                        public void onNext(AVUser user) {
                            // 登录成功
                            mProgressDialog.dismiss();
                            mPush.setPhone(mPhone);
                            mPush.setPassword(mPassword);
                            User.updateLocalParams(getActivity());
                            Log.d(TAG, "User login successfully.");
                            Toast.makeText(getActivity(), "登陆成功", Toast.LENGTH_SHORT).show();
                            popBackStack(HomeFragment.class);
                        }
                        public void onError(Throwable throwable) {
                            // 登录失败（可能是密码错误）
                            mProgressDialog.dismiss();
                            String msg = throwable.getMessage().toLowerCase();
                            if (msg.equals("mobile phone number isn't verified.")) {
                                mPush.setVerifiedPhone(mPhone.trim());
                                AVUser.requestMobilePhoneVerifyInBackground(mPhone.trim()).blockingSubscribe();
                                Toast.makeText(getActivity(), "请先验证该手机号码！", Toast.LENGTH_SHORT).show();
                                startFragment(new PhoneVerifyFragment());
                            } else {
                                Toast.makeText(getActivity(), "手机号码或者密码不正确！", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "User login failed:" + throwable.getMessage());
                            }
                        }
                        public void onComplete() {}
                    });


                }
            }
        });
    }
}