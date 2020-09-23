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
import com.chanlin.ad.data.User;
import com.chanlin.ad.fragment.home.HomeFragment;
import com.chanlin.ad.view.button.CircularProgressButton;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.leancloud.AVUser;
import cn.leancloud.types.AVNull;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ResetPasswordFragment extends BaseFragment {
    public static final String TAG = ResetPasswordFragment.class.getName();

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;

    @BindView(R.id.reset_code)
    EditText mCodeField;

    @BindView(R.id.reset_new_pwd)
    EditText mNewPwdField;

    @BindView(R.id.reset_new_pwd_confirm)
    EditText mNewPwdConfirmField;

    @BindView(R.id.reset_pwd_submit)
    CircularProgressButton mSubmitButton;

    private String mCode;
    private String mNewPwd;
    private String mNewPwdConfirm;
    private Dialog mProgressDialog;
    private PushConfig mPush;

    @Override
    protected View onCreateView() {
        mPush = PushConfig.get(getActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_reset_password, null);
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
        mCodeField.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                mCode = c.toString();
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
            }

            public void afterTextChanged(Editable c) {
            }
        });

        mNewPwdField.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                mNewPwd = c.toString();
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
            }

            public void afterTextChanged(Editable c) {
            }
        });

        mNewPwdConfirmField.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                mNewPwdConfirm = c.toString();
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
            }

            public void afterTextChanged(Editable c) {
            }
        });

        mSubmitButton.setIndeterminateProgressMode(true);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mCode == null || mCode.equals("")) {
                    Toast.makeText(getActivity(), "验证码不能为空！", Toast.LENGTH_SHORT).show();
                } else if (mCode.length() > 30) {
                    Toast.makeText(getActivity(), "验证码不能多于30个字符！", Toast.LENGTH_SHORT).show();
                } else if (mNewPwd == null || mNewPwd.equals("")) {
                    Toast.makeText(getActivity(), "新密码不能为空！", Toast.LENGTH_SHORT).show();
                } else if (mNewPwd.length() > 30) {
                    Toast.makeText(getActivity(), "新密码不能多于30个字符！", Toast.LENGTH_SHORT).show();
                } else if (mNewPwdConfirm == null || mNewPwdConfirm.equals("")) {
                    Toast.makeText(getActivity(), "新密码确认不能为空！", Toast.LENGTH_SHORT).show();
                } else if (mNewPwdConfirm.length() > 30) {
                    Toast.makeText(getActivity(), "新密码确认不能多于30个字符！", Toast.LENGTH_SHORT).show();
                } else if (!mNewPwdConfirm.equals(mNewPwd)) {
                    Toast.makeText(getActivity(), "两次输入的新密码不一致！", Toast.LENGTH_SHORT).show();
                } else {
                    mProgressDialog =
                            ProgressDialog.show(getActivity(), "", "正在重置密码，请稍候...", true);

                    AVUser.resetPasswordBySmsCodeInBackground(mCode, mNewPwd).subscribe(new Observer<AVNull>() {
                        public void onSubscribe(Disposable disposable) {}
                        public void onNext(AVNull avNull) {
                            // 密码重置成功
                            mSubmitButton.setProgress(100);
                            mProgressDialog.dismiss();
                            Log.d(TAG, "User reset pwd successfully.");
                            Toast.makeText(getActivity(), "密码重置成功", Toast.LENGTH_SHORT).show();
                            User.logout();
                            startFragment(new LoginFragment());
                        }
                        public void onError(Throwable throwable) {
                            // 验证码不正确
                            Toast.makeText(getActivity(), "密码重置失败，请重试!", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "User reset pwd failed: " + throwable.getMessage());
                        }
                        public void onComplete() {}
                    });
                }
            }
        });
    }
}