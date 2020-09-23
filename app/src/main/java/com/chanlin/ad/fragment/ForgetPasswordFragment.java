package com.chanlin.ad.fragment;

import android.app.Dialog;
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
import cn.leancloud.types.AVNull;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ForgetPasswordFragment extends BaseFragment {
    public static final String TAG = ForgetPasswordFragment.class.getName();

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;

    @BindView(R.id.reset_phone)
    EditText mPhoneField;

    @BindView(R.id.reset_phone_submit)
    CircularProgressButton mSubmitButton;

    private String mPassword;
    private String mPhone;
    private Dialog mProgressDialog;
    private PushConfig mPush;

    @Override
    protected View onCreateView() {
        mPush = PushConfig.get(getActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_forget_password, null);
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

        mTopBar.setTitle("重置密码");
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

        mSubmitButton.setIndeterminateProgressMode(true);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (mPhone == null || mPhone.equals("")) {
                    Toast.makeText(getActivity(), "手机号码不能为空！", Toast.LENGTH_SHORT).show();
                } else if (mPhone.length() > 11) {
                    Toast.makeText(getActivity(), "手机号码不能多于11位！", Toast.LENGTH_SHORT).show();
                } else {
                    AVUser.requestPasswordResetBySmsCodeInBackground(mPhone).subscribe(new Observer<AVNull>() {
                        public void onSubscribe(Disposable disposable) {}
                        public void onNext(AVNull avNull) {
                            mSubmitButton.setProgress(100);
                            Log.d(TAG, "requestPasswordResetBySmsCodeInBackground successfully.");
                            mPush.setVerifiedPhone(mPhone.trim());
                            startFragment(new ResetPasswordFragment());
                        }
                        public void onError(Throwable throwable) {
                            String msg = throwable.getMessage().toLowerCase();
                            if (msg.equals("mobile phone number isn't verified.")) {
                                mPush.setVerifiedPhone(mPhone.trim());
                                AVUser.requestMobilePhoneVerifyInBackground(mPhone.trim()).blockingSubscribe();
                                Toast.makeText(getActivity(), "请先验证该手机号码！", Toast.LENGTH_SHORT).show();
                                startFragment(new PhoneVerifyFragment());
                            } else {
                                Toast.makeText(getActivity(), "获取验证码失败，请一分钟后再试!", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "requestPasswordResetBySmsCodeInBackground failed: " + throwable.getMessage());
                            }
                        }
                        public void onComplete() {}
                    });
                }
            }
        });

    }
}