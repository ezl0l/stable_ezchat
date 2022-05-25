package com.ezlol.mesh.stableezchat.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;

import com.ezlol.mesh.stableezchat.R;
import com.ezlol.mesh.stableezchat.asynctask.OnChatClickListener;
import com.ezlol.mesh.stableezchat.asynctask.UserSearchTask;
import com.ezlol.mesh.stableezchat.model.Chat;
import com.ezlol.mesh.stableezchat.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener,
        LoginFragment.OnLoginSuccessListener, OnChatClickListener, OnToolbarClickListener {
    public static final int REQUEST_PERMISSIONS = 101;

    private ChatsFragment chatsFragment;
    private BottomNavigationView bottomNavigationView;

    public static boolean isShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSIONS);
            return;
        }

        init();

        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        isShow = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isShow = false;
    }

    private void init() {
        String accessTokenJson = PreferenceManager.getDefaultSharedPreferences(this)
                .getString("accessToken", null);
        if(accessTokenJson == null) {
            LoginFragment loginFragment = new LoginFragment();
            loginFragment.setOnLoginSuccessListener(this);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, loginFragment)
                    .commit();
            return;
        }
        chatsFragment = (ChatsFragment) FragmentHelper.createInstance(new ChatsFragment(), this);
        chatsFragment.setOnChatClickListener(this);
        chatsFragment.setOnToolbarClickListener(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, chatsFragment)
                .commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            init();
        } else {
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch(item.getItemId()) {
            case R.id.bottom_nav_chats:

                break;

            default:
                return false;
        }
        return true;
    }

    @Override
    public void onLoginSuccess() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, FragmentHelper.createInstance(new ChatsFragment(), this))
                .commit();
    }

    @Override
    public void onChatClick(Chat chat, View view) {
        bottomNavigationView.setVisibility(View.GONE);
        if(chat.id == null)
            chat.id = 0;

        DialogFragment dialogFragment = new DialogFragment();

        Bundle bundle = new Bundle();
        bundle.putString("accessToken", PreferenceManager.getDefaultSharedPreferences(this)
                .getString("accessToken", null));
        bundle.putInt("chatId", chat.id);

        dialogFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, dialogFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onUserSearchButtonClick(View view) {
        UserSearchFragment userSearchFragment = new UserSearchFragment();
        userSearchFragment.setOnChatClickListener(this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, FragmentHelper.createInstance(userSearchFragment, this))
                .addToBackStack(null)
                .commit();
    }
}