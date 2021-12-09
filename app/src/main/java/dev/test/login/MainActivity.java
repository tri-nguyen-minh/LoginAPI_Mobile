package dev.test.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;

import dev.source.api.API;
import dev.source.api.WebAPI;
import dev.source.api.WebAPIListener;
import dev.source.model.User;

public class MainActivity extends AppCompatActivity {

    private static final String PREFERENCE = "PREFERENCE";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private long loginTime, currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        xử lí session, kiểm tra xem đã login chưa
        sharedPreferences = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);

        String studentId = sharedPreferences.getString("STUDENT_ID", "");
        if (!studentId.equals("")) {
            currentTime = new Date().getTime();
            loginTime = sharedPreferences.getLong("LOGIN_TIME", 0);
            if(loginTime != 0 && (currentTime-loginTime) < 60000)
                loginSuccess(new User());
        }

//        lấy username và password để login
        EditText txtUsername = (EditText)findViewById(R.id.editUsername);
        EditText txtPassword = (EditText)findViewById(R.id.editPassword);

//        xử lí login
        Button btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();

//                gọi API
                API webAPI = new WebAPI(MainActivity.this.getApplication());
                webAPI.login(username, password, new WebAPIListener() {
                    @Override
                    public void onSuccessfulLogin(User user) {
                        loginSuccess(user);
                    }
                });
            }
        });
    }

    private void loginSuccess(User user) {
        Intent intent = new Intent(getApplicationContext(), DisplayActivity.class);
        editor = sharedPreferences.edit();
        loginTime = new Date().getTime();
        editor.putString("STUDENT_ID", user.getUsername());
        editor.putLong("LOGIN_TIME", loginTime);
        editor.commit();

        intent.putExtra("USER", user);
        startActivity(intent);
    }
}