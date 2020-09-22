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
import cn.leancloud.types.AVNull;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class PhoneVerifyFragment extends BaseFragment {
    public static final String TAG = PhoneVerifyFragment.class.getName();

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;

    @BindView(R.id.phone_verify_number)
    EditText mPhoneFiled;

    @BindView(R.id.phone_verify_code)
    EditText mCodeField;

    @BindView(R.id.phone_verify_submit)
    CircularProgressButton mSubmitButton;

    private String mCode;
    private String mPhone;
    private Dialog mProgressDialog;
    private PushConfig mPush;

    @Override
    protected View onCreateView() {
        mPush = PushConfig.get(getActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_phone_verify, null);
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

        mTopBar.setTitle("请输入验证码");
    }

    private void initView() {
        mPhoneFiled.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                mPhone = c.toString();
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
            }

            public void afterTextChanged(Editable c) {
            }
        });

        String verifiedPhone = mPush.getVerifiedPhone();
        if (verifiedPhone != null && !verifiedPhone.equals("")) {
            mPhoneFiled.setText(verifiedPhone);
            mPhone = verifiedPhone;
        }

        mCodeField.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                mCode = c.toString();
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
                    Toast.makeText(getActivity(), "手机号码不能多于11个字符！", Toast.LENGTH_SHORT).show();
                } else if (mCode == null || mCode.equals("")) {
                    Toast.makeText(getActivity(), "验证码不能为空！", Toast.LENGTH_SHORT).show();
                } else if (mCode.length() > 10) {
                    Toast.makeText(getActivity(), "验证码不能多于10个字符！", Toast.LENGTH_SHORT).show();
                } else {
                    mProgressDialog =
                            ProgressDialog.show(getActivity(), "", "正在验证手机号码，请稍候...", true);

                    AVUser.verifyMobilePhoneInBackground(mCode).subscribe(new Observer<AVNull>() {
                        public void onSubscribe(Disposable disposable) {}
                        public void onNext(AVNull avNull) {
                            mProgressDialog.dismiss();
                            mSubmitButton.setProgress(100);
                            Toast.makeText(getActivity(), "注册成功", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Phone verify successfully.");
                            mPush.setPhone(mPhone);
                            startFragment(new LoginFragment());
                        }
                        public void onError(Throwable throwable) {
                            // 验证码不正确
                            mProgressDialog.dismiss();
                            Toast.makeText(getActivity(), "验证码错误，请重试！", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Phone verify failed:" + throwable.getMessage());
                        }
                        public void onComplete() {}
                    });
                }
            }
        });
    }
}