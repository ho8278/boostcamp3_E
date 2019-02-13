package com.teame.boostcamp.myapplication.ui.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ActivityLoginBinding;
import com.teame.boostcamp.myapplication.ui.MainActivity;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;
import com.teame.boostcamp.myapplication.ui.signup.SignUpActivity;

public class LoginActivity extends BaseMVPActivity<ActivityLoginBinding, LoginContract.Presenter> implements LoginContract.View {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_login;
    }

    @Override
    protected LoginContract.Presenter getPresenter() {
        return new LoginPresenter(this, FirebaseAuth.getInstance());
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        presenter.checkLogIn();
    }

    private void initView() {
        binding.buttonLogin.setOnClickListener(__ -> onLogInButtonClicked());
        binding.tvSignup.setOnClickListener(__ -> SignUpActivity.startActivity(this));
    }

    private void onLogInButtonClicked() {
        String email = binding.tietEmail.getText().toString();
        String password = binding.tietPassword.getText().toString();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            presenter.doLogIn(email, password);
        } else {
            showToast(getString(R.string.login_empty_input_error));
        }
    }


    // 로그인 성공 후 메인액티비티 이동 메서드
    @Override
    public void succeedLogIn() {
        showToast(getString(R.string.login_success));
        MainActivity.startActivity(this);
        finish();
    }

    @Override
    public void occurLogInError() {
        showToast(getString(R.string.login_error));
    }

    //로그인 체크 후 메인액티비티 이동 메서드
    @Override
    public void isLogIn(boolean logInCheck) {
        if (logInCheck) {
            MainActivity.startActivity(this);
            finish();
        }
    }

    @Override
    public void showLogInLoading(boolean visibility) {
        if (visibility) {
            binding.pbLoginloading.setVisibility(View.VISIBLE);
        } else {
            binding.pbLoginloading.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }
}
