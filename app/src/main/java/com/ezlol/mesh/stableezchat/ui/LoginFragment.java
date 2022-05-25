package com.ezlol.mesh.stableezchat.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ezlol.mesh.stableezchat.R;
import com.ezlol.mesh.stableezchat.api.API;
import com.ezlol.mesh.stableezchat.asynctask.LoginTask;

public class LoginFragment extends Fragment implements View.OnClickListener, LoginTask.OnFinishLoginListener {
    public interface OnLoginSuccessListener {
        void onLoginSuccess();
    }

    private EditText usernameEditText, passwordEditText;
    private ProgressBar loginProgressBar;
    private Button loginButton;

    private API api = null;

    private OnLoginSuccessListener onLoginSuccessListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        usernameEditText = v.findViewById(R.id.usernameEditText);
        passwordEditText = v.findViewById(R.id.passwordEditText);

        loginProgressBar = v.findViewById(R.id.loginProgressBar);

        loginButton = v.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.loginButton) {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            if(username.length() > 0 && password.length() > 0) {
                LoginTask loginTask = new LoginTask(this, api, username, password);
                loginTask.setOnFinishLoginListener(this);
                loginTask.execute();
            }
        }
    }

    @Override
    public void onFinishLogin(API api) {
        if(api == null) {
            Toast.makeText(getContext(), "Login failed", Toast.LENGTH_LONG).show();
            return;
        }
        if(onLoginSuccessListener != null)
            onLoginSuccessListener.onLoginSuccess();
    }

    public void setOnLoginSuccessListener(OnLoginSuccessListener onLoginSuccessListener) {
        this.onLoginSuccessListener = onLoginSuccessListener;
    }
}
